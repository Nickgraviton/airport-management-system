package multimedia.controllers;

import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import multimedia.model.Flight;
import multimedia.model.Gate;
import multimedia.util.Helper;

/**
 * Class responsible for the Details menu popups.
 */
public class PopupController {
    final MainWindowController owner;
    final String popupType;
    final Stage popup;
    final AnchorPane anchor;
    TableView<Gate> gateTable;
    TableView<Flight> flightTable;
    TableView<Flight> delayedTable;
    TableView<Flight> holdingTable;
    TableView<Flight> nextDeparturesTable;
    
    /**
     * @param owner the owner window controller instance
     * @param popupType defines the popup type 
     */    
    public PopupController(MainWindowController owner, String popupType) {
        this.owner = owner;
        this.popupType = popupType;
        popup = new Stage();
        popup.getIcons().add(new Image(getClass()
                .getResourceAsStream("/pngs/plane_32x32.png")));
        popup.setTitle("Airport Management System");
        anchor = new AnchorPane();

        switch(popupType) {
            case "Gates":
                gateTable = new TableView<>();
                gateTable.setSelectionModel(null);
                
                TableColumn<Gate, String> gateId = new TableColumn<>("Gate");
                TableColumn<Gate, String> gateStatus = new TableColumn<>("Status");
                TableColumn<Gate, String> flightIdInGate = new TableColumn<>("Flight");
                TableColumn<Gate, String> takeOffTime = new TableColumn<>("Takeoff time");
                
                gateId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
                gateStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmpty() ? "Empty" : "Taken"));
                flightIdInGate.setCellValueFactory(data -> new SimpleStringProperty(
                        data.getValue().getAssignedFlight() == null ? "" : data.getValue().getAssignedFlight().getId()));
                takeOffTime.setCellValueFactory(data -> new SimpleStringProperty(
                        (data.getValue().getAssignedFlight() == null || !data.getValue().getAssignedFlight().getStatus().equals("Parked"))
                                ? "" : Integer.toString(data.getValue().getAssignedFlight().getLeavesOn())));
                
                gateTable.getColumns().addAll(gateId, gateStatus, flightIdInGate, takeOffTime);
                for (TableColumn c : gateTable.getColumns()) {
                    c.setMinWidth(100);
                    c.setSortable(false);
                }
                gateTable.setItems(owner.getGateList());
                anchor.getChildren().add(gateTable);
                AnchorPane.setBottomAnchor(gateTable, 0.0);
                AnchorPane.setTopAnchor(gateTable, 0.0);
                AnchorPane.setLeftAnchor(gateTable, 0.0);
                AnchorPane.setRightAnchor(gateTable, 0.0);
                break;
            case "Flights":
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
                flightTable.setItems(owner.getFlightList());
                anchor.getChildren().add(flightTable);
                AnchorPane.setBottomAnchor(flightTable, 0.0);
                AnchorPane.setTopAnchor(flightTable, 0.0);
                AnchorPane.setLeftAnchor(flightTable, 0.0);
                AnchorPane.setRightAnchor(flightTable, 0.0);
                break;
            case "Delayed":
                delayedTable = new TableView<>();
                delayedTable.setSelectionModel(null);
                
                TableColumn<Flight, String> delayedGateId = new TableColumn<>("Gate");
                TableColumn<Flight, String> delayedFlightId = new TableColumn<>("Flight");
                TableColumn<Flight, String> delayedFlightType = new TableColumn<>("Flight type");
                TableColumn<Flight, String> delayedAircraftType = new TableColumn<>("Aircraft type");
                TableColumn<Flight, String> delayedTakeOffTime = new TableColumn<>("Takeoff time");
                
