package fi.cosky.sdk;
import java.util.List;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class TaskEventData extends BaseData {
    private int Id;
    private Type Type;
    private String Info;
    private State State;
    private List<TimeWindowData> TimeWindows;
    private LocationData Location;
    private int ServiceTime;
    private List<CapacityData> Capacities;
        
    //Constructor uses only the required fields, others can be accessed via getters and setters
    public TaskEventData(Type type, LocationData location, List<CapacityData> capacities) {
        this.Type = type;
        this.Location = location;
        this.Capacities = capacities;
        this.ServiceTime = 0;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getInfo() {
		return Info;
	}

	public void setInfo(String info) {
		Info = info;
	}

	public Type getType() {
        return Type;
    }

    public void setType(Type type) {
        this.Type = type;
    }

    public State getState() {
        return State;
    }

    public void setState(State state) {
        State = state;
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

	public TaskEventUpdateRequest toRequest() {
		TaskEventUpdateRequest request = new TaskEventUpdateRequest(Type, Location, Capacities);
		request.setServiceTime(ServiceTime);
		request.setTaskEventId(Id);
		request.setTimeWindows(TimeWindows);
		return request;
	}
}
