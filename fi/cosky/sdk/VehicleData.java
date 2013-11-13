package fi.cosky.sdk;
import java.util.ArrayList;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class VehicleData extends BaseData {
    private int Id;
    private String Name;
    private ArrayList<CapacityData> Capacities;
    private LocationData StartLocation;
    private LocationData EndLocation;
    private ArrayList<TimeWindowData> TimeWindows;
    private RouteData Route;

    public RouteData getRoute() {
        return Route;
    }

    public void setRoute(RouteData route) {
        Route = route;
    }

    public VehicleData(String name, ArrayList<CapacityData> capacities, LocationData startLoc, LocationData endLoc){
        this.Capacities = capacities;
        this.EndLocation = endLoc;
        this.StartLocation = startLoc;
        this.Name = name;
    }

    public int getId() {
        return this.Id;
    }

    public void setCapacities(ArrayList<CapacityData> capa) {
        this.Capacities = capa;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setTimeWindows(ArrayList<TimeWindowData> timeWindows) {
        this.TimeWindows = timeWindows;
    }


    public LocationData getEndLocation() {
        return EndLocation;
    }

    public void setEndLocation(LocationData endLocation) {
        EndLocation = endLocation;
    }

    @Override
    public String toString() {
        return "VehicleData{" +
                "Id=" + Id +
                ", Name='" + Name + '\'' +
                ", Capacities=" + Capacities +
                ", StartLocation=" + StartLocation +
                ", EndLocation=" + EndLocation +
                ", TimeWindows=" + TimeWindows +
                ", Route=" + Route +
                '}' + super.toString();
    }

    public LocationData getStartLocation() {
        return StartLocation;
    }

    public void setStartLocation(LocationData startLocation) {
        StartLocation = startLocation;
    }
}
