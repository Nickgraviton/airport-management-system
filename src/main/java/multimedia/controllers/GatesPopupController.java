package multimedia.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import multimedia.model.BaseGate;

/**
 * Class responsible for the gates popup.
 */
public class GatesPopupController extends PopupController {
    TableView<BaseGate> gateTable;
    /**
     * @param owner the owner window controller instance
     */
    public GatesPopupController(MainWindowController owner) {
        gateTable = new TableView<>();
        gateTable.setSelectionModel(null);

        TableColumn<BaseGate, String> gateId = new TableColumn<>("Gate");
        TableColumn<BaseGate, String> gateStatus = new TableColumn<>("Status");
        TableColumn<BaseGate, String> flightIdInGate = new TableColumn<>("Flight");
        TableColumn<BaseGate, String> takeOffTime = new TableColumn<>("Takeoff time");

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
        gateTable.setItems(airport.getGateList());
        anchor.getChildren().add(gateTable);
        AnchorPane.setBottomAnchor(gateTable, 0.0);
        AnchorPane.setTopAnchor(gateTable, 0.0);
        AnchorPane.setLeftAnchor(gateTable, 0.0);
        AnchorPane.setRightAnchor(gateTable, 0.0);

        Scene scene = new Scene(anchor, 500, 500);
        popup.setScene(scene);
        popup.show();
        popup.setOnCloseRequest(e -> owner.getPopupList().remove(this));
    }

    public void refresh() {
        gateTable.setItems(airport.getGateList());
        gateTable.refresh();
    }
}
