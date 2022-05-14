package multimedia.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for all gate types.
 */
public class BaseGate {
    /**
     * Gate id
     */
    protected final String id;
    /**
     * Base cost of staying at gate
     */
    protected final int cost;
    /**
     * Boolean value that is true when the gate is empty
     */
    protected boolean empty;
    /**
     * String containing the gate type
     */
    protected final String category;
    /**
     * Max number of minutes an aircraft can stay at the gate
     */
    protected final int maxStay;
    /**
     * Mapping of flights and whether they are allowed at the gate
     */
    protected final Map<String, Boolean> flightsAllowed;
    /**
     * Mapping of aircraft and whether they are allowed at the gate
     */
    protected final Map<String, Boolean> aircraftAllowed;
    /**
     * Mapping of service and whether they are provided at the gate
     */
    protected final Map<String, Boolean> providedServices;
    /**
     * GUI circle that is green when the gate is empty and red when it is not
     */
    protected final Circle circle;
    /**
     * Flight assigned to the gate
     */
    protected Flight assignedFlight;

    public BaseGate(String id, int cost, String category, int maxStay) {
        this.id = id;
        this.cost = cost;
        empty = true;
        this.category = category;
        this.maxStay = maxStay;
        flightsAllowed = new HashMap<>();
        aircraftAllowed = new HashMap<>();
        providedServices = new HashMap<>();
        circle = new Circle(5, Color.GREEN);
        assignedFlight = null;
    }

    public boolean getEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        if (empty)
            circle.setFill(Color.GREEN);
        else
            circle.setFill(Color.RED);
        this.empty = empty;
    }

    public String getId() {
        return id;
    }

    public int getCost() {
        return cost;
    }

    public String getCategory() {
        return category;
    }

    public int getMaxStay() {
        return maxStay;
    }

    public Map<String, Boolean> getFlightsAllowed() {
        return flightsAllowed;
    }

    public Map<String, Boolean> getAircraftAllowed() {
        return aircraftAllowed;
    }

    public Map<String, Boolean> getProvidedServices() {
        return providedServices;
    }

    public Circle getCircle() {
        return circle;
    }

    public Flight getAssignedFlight() {
        return assignedFlight;
    }

    public void setAssignedFlight(Flight assignedFlight) {
        this.assignedFlight = assignedFlight;
    }
}
