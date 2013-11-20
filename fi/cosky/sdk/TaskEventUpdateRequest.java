package fi.cosky.sdk;
import java.util.List;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class TaskEventUpdateRequest extends BaseData {
	private int TaskEventId;
    private Type Type;
    private List<TimeWindowData> TimeWindows;
    private LocationData Location;
    private int ServiceTime;
    private List<CapacityData> Capacities;
    

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

    public List<CapacityData> getCapacities() {
        return Capacities;
    }

    public void setCapacities(List<CapacityData> capacities) {
        Capacities = capacities;
    }
}
