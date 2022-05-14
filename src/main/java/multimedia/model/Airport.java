package multimedia.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
 * Singleton class
 */
public class Airport {
    private int income;
    private final ObservableList<Flight> flightList;
    private final ObservableList<Gate> gateList;
    private static final Airport airport = new Airport();

    private Airport() {
        income = 0;
        flightList = FXCollections.observableArrayList();
        gateList = FXCollections.observableArrayList();
    }

    public static Airport getInstance( ) { return airport; }
    public int getIncome() { return income; }
    public void setIncome(int income) { this.income = income; }
    public ObservableList<Flight> getFlightList() { return flightList; }
    public ObservableList<Gate> getGateList() { return gateList; }
}
