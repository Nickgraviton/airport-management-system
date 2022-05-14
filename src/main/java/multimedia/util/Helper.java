package multimedia.util;

import java.io.*;
import java.sql.Time;
import java.util.*;
import java.net.URL;
import multimedia.model.*;
import multimedia.controllers.MainWindowController;
import java.util.concurrent.ThreadLocalRandom;
import javafx.application.Platform;

/**
 * Static helper class that provides simple utilities and handles flight related
 * events.
 */
public class Helper {
    private static Airport airport;
    private static TimeScheduler timeScheduler;

    public Helper() {
        airport = Airport.getInstance();
        timeScheduler = TimeScheduler.getInstance();
    }

    /**
     * Map between input file gate numbers and a meaningful gate name
     */
    public final static Map<String, String> MAP_CATEGORIES;
    static{
        MAP_CATEGORIES = new HashMap<>();
        MAP_CATEGORIES.put("1", "Gate");
        MAP_CATEGORIES.put("2", "Cargo Gate");
        MAP_CATEGORIES.put("3", "Zone A");
        MAP_CATEGORIES.put("4", "Zone B");
        MAP_CATEGORIES.put("5", "Zone C");
        MAP_CATEGORIES.put("6", "General Parking");
        MAP_CATEGORIES.put("7", "Long Stay");

    }

   /**
   * Reads the input files.
   * @param controller the controller instance to which we add the data we create during the initialization
   * @param airportURL the URL of the airport initialization file that defines the gates
   * @param setupURL the URL of the flight initialization files that defines incoming flights
   * @throws IOException is thrown if opening a Stream/Reader fails
   */
    public static void readInput(MainWindowController controller,
            URL airportURL, URL setupURL) throws IOException {
        InputStream airportStream = airportURL.openStream();
        InputStream setupStream = setupURL.openStream();

        BufferedReader airportReader = new BufferedReader(
                new InputStreamReader(airportStream));
        BufferedReader setupReader = new BufferedReader(
                new InputStreamReader(setupStream));

        String line;
        while ((line = airportReader.readLine()) != null) {
            String[] tokens = line.replaceAll("\\s*,\\s*", ",").split(",");
            String gateType = tokens[0];
            if (Integer.parseInt(gateType) < 1 || Integer.parseInt(gateType) > 7) {
                controller.handleError("Invalid gate type");
                return;
            }
            for (int i = 1; i <= Integer.parseInt(tokens[1]); i++) {
                airport.getGateList().add(new Gate(tokens[3]+String.valueOf(i),
                        Integer.parseInt(tokens[2]), MAP_CATEGORIES.get(tokens[0])));
            }
        }

        // We assume that passenger flights use the passenger service and cargo flights
        // and cargo flights use the (un)loading service
        while ((line = setupReader.readLine()) != null) {
            String[] tokens = line.replaceAll("\\s*,\\s*", ",").split(",");
            String flightType, aircraftType;
            List<String> requestedServices = new ArrayList<>();
            switch(tokens[2]) {
                case "1":
                    flightType = "passenger";
                    requestedServices.add("passenger carrying");
                    break;
                case "2":
                    flightType = "cargo";
                    requestedServices.add("(un)loading");
                    break;
                case "3":
                    flightType = "private";
                    break;
                default:
                    controller.handleError("Invalid flight type number");
                    return;
            }

            switch(tokens[3]) {
                case "1":
                    aircraftType = "single-engine";
                    break;
                case "2":
                    aircraftType = "turboprop";
                    break;
                case "3":
                    aircraftType = "jet";
                    break;
                default:
                    controller.handleError("Invalid aircraft type number");
                    return;
            }
            airport.getFlightList().add(new Flight(tokens[0], tokens[1], flightType, aircraftType,
                    Integer.parseInt(tokens[4]), requestedServices, 0));
        }
    }

   /**
   * Processes a flight and assigns it to a gate if there is an available one.
   * @param gateList the list of gates that will be checked for availability
   * @param f the flight that will be processed
   * @param successStatus the new status of the flight if it is successfully assigned to a gate
   * @param failureStatus the new status of the flight if it is not successfully assigned to a gate
   * @return the gate's id if the assignment was successful or the string "Unavailable" if it wasn't
   */
    public static String service(List<Gate> gateList, Flight f, String successStatus,
            String failureStatus) {
        Gate assignedGate = null;
        for (Gate g : gateList) {
            if (g.getFlightsAllowed().get(f.getFlightType())
                    && g.getAircraftsAllowed().get(f.getAircraftType())
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
   * @param controller the controller to which the timer task will be added
   * @param f the flight that will be processed
   */
    public static void handleLanding(MainWindowController controller, Flight f) {
        int landingTime = 0;
        switch(f.getAircraftType()) {
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
        timeScheduler.getTimer().schedule(task, landingTime * timeScheduler.getMinuteDuration() * 1000l);
    }

   /**
   * Handles a flight's transition to "Parked" status and creates a timer task
   * that runs when the flight is ready for takeoff.
   *
   * We assume that minutesToTakeOff start counting down
   * after the flight has parked.
   *
   * The exact takeoff time is randomly selected. The minimum stay is 5 minutes
   * and the maximum is twice the minutesToTakeOff.
   * @param controller the controller to which the timer task will be added
   * @param f the flight that will be processed
   */
    public static void handleParked(MainWindowController controller, Flight f) {
        f.setStatus("Parked");

        int rand = Helper.generateRandom(5, 2*f.getMinutesToTakeOff());
        f.setLeavesOn(timeScheduler.getCurrentTime()+rand);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handleTakeOff(controller, f);
            }
        };
        timeScheduler.getTimer().schedule(task, rand * timeScheduler.getMinuteDuration() * 1000l);
        Platform.runLater(() -> controller.updateUI("Flight with id " + f.getId()
                + " completed its landing and is now parked in Gate " + f.getGate().getId() + "."));
    }

   /**
   * Handles a flight's takeoff calculating the revenue and assigning a flight to
   * the freed gate if there is a suitable flight in "Holding".
   * @param controller the controller to which the revenue will be added
   * @param f the flight that will be processed
   */
    public static void handleTakeOff(MainWindowController controller, Flight f) {
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
                    finalCost += cost * 0.02;
                    break;
                case "passenger carrying":
                    finalCost += cost * 0.02;
                    break;
                case "(un)loading":
                    finalCost += cost * 0.05;
            }
        }
        // Compare the actual take off time with the original projected value
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
                if (!service(airport.getGateList(), flight, "Landing", "Holding").equals("Unavailable")){
                    handleLanding(controller, flight);
                }
            }
        }

        final String flightPayed = Integer.toString(finalCost);
        Platform.runLater(() -> controller.updateUI("Flight with id " + f.getId() + " left and payed " + flightPayed + "."));
    }

   /**
   * Helper function that capitalizes the first letter of a word.
   * @param str input string
   * @return the flight that will be processed
   */
    public static String capitalize(String str) {
        if(str == null) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

   /**
   * Helper function that generates a random number in the range [min, max].
   * @param min minimum possible number
   * @param max maximum possible number
   * @return a random number between the two given arguments
   */
    public static int generateRandom(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}