package multimedia.model;

import java.util.*;

/**
 * Class that represents a flight object containing flight info.
 */
public class Flight {
    /**
     * Flight id
     */
    private final String id;
    /**
     * City that the aircraft flies to
     */
    private final String city;
    /**
     * Type of flight. One of "passenger", "cargo" or "private"
     */
    private final String flightType;
    /**
     * Type of aircraft. One of "single-engine", "turboprop" or "jet"
     */
    private final String aircraftType;
    /**
     * Remaining minutes before the flight takes off
     */
    private final int minutesToTakeOff;
    /**
     * List of additional services requested by the flight. Available service are "refuel", "cleaning",
     * "passenger carrying" and "(un)loading"
     */
    private final List<String> requestedServices;
    /**
     * Timestamp when the flight was submitted
     */
    private final int requestTimeStamp;
    /**
     * Current flight status. One "Parked", "Holding" or "Landing"
     */
    private String status;
    /**
     * Assigned gate to the flight
     */
    private BaseGate gate;
    /**
     * Actual timestamp when the flight will leave. This may differ from the requestTimeStamp + minutesToTakeOff
     * because the actual takeoff time is randomly selected within a range of requestTimeStamp + [5, 2 * minutesToTakeOff]
     */
    private int leavesOn;


    public Flight(String id, String city, String flightType, String aircraftType,
                  int minutesToTakeOff, List<String> requestedServices, int requestTimeStamp) {
        this.id = id;
        this.city = city;
        this.flightType = flightType;
        this.aircraftType = aircraftType;
        this.minutesToTakeOff = minutesToTakeOff;
        this.requestedServices = requestedServices;
        this.requestTimeStamp = requestTimeStamp;
        this.leavesOn = -1;
    }

    public String getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getFlightType() {
        return flightType;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public int getMinutesToTakeOff() {
        return minutesToTakeOff;
    }

    public List<String> getRequestedServices() {
        return requestedServices;
    }

    public int getRequestTimeStamp() {
        return requestTimeStamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLeavesOn() {
        return leavesOn;
    }

    public void setLeavesOn(int leavesOn) {
        this.leavesOn = leavesOn;
    }

    public BaseGate getGate() {
        return gate;
    }

    public void setGate(BaseGate gate) {
        this.gate = gate;
    }
}