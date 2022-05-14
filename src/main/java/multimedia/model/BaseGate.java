package multimedia.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.Map;

public class BaseGate {
    protected final String id;
    protected final int cost;
    protected boolean empty;
    protected final String category;
    protected final int maxStay;
    protected final Map<String, Boolean> flightsAllowed;
    protected final Map<String, Boolean> aircraftAllowed;
    protected final Map<String, Boolean> providedServices;
    protected final Circle circle;
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
