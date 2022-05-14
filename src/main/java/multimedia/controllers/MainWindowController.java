package multimedia.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import multimedia.exceptions.InvalidInputException;
import multimedia.model.Airport;
import multimedia.model.BaseGate;
import multimedia.model.Flight;
import multimedia.util.Initializer;
import multimedia.util.TimeScheduler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Timer;

/**
 * Class responsible for the main window of the program
 */
public class MainWindowController {
    private final Airport airport;
    private final Initializer initializer;

    private final TimeScheduler timeScheduler;
    private Stage myStage;
    private final List<PopupController> popupList;

    @FXML
    private Label flightsArriving, availableParking, nextTenMinutes,
            totalIncome, totalTime, bottomText;
    @FXML
    private Menu detailsMenu;
    @FXML
    private AnchorPane flightForm;
    @FXML
    private TextField id, city, minutesToTakeOff;
    @FXML
    private ChoiceBox flightType, aircraftType;
    @FXML
    private CheckBox refueling, cleaning, passengers, loading;
    @FXML
    private Button submitButton;
    @FXML
    private MenuItem menuStart, menuLoad, menuExit, gates, flights, holding, nextDepartures;
    @FXML
    private TableView<BaseGate> gateTable;
    @FXML
    private TableColumn<BaseGate, String> gateType, gateId;
    @FXML
    private TableColumn<BaseGate, Circle> gateStatus;

    public List<PopupController> getPopupList() {
        return popupList;
    }

    public MainWindowController() {
        airport = Airport.getInstance();
        initializer = Initializer.getInstance();
        timeScheduler = TimeScheduler.getInstance();
        timeScheduler.setTimeline(this);
        popupList = new ArrayList<>();
    }

