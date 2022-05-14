package multimedia.controllers;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import multimedia.model.Airport;
import multimedia.util.TimeScheduler;

/**
 * Base class for all the popup windows.
 */
public class PopupController {
    protected final Airport airport;
    protected final TimeScheduler timeScheduler;
    final Stage popup;
    final AnchorPane anchor;

    public PopupController() {
        airport = Airport.getInstance();
        timeScheduler = TimeScheduler.getInstance();

        popup = new Stage();
        popup.setTitle("Airport Management System");

        anchor = new AnchorPane();
    }

    public void refresh() {}
}
