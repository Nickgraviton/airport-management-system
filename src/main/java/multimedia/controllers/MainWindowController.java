package multimedia.controllers;

import java.io.*;
import java.util.*;
import java.net.URL;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import multimedia.model.*;
import multimedia.util.Helper;

/**
 * Class responsible for the main window of the program
 */
public class MainWindowController {
    // 5 real world seconds correspond to 1 simulation minute
    private final int minuteDuration = 5;
    private final Timeline timeline;
    private Timer timer;
    private int currentTime;
    private int income;
    private Stage myStage;
    private final ObservableList<Flight> flightList;
    private final ObservableList<Gate> gateList;
    private final List<PopupController> popupList;

    @FXML private Label flightsArriving, availableParking, nextTenMinutes,
                totalIncome, totalTime, bottomText;
    @FXML private Menu detailsMenu;
    @FXML private AnchorPane flightForm;
    @FXML private TextField id, city, minutesToTakeOff;
    @FXML private ChoiceBox flightType, aircraftType;
    @FXML private CheckBox refueling, cleaning, passengers, loading;
    @FXML private Button submitButton;
    @FXML private MenuItem menuStart, menuLoad, menuExit, gates, flights, holding, nextDepartures;
    @FXML private TableView<Gate> gateTable;
    @FXML private TableColumn<Gate, String> gateType, gateId;
    @FXML private TableColumn<Gate, Circle> gateStatus;

    public MainWindowController () {
        flightList = FXCollections.observableArrayList();
        gateList = FXCollections.observableArrayList();
        popupList = new ArrayList<>();

        // Timers initialization for the clock and the scheduled events
        timeline = new Timeline(
            new KeyFrame(Duration.seconds(minuteDuration), e -> {
                currentTime++;
                Platform.runLater(() -> updateUI(null));
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timer = new Timer();
    }

    @FXML
    private void initialize() {
        gateType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));
        gateType.setSortable(false);
        gateId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        gateId.setSortable(false);
        gateStatus.setCellValueFactory(new PropertyValueFactory<>("circle"));
        gateStatus.setSortable(false);

        gateTable.setItems(gateList);
        gateTable.setSelectionModel(null);
    }

    public void setStage(Stage myStage) {
        this.myStage = myStage;
        this.myStage.setOnCloseRequest(e -> {
            e.consume();
            exitApplication();
        });
    }

    public ObservableList<Flight> getFlightList() { return flightList; }
    public ObservableList<Gate> getGateList() { return gateList; }
    public int getCurrentTime() { return currentTime; }
    public void setCurrentTime(int currentTime) { this.currentTime = currentTime; }
    public int getIncome() { return income; }
    public void setIncome(int income) { this.income = income; }
    public int getMinuteDuration() { return minuteDuration; }
    public Timer getTimer() { return timer; }
    public List<PopupController> getPopupList() { return popupList; }


    /**
     * Runs when the Start menu button is pressed. Begins the execution and
     * starts the timers.
     */
    @FXML
    private void startApplication() {
        timer = new Timer();
        detailsMenu.setDisable(false);
        flightForm.setDisable(false);

        List<Flight> ignoredFlights = new ArrayList<>();
        for (Flight f : flightList) {
            // During the initialization phase we either put the flight directly
            // in parked status or we ignore and remove it from the list
            if(Helper.service(gateList, f, "Parked", "Ignored").equals("Unavailable")){
                bottomText.setText("Flight with id " + f.getId() + " and city "
                        + f.getCity() + " could not be servied");
                ignoredFlights.add(f);
            } else {
                Helper.handleParked(this, f);
            }
        }
        Platform.runLater(() -> updateUI("Initialization complete"));
        flightList.removeAll(ignoredFlights);
        menuStart.setDisable(true);
        timeline.play();
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
        Stage scenarioStage = (Stage) chooseScenario.getDialogPane().getScene().getWindow();
        scenarioStage.getIcons().add(new Image(getClass()
                .getResourceAsStream("/pngs/plane_32x32.png")));
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

                Helper.readInput(this, airportURL, setupURL);
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
        Stage confirmationStage = (Stage) confirmation.getDialogPane().getScene().getWindow();
        confirmationStage.getIcons().add(new Image(getClass()
                .getResourceAsStream("/pngs/plane_32x32.png")));
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
        if (loading.isSelected() && aircraftType.getValue().toString().toLowerCase().equals("single-engine")) {
            showError("(Un)Loading cannot be selected for single-engine aircrafts");
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
                requestedServices, currentTime);
        flightList.add(f);

        String msg;
        String returnStatus = Helper.service(gateList, f, "Landing", "Holding");
        if (returnStatus.equals("Unavailable")) {
            msg = "The flight was put on hold until a suitable gate is freed.";
        } else {
            msg = "The flight with id " + f.getId() + " from " + f.getCity()
                    + " will land in gate with id " + returnStatus + ".";
            Helper.handleLanding(this, f);
        }
        showInfo(msg);
        id.setText("");
        city.setText("");
        minutesToTakeOff.setText("");
        Platform.runLater(() -> updateUI("Flight with id " + f.getId() + " is in " + f.getStatus() + " status."));
    }

    @FXML
    private void gatesPopup() {
        popupList.add(new PopupController(this, "Gates"));
    }

    @FXML
    private void flightsPopup() {
        popupList.add(new PopupController(this, "Flights"));
    }

    @FXML
    private void delayedPopup() {
        popupList.add(new PopupController(this, "Delayed"));
    }

    @FXML
    private void holdingPopup() {
        popupList.add(new PopupController(this, "Holding"));
    }

    @FXML
    private void nextDeparturesPopup() {
        popupList.add(new PopupController(this, "Next Departures"));
    }

    /**
     * Updates all UI elements and popups
     * @param msg optional message that appears on the bottom of the screen
     */
    public void updateUI(String msg) {
        long incomingFlights, available, nextTen;
        incomingFlights = flightList.stream()
                .filter(f -> (f.getStatus().equals("Landing") || f.getStatus().equals("Holding"))).count();
        available = gateList.stream().filter(g -> g.getEmpty() == true).count();
        nextTen = flightList.stream().filter(f -> (f.getLeavesOn() > 0  && f.getLeavesOn() - currentTime <= 10)).count();

        flightsArriving.setText(Long.toString(incomingFlights));
        availableParking.setText(Long.toString(available));
        nextTenMinutes.setText(Long.toString(nextTen));
        totalIncome.setText(Integer.toString(income));
        totalTime.setText(String.format("%02d:%02d", currentTime / 60, currentTime % 60));

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
        Stage errorStage = (Stage) error.getDialogPane().getScene().getWindow();
        errorStage.getIcons().add(new Image(getClass()
                .getResourceAsStream("/pngs/plane_32x32.png")));
        error.showAndWait();
    }

    private void showInfo(String msg) {
        Alert info = new Alert(AlertType.INFORMATION, msg);
        info.setTitle("Airport Management System");
        info.setHeaderText("Success");
        Stage errorStage = (Stage) info.getDialogPane().getScene().getWindow();
        errorStage.getIcons().add(new Image(getClass()
                .getResourceAsStream("/pngs/plane_32x32.png")));
        info.showAndWait();
    }

    private void cleanup() {
        currentTime = 0;
        income = 0;
        flightList.clear();
        gateList.clear();
        flightsArriving.setText("0");
        availableParking.setText("0");
        nextTenMinutes.setText("0");
        totalIncome.setText("0");
        totalTime.setText("00:00");
    }

    public void stopTimers() {
        timer.cancel();
        timer.purge();
        timeline.stop();
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
