package fi.cosky.sdk;
import java.util.List;
import java.util.Date;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class TaskEventUpdateRequest extends BaseData {
	private int TaskEventId;
    private Type Type;
    private String Name;
    private List<TimeWindowData> TimeWindows;
    private LocationData Location;
    private int ServiceTime;
    private int StoppingTime;
    private List<CapacityData> Capacities;
    private String VehicleId;
    private String Style;
    private int SequenceNumber;
    private boolean IsLocked;
    private Date PresetArrivalTime;

    //Constructor uses only the required fields, others can be accessed via getters and setters
    public TaskEventUpdateRequest(Type type, LocationData location, List<CapacityData> capacities) {
        this.Type = type;
        this.Location = location;
        this.Capacities = capacities;
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type type) {
        this.Type = type;
    }
    
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }
    
    public int getTaskEventId() {
		return TaskEventId;
	}

	public void setTaskEventId(int taskEventId) {
		TaskEventId = taskEventId;
	}

	public List<TimeWindowData> getTimeWindows() {
        return TimeWindows;
    }

    public void setTimeWindows(List<TimeWindowData> timeWindows) {
        TimeWindows = timeWindows;
    }

    public LocationData getLocation() {
        return Location;
    }

    public void setLocation(LocationData location) {
        Location = location;
    }

    public int getServiceTime() {
        return ServiceTime;
    }

    public void setServiceTime(int serviceTime) {
        ServiceTime = serviceTime;
    }

    public int getStoppingTime() {
		return StoppingTime;
	}

	public void setStoppingTime(int stoppingTime) {
		StoppingTime = stoppingTime;
	}

	public List<CapacityData> getCapacities() {
        return Capacities;
    }

    public void setCapacities(List<CapacityData> capacities) {
        Capacities = capacities;
    }
    
    public void setVehicleId(String vehicleId) {
        VehicleId = vehicleId;
    }
    
    public String getVehicleId() {
        return VehicleId;
    }

    public void setStyle(String style) {
        Style = style;
    }
    
    public String getStyle() {
        return Style;
    }
    
    public void setSequenceNumber(int sequenceNumber) {
        SequenceNumber = sequenceNumber;
    }
    
    public int getSequenceNumber() {
        return SequenceNumber;
    }
     
    public void setIsLocked(boolean isLocked) {
        IsLocked = isLocked;
    }
    
    public boolean getIsLocked() {
        return IsLocked;
    }
    
    public Date getPresetArrivalTime() {
        return PresetArrivalTime;
    }
    
    public void setPresetArrivalTime(Date presetArrivalTime) {
        PresetArrivalTime = presetArrivalTime;
    }
}
