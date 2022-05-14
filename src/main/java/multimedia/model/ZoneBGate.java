package multimedia.model;

public class ZoneBGate extends BaseGate{
    public ZoneBGate(String id, int cost, String category) {
        super(id, cost, category, 60);

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
    }
}
