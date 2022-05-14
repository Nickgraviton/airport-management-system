package multimedia.model;

public class LongStayGate extends BaseGate{
    public LongStayGate(String id, int cost, String category) {
        super(id, cost, category, 10 * 60);

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
    }
}
