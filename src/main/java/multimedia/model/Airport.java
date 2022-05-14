package multimedia.model;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import multimedia.controllers.MainWindowController;
import multimedia.util.Helper;
import multimedia.util.TimeScheduler;

import java.util.List;
import java.util.TimerTask;

/*
 * Singleton class
 */
public class Airport {
    private int income;
    private final ObservableList<Flight> flightList;
    private final ObservableList<Gate> gateList;
    private final TimeScheduler timeScheduler;
    private static final Airport airport = new Airport();

    private Airport() {
        income = 0;
        flightList = FXCollections.observableArrayList();
        gateList = FXCollections.observableArrayList();
        timeScheduler = TimeScheduler.getInstance();
    }

    public static Airport getInstance() {
        return airport;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public ObservableList<Flight> getFlightList() {
        return flightList;
    }

    public ObservableList<Gate> getGateList() {
        return gateList;
    }

    /**
     * Processes a flight and assigns it to a gate if there is an available one.
     *
     * @param f             the flight that will be processed
     * @param successStatus the new status of the flight if it is successfully assigned to a gate
     * @param failureStatus the new status of the flight if it is not successfully assigned to a gate
     * @return the gate's id if the assignment was successful or the string "Unavailable" if it wasn't
     */
    public String service(Flight f, String successStatus, String failureStatus) {
        Gate assignedGate = null;
        for (Gate g : gateList) {
            if (g.getFlightsAllowed().get(f.getFlightType())
                    && g.getAircraftAllowed().get(f.getAircraftType())
                    && f.getMinutesToTakeOff() + f.getRequestTimeStamp() < g.getMaxStay()
                    && g.getEmpty()) {
                f.setStatus(successStatus);
                f.setGate(g);
                g.setAssignedFlight(f);
                g.setEmpty(false);
                assignedGate = g;
                break;
            }
        }
        if (assignedGate == null) {
            f.setStatus(failureStatus);
            return "Unavailable";
        } else {
            return assignedGate.getId();
        }
    }

    /**
     * Handles a flight's landing and creates a timer task that runs when the flight
     * has completed its landing and is in "Parked" status.
     *
     * @param controller the controller to which the timer task will be added
     * @param f          the flight that will be processed
     */
    public void handleLanding(MainWindowController controller, Flight f) {
        int landingTime = 0;
        switch (f.getAircraftType()) {
            case "single-engine":
                landingTime = 6;
                break;
            case "turboprop":
                landingTime = 4;
                break;
            case "jet":
                landingTime = 2;
        }

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handleParked(controller, f);
            }
        };
        timeScheduler.getTimer().schedule(task, landingTime * timeScheduler.getMinuteDuration() * 1000L);
    }

    /**
     * Handles a flight's transition to "Parked" status and creates a timer task
     * that runs when the flight is ready for takeoff.
     * <p>
     * We assume that minutesToTakeOff start counting down
     * after the flight has parked.
     * <p>
     * The exact takeoff time is randomly selected. The minimum stay is 5 minutes
     * and the maximum is twice the minutesToTakeOff.
     *
     * @param controller the controller to which the timer task will be added
     * @param f          the flight that will be processed
     */
    public void handleParked(MainWindowController controller, Flight f) {
        f.setStatus("Parked");

        int rand = Helper.generateRandom(5, 2 * f.getMinutesToTakeOff());
        f.setLeavesOn(timeScheduler.getCurrentTime() + rand);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handleTakeOff(controller, f);
            }
        };
        timeScheduler.getTimer().schedule(task, rand * timeScheduler.getMinuteDuration() * 1000L);
        Platform.runLater(() -> controller.updateUI("Flight with id " + f.getId()
                + " completed its landing and is now parked in Gate " + f.getGate().getId() + "."));
    }

    /**
     * Handles a flight's takeoff calculating the revenue and assigning a flight to
     * the freed gate if there is a suitable flight in "Holding".
     *
     * @param controller the controller to which the revenue will be added
     * @param f          the flight that will be processed
     */
    public void handleTakeOff(MainWindowController controller, Flight f) {
        int actualMinutesToTakeOff = f.getLeavesOn() - f.getRequestTimeStamp();
        int cost = f.getGate().getCost();
        int finalCost = cost;

        List<String> services = f.getRequestedServices();
        for (String service : services) {
            switch (service) {
                case "refuel":
                    finalCost += cost * 0.25;
                    break;
                case "cleaning":
                case "passenger carrying":
                    finalCost += cost * 0.02;
                    break;
                case "(un)loading":
                    finalCost += cost * 0.05;
            }
        }
        // Compare the actual take-off time with the original projected value
        if (actualMinutesToTakeOff >= f.getMinutesToTakeOff()) {
            // Delayed flights get charged twice as much
            finalCost *= 2;
        } else if (actualMinutesToTakeOff < f.getMinutesToTakeOff() - 25) {
            // 20% reduction
            finalCost *= 0.8;
        } else if (actualMinutesToTakeOff < f.getMinutesToTakeOff() - 10) {
            // 10% reduction
            finalCost *= 0.9;
        }
        airport.setIncome(airport.getIncome() + finalCost);
        f.getGate().setEmpty(true);
        f.getGate().setAssignedFlight(null);
        airport.getFlightList().remove(f);

        for (Flight flight : airport.getFlightList()) {
            if (flight.getStatus().equals("Holding")) {
                if (!service(flight, "Landing", "Holding").equals("Unavailable")) {
                    handleLanding(controller, flight);
                }
            }
        }

        final String flightPayed = Integer.toString(finalCost);
        Platform.runLater(() -> controller.updateUI("Flight with id " + f.getId() + " left and payed " + flightPayed + "."));
    }
}
