package multimedia.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import multimedia.model.Flight;
import multimedia.util.Helper;

import java.util.stream.Collectors;

public class DelayedPopupController extends PopupController {
    TableView<Flight> delayedTable;

    public DelayedPopupController(MainWindowController owner) {
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
                airport.getFlightList().stream()
                        .filter(f -> (f.getStatus().equals("Parked") &&
                                f.getLeavesOn() - f.getRequestTimeStamp() > f.getMinutesToTakeOff()))
                        .collect(Collectors.toList()));
        delayedTable.setItems(delayed);
        anchor.getChildren().add(delayedTable);
        AnchorPane.setBottomAnchor(delayedTable, 0.0);
        AnchorPane.setTopAnchor(delayedTable, 0.0);
        AnchorPane.setLeftAnchor(delayedTable, 0.0);
        AnchorPane.setRightAnchor(delayedTable, 0.0);

        Scene scene = new Scene(anchor, 500, 500);
        popup.setScene(scene);
        popup.show();
        popup.setOnCloseRequest(e -> owner.getPopupList().remove(this));
    }

    @Override
    public void refresh() {
        ObservableList<Flight> delayed = FXCollections.observableArrayList(
                airport.getFlightList().stream()
                        .filter(f -> (f.getStatus().equals("Parked") &&
                                f.getLeavesOn() - f.getRequestTimeStamp() > f.getMinutesToTakeOff()))
                        .collect(Collectors.toList()));
        delayedTable.setItems(delayed);
        delayedTable.refresh();
    }
}
