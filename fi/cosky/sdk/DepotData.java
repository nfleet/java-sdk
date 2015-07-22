package fi.cosky.sdk;


import java.util.ArrayList;

public class DepotData extends BaseData {
    public static final String MimeType = "application/vnd.jyu.nfleet.depot";
    public static final double MimeVersion = 2.2;

    private int Id;
    private String Name;
    private String Info1;
    private String Type;
    private String DataSource;
    private ArrayList<CapacityData> Capacities;
    private LocationData Location;
    private int VersionNumber;
    private double StoppingTime;


    public DepotData() {

    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

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

    public String getDataSource() {
        return DataSource;
    }

    public void setDataSource(String dataSource) {
        DataSource = dataSource;
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

    public int getVersionNumber() {
        return VersionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        VersionNumber = versionNumber;
    }

	public double getStoppingTime() {
		return StoppingTime;
	}

	public void setStoppingTime(double stoppingTime) {
		StoppingTime = stoppingTime;
	}
}
