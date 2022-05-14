package multimedia.model;

public class ZoneCGate extends BaseGate{
    public ZoneCGate(String id, int cost, String category) {
        super(id, cost, category, 3* 60);

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
    }
}
