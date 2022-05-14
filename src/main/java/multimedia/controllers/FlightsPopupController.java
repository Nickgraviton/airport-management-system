package multimedia.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import multimedia.model.Flight;
import multimedia.util.Helper;

/**
 * Class responsible for flights popup.
 */
public class FlightsPopupController extends PopupController {
    TableView<Flight> flightTable;

    /**
     * @param owner the owner window controller instance
     */
    public FlightsPopupController(MainWindowController owner) {
        flightTable = new TableView<>();
        flightTable.setSelectionModel(null);

        TableColumn<Flight, String> flightId = new TableColumn<>("Flight");
        TableColumn<Flight, String> flightCity = new TableColumn<>("City");
        TableColumn<Flight, String> aircraftType = new TableColumn<>("Aircraft type");
        TableColumn<Flight, String> flightStatus = new TableColumn<>("Status");
        TableColumn<Flight, String> gateIdOfFlight = new TableColumn<>("Gate");
        TableColumn<Flight, String> flightTakeOffTime = new TableColumn<>("Takeoff time");

        flightId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        flightCity.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCity()));
        aircraftType.setCellValueFactory(data -> new SimpleStringProperty(Helper.capitalize(data.getValue().getAircraftType())));
        flightStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        gateIdOfFlight.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGate() == null
                ? "" : data.getValue().getGate().getId()));
        flightTakeOffTime.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLeavesOn() < 0
                ? "" : Integer.toString(data.getValue().getLeavesOn())));

        flightTable.getColumns().addAll(flightId, flightCity, aircraftType, flightStatus, gateIdOfFlight, flightTakeOffTime);
        for (TableColumn c : flightTable.getColumns()) {
            c.setMinWidth(100);
            c.setSortable(false);
        }
        flightTable.setItems(airport.getFlightList());
        anchor.getChildren().add(flightTable);
        AnchorPane.setBottomAnchor(flightTable, 0.0);
        AnchorPane.setTopAnchor(flightTable, 0.0);
        AnchorPane.setLeftAnchor(flightTable, 0.0);
        AnchorPane.setRightAnchor(flightTable, 0.0);

        Scene scene = new Scene(anchor, 500, 500);
        popup.setScene(scene);
        popup.show();
        popup.setOnCloseRequest(e -> owner.getPopupList().remove(this));
    }

    public void refresh() {
        flightTable.setItems(airport.getFlightList());
        flightTable.refresh();
    }
}
