package multimedia.util;

import multimedia.exceptions.InvalidInputException;
import multimedia.model.Airport;
import multimedia.model.Flight;
import multimedia.model.Gate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Initializer {
    private final Airport airport;
    private final Map<String, String> MAP_CATEGORIES;
    private static final Initializer initializer = new Initializer();

    private Initializer() {
        airport = Airport.getInstance();
        // Map between input file gate numbers and a meaningful gate name
        MAP_CATEGORIES = new HashMap<>();
        MAP_CATEGORIES.put("1", "Gate");
        MAP_CATEGORIES.put("2", "Cargo Gate");
        MAP_CATEGORIES.put("3", "Zone A");
        MAP_CATEGORIES.put("4", "Zone B");
        MAP_CATEGORIES.put("5", "Zone C");
        MAP_CATEGORIES.put("6", "General Parking");
        MAP_CATEGORIES.put("7", "Long Stay");
    }

    public static Initializer getInstance() {
        return initializer;
    }

    /**
     * Reads the input files.
     *
     * @param airportURL the URL of the airport initialization file that defines the gates
     * @param setupURL   the URL of the flight initialization files that defines incoming flights
     * @throws IOException           is thrown if opening a Stream/Reader fails
     * @throws InvalidInputException is thrown if the input data is not valid
     */
    public void readInput(URL airportURL, URL setupURL) throws IOException, InvalidInputException {
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
                throw new InvalidInputException("Invalid gate type");
            }
            for (int i = 1; i <= Integer.parseInt(tokens[1]); i++) {
                airport.getGateList().add(new Gate(tokens[3] + i,
                        Integer.parseInt(tokens[2]), MAP_CATEGORIES.get(tokens[0])));
            }
        }

        // We assume that passenger flights use the passenger service and cargo flights
        // and cargo flights use the (un)loading service
        while ((line = setupReader.readLine()) != null) {
            String[] tokens = line.replaceAll("\\s*,\\s*", ",").split(",");
            String flightType, aircraftType;
            List<String> requestedServices = new ArrayList<>();
            switch (tokens[2]) {
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
                    throw new InvalidInputException("Invalid flight type number");
            }

            switch (tokens[3]) {
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
                    throw new InvalidInputException("Invalid aircraft type number");
            }
            airport.getFlightList().add(new Flight(tokens[0], tokens[1], flightType, aircraftType,
                    Integer.parseInt(tokens[4]), requestedServices, 0));
        }
    }
}
