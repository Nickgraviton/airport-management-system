package multimedia.controllers;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import multimedia.model.Airport;
import multimedia.util.TimeScheduler;

/**
 * Base controller class for all the popup windows.
 * Creates a new stage and puts an empty anchor-pane inside that the individual popups use.
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

    /**
     * Function that is meant to be overridden by subclasses that refresh the popup data.
     */
    public void refresh() {}
}
