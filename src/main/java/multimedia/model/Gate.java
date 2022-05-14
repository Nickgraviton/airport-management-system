package multimedia.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.Map;

public class Gate {
    private final String id;
    private final int cost;
    private boolean empty;
    private final String category;
    private final int maxStay;
    private final Map<String, Boolean> flightsAllowed;
    private final Map<String, Boolean> aircraftAllowed;
    private final Map<String, Boolean> providedServices;
    private final Circle circle;
    private Flight assignedFlight;

    public Gate(String id, int cost, String category) {
        circle = new Circle(5, Color.GREEN);
        this.id = id;
        this.cost = cost;
        this.category = category;
        this.empty = true;
        this.assignedFlight = null;
        flightsAllowed = new HashMap<>();
        aircraftAllowed = new HashMap<>();
        providedServices = new HashMap<>();
        switch (category) {
            case "Gate":
                this.maxStay = 45;

                flightsAllowed.put("passenger", Boolean.TRUE);
                flightsAllowed.put("cargo", Boolean.FALSE);
                flightsAllowed.put("private", Boolean.FALSE);

                aircraftAllowed.put("single-engine", Boolean.FALSE);
                aircraftAllowed.put("turboprop", Boolean.TRUE);
                aircraftAllowed.put("jet", Boolean.TRUE);

                providedServices.put("refuel", Boolean.TRUE);
                providedServices.put("cleaning", Boolean.TRUE);
                providedServices.put("passenger carrying", Boolean.TRUE);
                providedServices.put("(un)loading", Boolean.TRUE);
                break;
            case "Cargo Gate":
                this.maxStay = 90;

                flightsAllowed.put("passenger", Boolean.FALSE);
                flightsAllowed.put("cargo", Boolean.TRUE);
                flightsAllowed.put("private", Boolean.FALSE);

                aircraftAllowed.put("single-engine", Boolean.FALSE);
                aircraftAllowed.put("turboprop", Boolean.TRUE);
                aircraftAllowed.put("jet", Boolean.TRUE);

                providedServices.put("refuel", Boolean.TRUE);
                providedServices.put("cleaning", Boolean.TRUE);
                providedServices.put("passenger carrying", Boolean.TRUE);
                providedServices.put("(un)loading", Boolean.TRUE);
                break;
            case "Zone A":
                maxStay = 90;

                flightsAllowed.put("passenger", Boolean.TRUE);
                flightsAllowed.put("cargo", Boolean.FALSE);
                flightsAllowed.put("private", Boolean.FALSE);

                aircraftAllowed.put("single-engine", Boolean.FALSE);
                aircraftAllowed.put("turboprop", Boolean.TRUE);
                aircraftAllowed.put("jet", Boolean.TRUE);

                providedServices.put("refuel", Boolean.TRUE);
                providedServices.put("cleaning", Boolean.TRUE);
                providedServices.put("passenger carrying", Boolean.TRUE);
                providedServices.put("(un)loading", Boolean.TRUE);
                break;
            case "Zone B":
                maxStay = 2 * 60;

                flightsAllowed.put("passenger", Boolean.TRUE);
                flightsAllowed.put("cargo", Boolean.TRUE);
                flightsAllowed.put("private", Boolean.TRUE);

                aircraftAllowed.put("single-engine", Boolean.FALSE);
                aircraftAllowed.put("turboprop", Boolean.TRUE);
                aircraftAllowed.put("jet", Boolean.TRUE);

                providedServices.put("refuel", Boolean.TRUE);
                providedServices.put("cleaning", Boolean.TRUE);
                providedServices.put("passenger carrying", Boolean.TRUE);
                providedServices.put("(un)loading", Boolean.TRUE);
                break;
            case "Zone C":
                maxStay = 3 * 60;

                flightsAllowed.put("passenger", Boolean.TRUE);
                flightsAllowed.put("cargo", Boolean.TRUE);
                flightsAllowed.put("private", Boolean.TRUE);

                aircraftAllowed.put("single-engine", Boolean.TRUE);
                aircraftAllowed.put("turboprop", Boolean.FALSE);
                aircraftAllowed.put("jet", Boolean.FALSE);

                providedServices.put("refuel", Boolean.TRUE);
                providedServices.put("cleaning", Boolean.TRUE);
                providedServices.put("passenger carrying", Boolean.TRUE);
                providedServices.put("(un)loading", Boolean.TRUE);
                break;
            case "General Parking":
                maxStay = 4 * 60;

                flightsAllowed.put("passenger", Boolean.TRUE);
                flightsAllowed.put("cargo", Boolean.TRUE);
                flightsAllowed.put("private", Boolean.TRUE);

                aircraftAllowed.put("single-engine", Boolean.TRUE);
                aircraftAllowed.put("turboprop", Boolean.TRUE);
                aircraftAllowed.put("jet", Boolean.TRUE);

                providedServices.put("refuel", Boolean.TRUE);
                providedServices.put("cleaning", Boolean.TRUE);
                providedServices.put("passenger carrying", Boolean.FALSE);
                providedServices.put("(un)loading", Boolean.FALSE);
                break;
            case "Long Stay":
                maxStay = 10 * 60;

                flightsAllowed.put("passenger", Boolean.FALSE);
                flightsAllowed.put("cargo", Boolean.TRUE);
                flightsAllowed.put("private", Boolean.TRUE);

                aircraftAllowed.put("single-engine", Boolean.TRUE);
                aircraftAllowed.put("turboprop", Boolean.TRUE);
                aircraftAllowed.put("jet", Boolean.TRUE);

                providedServices.put("refuel", Boolean.TRUE);
                providedServices.put("cleaning", Boolean.TRUE);
                providedServices.put("passenger carrying", Boolean.TRUE);
                providedServices.put("(un)loading", Boolean.TRUE);
                break;
            default:
                maxStay = -1;
                System.out.println("Invalid gate category");
        }
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