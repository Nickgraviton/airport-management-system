package multimedia.model;

public class Gate extends BaseGate{
    public Gate(String id, int cost, String category) {
        super(id, cost, category, 45);

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
    }
}