    @FXML
    private void initialize() {
        gateType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));
        gateType.setSortable(false);
        gateId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        gateId.setSortable(false);
        gateStatus.setCellValueFactory(new PropertyValueFactory<>("circle"));
        gateStatus.setSortable(false);

        gateTable.setItems(airport.getGateList());
        gateTable.setSelectionModel(null);
    }

    /**
     * The stage is passed to the controller when the application is started in the Main class.
     */
    public void setStage(Stage myStage) {
        this.myStage = myStage;
        this.myStage.setOnCloseRequest(event -> {
            event.consume();
            exitApplication();
        });
    }

    /**
     * Runs when the Start menu button is pressed. Begins the execution and starts the timers.
     */
    @FXML
    private void startApplication() {
        timeScheduler.setTimer(new Timer());
        detailsMenu.setDisable(false);
        flightForm.setDisable(false);

        List<Flight> ignoredFlights = new ArrayList<>();
        for (Flight f : airport.getFlightList()) {
            // During the initialization phase we either put the flight directly
            // in parked status or we ignore and remove it from the list
            if (airport.service(f, "Parked", "Ignored").equals("Unavailable")) {
                bottomText.setText("Flight with id " + f.getId() + " and city "
                        + f.getCity() + " could not be serviced");
                ignoredFlights.add(f);
            } else {
                airport.handleParked(this, f);
            }
        }
        Platform.runLater(() -> updateUI("Initialization complete"));
        airport.getFlightList().removeAll(ignoredFlights);
        menuStart.setDisable(true);
        timeScheduler.getTimeline().play();
    }

    /**
     * Runs when the Load menu button is pressed. Initializes the application
     * and reads the files inside the multimedia folder.
     */
    @FXML
    private void loadConfiguration() throws IOException {
        TextInputDialog chooseScenario = new TextInputDialog("default");
        chooseScenario.setTitle("Airport Management System");
        chooseScenario.setHeaderText("Insert the scenario ID to be loaded");
        chooseScenario.setContentText("Scenario ID");
        Optional<String> retValue = chooseScenario.showAndWait();

        // If the OK button in the TextInputDialog window was pressed the
        // optional string has a value, else skip this block
        if (retValue.isPresent()) {
            String scenarioId = retValue.get();

            URL airportURL = getClass()
                    .getResource("/medialab/airport_" + scenarioId + ".txt");
            URL setupURL = getClass()
                    .getResource("/medialab/setup_" + scenarioId + ".txt");

            if (airportURL == null || setupURL == null) {
                showError("Invalid scenario ID or missing file(s)");
            } else {
                stopTimers();
                cleanup();
                disable();

                try {
                    initializer.readInput(airportURL, setupURL);
                } catch (InvalidInputException exception) {
                    handleError(exception.getMessage());
                }
                menuStart.setDisable(false);
            }
        }
    }

    /**
     * Runs when the Exit menu button is pressed. Stops the timers and c
     * loses the program gracefully.
     */
    @FXML
    private void exitApplication() {
        Alert confirmation = new Alert(AlertType.CONFIRMATION,
                "Are you sure you want to exit?");
        confirmation.setTitle("Airport Management System");
        confirmation.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    stopTimers();
                    this.myStage.close();
                });
    }

    /**
     * Runs when the Submit button in the flight request form is pressed. Handles
     * the incoming flight request if possible or puts in on hold.
     */
    @FXML
    private void addFlightRequest() {
        int flightMinutesToTakeOff;
        if (id.getText().equals("") || city.getText().equals("") || minutesToTakeOff.getText().equals("")) {
            showError("Missing form field(s)");
            return;
        }
        try {
            flightMinutesToTakeOff = Integer.parseInt(minutesToTakeOff.getText());
            if (flightMinutesToTakeOff < 5) {
                showError("Minutes to take off need to be at least 5");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Minutes to take off field needs to contain a number");
            return;
        }
        if (loading.isSelected() && aircraftType.getValue().toString().equalsIgnoreCase("single-engine")) {
            showError("(Un)Loading cannot be selected for single-engine aircraft");
            return;
        }

        List<String> requestedServices = new ArrayList<>();
        if (refueling.isSelected())
            requestedServices.add("refuel");
        if (cleaning.isSelected())
            requestedServices.add("cleaning");
        if (passengers.isSelected())
            requestedServices.add("passenger carrying");
        if (loading.isSelected())
            requestedServices.add("(un)loading");
        Flight f = new Flight(id.getText(), city.getText(), flightType.getValue().toString().toLowerCase(),
                aircraftType.getValue().toString().toLowerCase(), flightMinutesToTakeOff,
                requestedServices, timeScheduler.getCurrentTime());
        airport.getFlightList().add(f);

        String msg;
        String returnStatus = airport.service(f, "Landing", "Holding");
        if (returnStatus.equals("Unavailable")) {
            msg = "The flight was put on hold until a suitable gate is freed.";
        } else {
            msg = "The flight with id " + f.getId() + " from " + f.getCity()
                    + " will land in gate with id " + returnStatus + ".";
            airport.handleLanding(this, f);
        }
        showInfo(msg);
        id.setText("");
        city.setText("");
        minutesToTakeOff.setText("");
        Platform.runLater(() -> updateUI("Flight with id " + f.getId() + " is in " + f.getStatus() + " status."));
    }

    @FXML
    private void gatesPopup() {
        popupList.add(new GatesPopupController(this));
    }

    @FXML
    private void flightsPopup() {
        popupList.add(new FlightsPopupController(this));
    }

    @FXML
    private void delayedPopup() {
        popupList.add(new DelayedPopupController(this));
    }

    @FXML
    private void holdingPopup() {
        popupList.add(new HoldingPopupController(this));
    }

    @FXML
    private void nextDeparturesPopup() {
        popupList.add(new NextDeparturesPopupController(this));
    }

    /**
     * Updates all UI elements and popups. Invoked both periodically and on demand.
     *
     * @param msg optional message that appears at the bottom of the screen
     */
    public void updateUI(String msg) {
        long incomingFlights, available, nextTen;
        incomingFlights = airport.getFlightList().stream()
                .filter(f -> (f.getStatus().equals("Landing") || f.getStatus().equals("Holding"))).count();
        available = airport.getGateList().stream().filter(BaseGate::getEmpty).count();
        nextTen = airport.getFlightList().stream().filter(f -> (f.getLeavesOn() > 0 && f.getLeavesOn() - timeScheduler.getCurrentTime() <= 10)).count();

        flightsArriving.setText(Long.toString(incomingFlights));
        availableParking.setText(Long.toString(available));
        nextTenMinutes.setText(Long.toString(nextTen));
        totalIncome.setText(Integer.toString(airport.getIncome()));
        totalTime.setText(String.format("%02d:%02d", timeScheduler.getCurrentTime() / 60, timeScheduler.getCurrentTime() % 60));

        if (msg != null)
            bottomText.setText(msg);
        gateTable.refresh();
        for (PopupController popup : popupList) {
            popup.refresh();
        }
    }

    private void showError(String msg) {
        Alert error = new Alert(AlertType.ERROR, msg);
        error.setTitle("Airport Management System");
        error.showAndWait();
    }

    private void showInfo(String msg) {
        Alert info = new Alert(AlertType.INFORMATION, msg);
        info.setTitle("Airport Management System");
        info.setHeaderText("Success");
        info.showAndWait();
    }

    private void cleanup() {
        timeScheduler.setCurrentTime(0);
        airport.setIncome(0);
        airport.getFlightList().clear();
        airport.getGateList().clear();
        flightsArriving.setText("0");
        availableParking.setText("0");
        nextTenMinutes.setText("0");
        totalIncome.setText("0");
        totalTime.setText("00:00");
    }

    public void stopTimers() {
        timeScheduler.getTimer().cancel();
        timeScheduler.getTimer().purge();
        timeScheduler.getTimeline().stop();
    }

    private void disable() {
        menuStart.setDisable(true);
        detailsMenu.setDisable(true);
        flightForm.setDisable(true);
    }

    public void handleError(String msg) {
        showError(msg);
        cleanup();
        disable();
    }
}
