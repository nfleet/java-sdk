package fi.cosky.sdk;
import java.util.ArrayList;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class VehicleData extends BaseData {
	public static final String MimeType = "application/vnd.jyu.nfleet.vehicle";
    public static final double MimeVersion = 2.2;
	
    private int Id;
    private String Name;
    private ArrayList<CapacityData> Capacities;
    private LocationData StartLocation;
    private LocationData EndLocation;
    private ArrayList<TimeWindowData> TimeWindows;
    private RouteData Route;
    private int VersionNumber;
    private String VehicleType;
    private String SpeedProfile;
    private double SpeedFactor;
    private String RelocationType;
    private String Info1;
    
    private String ActivityState;
    
	private double FixedCost;
    private double KilometerCost;
    private double HourCost;

    private CoordinateData CurrentLocation;


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
        this.RelocationType = "None";
        this.ActivityState = "Active"; //has the same possibilities as activitystate in taskdata: Active and Inactive
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

    public LocationData getStartLocation() {
        return StartLocation;
    }

    public void setStartLocation(LocationData startLocation) {
        StartLocation = startLocation;
    }

	int getVersionNumber() {
		return VersionNumber;
	}

	void setVersionNumber(int versionNumber) {
		VersionNumber = versionNumber;
	}
	
	public String getName() {
		return Name;
	}

	public ArrayList<CapacityData> getCapacities() {
		return Capacities;
	}

	public String getVehicleType() {
		return VehicleType;
	}

	public void setVehicleType(String vehicleType) {
		VehicleType = vehicleType;
	}

	public String getSpeedProfile() {
		return SpeedProfile;
	}

	public void setSpeedProfile(String vehicleSpeedProfile) {
		SpeedProfile = vehicleSpeedProfile;
	}

	public double getSpeedFactor() {
		return SpeedFactor;
	}

	public void setSpeedFactor(double vehicleSpeedFactor) {
		SpeedFactor = vehicleSpeedFactor;
	}

	public ArrayList<TimeWindowData> getTimeWindows() {
		return TimeWindows;
	}

	public double getFixedCost() {
		return FixedCost;
	}

	public void setFixedCost(double fixedCost) {
		FixedCost = fixedCost;
	}

	public double getKilometerCost() {
		return KilometerCost;
	}

	public void setKilometerCost(double kilometerCost) {
		KilometerCost = kilometerCost;
	}

	public double getHourCost() {
		return HourCost;
	}

	public void setHourCost(double hourCost) {
		HourCost = hourCost;
	}

	public String getRelocationType() {
		return RelocationType;
	}

	public void setRelocationType(String relocationType) {
		RelocationType = relocationType;
	}
	
    public String getActivityState() {
		return ActivityState;
	}

	public void setActivityState(String activityState) {
		ActivityState = activityState;
	}

	public VehicleUpdateRequest toRequest() {
		VehicleUpdateRequest request = new VehicleUpdateRequest(Name, Capacities, StartLocation, EndLocation);
		request.setVersionNumber(VersionNumber);
		request.setName(Name);
		request.setTimeWindows(TimeWindows);
		request.setRoute(Route);
		request.setVehicleSpeedFactor(SpeedFactor);
		request.setVehicleSpeedProfile(SpeedProfile);
		request.setRelocationType(RelocationType);
		request.setActivityState(ActivityState);
        request.setInfo1(Info1);
        request.setHourCost(HourCost);
        request.setFixedCost(FixedCost);
        request.setKilometerCost(KilometerCost);
        request.setVehicleType(VehicleType);
		return request;
	}

    public CoordinateData getCurrentLocation() {
        return CurrentLocation;
    }

    public void setCurrentLocation(CoordinateData currentLocation) {
        CurrentLocation = currentLocation;
    }

    public String getInfo1() {
        return Info1;
    }

    public void setInfo1(String info1) {
        Info1 = info1;
    }
}