                delayedGateId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGate().getId()));
                delayedFlightId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
                delayedFlightType.setCellValueFactory(data -> new SimpleStringProperty(Helper.capitalize(data.getValue().getFlightType())));
                delayedAircraftType.setCellValueFactory(data -> new SimpleStringProperty(Helper.capitalize(data.getValue().getAircraftType())));
                delayedTakeOffTime.setCellValueFactory(data -> new SimpleStringProperty(Integer.toString(data.getValue().getLeavesOn())));

                delayedTable.getColumns().addAll(delayedGateId, delayedFlightId, delayedFlightType, delayedAircraftType, delayedTakeOffTime);
                for (TableColumn c : delayedTable.getColumns()) {
                    c.setMinWidth(100);
                    c.setSortable(false);
                }
                ObservableList<Flight> delayed = FXCollections.observableArrayList(
                        owner.getFlightList().stream()
                        .filter(f -> (f.getStatus().equals("Parked") &&
                                f.getLeavesOn() - f.getRequestTimeStamp() > f.getMinutesToTakeOff()))
                        .collect(Collectors.toList()));
                delayedTable.setItems(delayed);
                anchor.getChildren().add(delayedTable);
                AnchorPane.setBottomAnchor(delayedTable, 0.0);
                AnchorPane.setTopAnchor(delayedTable, 0.0);
                AnchorPane.setLeftAnchor(delayedTable, 0.0);
                AnchorPane.setRightAnchor(delayedTable, 0.0);
                break;
            case "Holding":
                holdingTable = new TableView<>();
                holdingTable.setSelectionModel(null);
                
                TableColumn<Flight, String> holdingFlightId = new TableColumn<>("Flight");
                TableColumn<Flight, String> holdingFlightType = new TableColumn<>("Flight type");
                TableColumn<Flight, String> holdingAircraftType = new TableColumn<>("Aircraft type");
                TableColumn<Flight, String> holdingRequestTimeStamp = new TableColumn<>("Time of request");
                
                holdingFlightId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
                holdingFlightType.setCellValueFactory(data -> new SimpleStringProperty(Helper.capitalize(data.getValue().getFlightType())));
                holdingAircraftType.setCellValueFactory(data -> new SimpleStringProperty(Helper.capitalize(data.getValue().getAircraftType())));
                holdingRequestTimeStamp.setCellValueFactory(data -> new SimpleStringProperty(Integer.toString(data.getValue().getRequestTimeStamp())));

                holdingTable.getColumns().addAll(holdingFlightId, holdingFlightType, holdingAircraftType, holdingRequestTimeStamp);
                for (TableColumn c : holdingTable.getColumns()) {
                    c.setMinWidth(100);
                    c.setSortable(false);
                }
                ObservableList<Flight> holding = FXCollections.observableArrayList(
                        owner.getFlightList().stream()
                        .filter(f -> f.getStatus().equals("Holding"))
                        .collect(Collectors.toList()));
                holdingTable.setItems(holding);
                anchor.getChildren().add(holdingTable);
                AnchorPane.setBottomAnchor(holdingTable, 0.0);
                AnchorPane.setTopAnchor(holdingTable, 0.0);
                AnchorPane.setLeftAnchor(holdingTable, 0.0);
                AnchorPane.setRightAnchor(holdingTable, 0.0);
                break;
            case "Next Departures":
                nextDeparturesTable = new TableView<>();
                nextDeparturesTable.setSelectionModel(null);
                
                TableColumn<Flight, String> nextTenFlightId = new TableColumn<>("Flight");
                TableColumn<Flight, String> nextTenFlightType = new TableColumn<>("Flight type");
                TableColumn<Flight, String> nextTenAircraftType = new TableColumn<>("Aircraft type");
                
                nextTenFlightId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
                nextTenFlightType.setCellValueFactory(data -> new SimpleStringProperty(Helper.capitalize(data.getValue().getFlightType())));
                nextTenAircraftType.setCellValueFactory(data -> new SimpleStringProperty(Helper.capitalize(data.getValue().getAircraftType())));

                nextDeparturesTable.getColumns().addAll(nextTenFlightId, nextTenFlightType, nextTenAircraftType);
                for (TableColumn c : nextDeparturesTable.getColumns()) {
                    c.setMinWidth(100);
                    c.setSortable(false);
                }
                ObservableList<Flight> nextDepartures = FXCollections.observableArrayList(
                        owner.getFlightList().stream()
                        .filter(f -> (f.getStatus().equals("Parked") &&
                                f.getLeavesOn() - owner.getCurrentTime() <= 10))
                        .collect(Collectors.toList()));
                nextDeparturesTable.setItems(nextDepartures);
                anchor.getChildren().add(nextDeparturesTable);
                AnchorPane.setBottomAnchor(nextDeparturesTable, 0.0);
                AnchorPane.setTopAnchor(nextDeparturesTable, 0.0);
                AnchorPane.setLeftAnchor(nextDeparturesTable, 0.0);
                AnchorPane.setRightAnchor(nextDeparturesTable, 0.0);
                break;
        }
        Scene scene = new Scene(anchor, 500, 500);
        popup.setScene(scene);
        popup.show();
        popup.setOnCloseRequest(e -> owner.getPopupList().remove(this));
    }
    
    /**
     * Updates the popup's interface
     */
    public void refresh() {
        switch(popupType) {
            case "Gates":
                gateTable.setItems(owner.getGateList());
                gateTable.refresh();
                break;
            case "Flights":
                flightTable.setItems(owner.getFlightList());
                flightTable.refresh();
                break;
            case "Delayed":
                ObservableList<Flight> delayed = FXCollections.observableArrayList(
                        owner.getFlightList().stream()
                        .filter(f -> (f.getStatus().equals("Parked") &&
                                f.getLeavesOn() - f.getRequestTimeStamp() > f.getMinutesToTakeOff()))
                        .collect(Collectors.toList()));
                delayedTable.setItems(delayed);
                delayedTable.refresh();
                break;
            case "Holding":
                ObservableList<Flight> holding = FXCollections.observableArrayList(
                        owner.getFlightList().stream()
                        .filter(f -> f.getStatus().equals("Holding"))
                        .collect(Collectors.toList()));
                holdingTable.setItems(holding);
                holdingTable.refresh();
                break;
            case "Next Departures":
                ObservableList<Flight> nextDepartures = FXCollections.observableArrayList(
                        owner.getFlightList().stream()
                        .filter(f -> (f.getStatus().equals("Parked") &&
                                f.getLeavesOn() - owner.getCurrentTime() <= 10))
                        .collect(Collectors.toList()));
                nextDeparturesTable.setItems(nextDepartures);
                nextDeparturesTable.refresh();
        }
    }
}
