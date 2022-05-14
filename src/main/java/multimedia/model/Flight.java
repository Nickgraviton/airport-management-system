package multimedia.model;

import java.util.*;

public class Flight {
    private final String id;
    private final String city;
    private final String flightType;
    private final String aircraftType;
    private final int minutesToTakeOff;
    private final List<String> requestedServices;
    private final int requestTimeStamp;
    private String status;
    private Gate gate;
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

    public Gate getGate() {
        return gate;
    }

    public void setGate(Gate gate) {
        this.gate = gate;
    }
}