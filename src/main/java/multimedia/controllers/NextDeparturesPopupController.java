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

public class NextDeparturesPopupController extends PopupController {
    TableView<Flight> nextDeparturesTable;

    public NextDeparturesPopupController(MainWindowController owner) {
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
                airport.getFlightList().stream()
                        .filter(f -> (f.getStatus().equals("Parked") &&
                                f.getLeavesOn() - timeScheduler.getCurrentTime() <= 10))
                        .collect(Collectors.toList()));
        nextDeparturesTable.setItems(nextDepartures);
        anchor.getChildren().add(nextDeparturesTable);
        AnchorPane.setBottomAnchor(nextDeparturesTable, 0.0);
        AnchorPane.setTopAnchor(nextDeparturesTable, 0.0);
        AnchorPane.setLeftAnchor(nextDeparturesTable, 0.0);
        AnchorPane.setRightAnchor(nextDeparturesTable, 0.0);

        Scene scene = new Scene(anchor, 500, 500);
        popup.setScene(scene);
        popup.show();
        popup.setOnCloseRequest(e -> owner.getPopupList().remove(this));
    }

    @Override
    public void refresh() {
        ObservableList<Flight> nextDepartures = FXCollections.observableArrayList(
                airport.getFlightList().stream()
                        .filter(f -> (f.getStatus().equals("Parked") &&
                                f.getLeavesOn() - timeScheduler.getCurrentTime() <= 10))
                        .collect(Collectors.toList()));
        nextDeparturesTable.setItems(nextDepartures);
        nextDeparturesTable.refresh();
    }
}
