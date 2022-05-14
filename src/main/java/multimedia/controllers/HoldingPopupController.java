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

/**
 * Class responsible for the holding flights popup.
 */
public class HoldingPopupController extends PopupController {
    TableView<Flight> holdingTable;

    /**
     * @param owner the owner window controller instance
     */
    public HoldingPopupController(MainWindowController owner) {
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
                airport.getFlightList().stream()
                        .filter(f -> f.getStatus().equals("Holding"))
                        .collect(Collectors.toList()));
        holdingTable.setItems(holding);
        anchor.getChildren().add(holdingTable);
        AnchorPane.setBottomAnchor(holdingTable, 0.0);
        AnchorPane.setTopAnchor(holdingTable, 0.0);
        AnchorPane.setLeftAnchor(holdingTable, 0.0);
        AnchorPane.setRightAnchor(holdingTable, 0.0);

        Scene scene = new Scene(anchor, 500, 500);
        popup.setScene(scene);
        popup.show();
        popup.setOnCloseRequest(e -> owner.getPopupList().remove(this));
    }

    public void refresh() {
        ObservableList<Flight> holding = FXCollections.observableArrayList(
                airport.getFlightList().stream()
                        .filter(f -> f.getStatus().equals("Holding"))
                        .collect(Collectors.toList()));
        holdingTable.setItems(holding);
        holdingTable.refresh();
    }
}
