package multimedia.model;

public class GeneralParkingGate extends BaseGate{
    public GeneralParkingGate(String id, int cost, String category) {
        super(id, cost, category, 4 * 60);

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
    }
}
