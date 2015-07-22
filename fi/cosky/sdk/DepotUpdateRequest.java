package fi.cosky.sdk;


import java.util.ArrayList;

public class DepotUpdateRequest {
    public static final String MimeType = DepotData.MimeType;
    public static final double MimeVersion = DepotData.MimeVersion;

    private String Name;
    private String Info1;
    private String Type;
    private ArrayList<CapacityData> Capacities;
    private LocationData Location;
    private double StoppingTime;
    
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getInfo1() {
        return Info1;
    }

    public void setInfo1(String info1) {
        Info1 = info1;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public ArrayList<CapacityData> getCapacities() {
        return Capacities;
    }

    public void setCapacities(ArrayList<CapacityData> capacities) {
        Capacities = capacities;
    }

    public LocationData getLocation() {
        return Location;
    }

    public void setLocation(LocationData location) {
        Location = location;
    }

	public double getStoppingTime() {
		return StoppingTime;
	}

	public void setStoppingTime(double stoppingTime) {
		StoppingTime = stoppingTime;
	}
}
