import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 26.3.2013
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */
public class TaskEventData extends GeneralDataType{
    private int Id;
    private Type Type;
    private State State;
    private List<TimeWindowData> TimeWindows;
    private LocationData Location;
    private int ServiceTime;
    private List<CapacityData> Capacities;
    private Date ActualArrivalTime;
    private Date ActualDepartureTime;
    private Date PlannedArrivalTime;
    private Date PlannedDepartureTime;
    private int SequenceNumber;
    private int Parent;
    private boolean IsLocked;

    //Constructor uses only the required fields, others can be accessed via getters and setters
    public TaskEventData(Type type, LocationData location, List<CapacityData> capacities) {
        this.Type = type;
        this.Location = location;
        this.Capacities = capacities;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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

    public Date getActualArrivalTime() {
        return ActualArrivalTime;
    }

    public void setActualArrivalTime(Date actualArrivalTime) {
        ActualArrivalTime = actualArrivalTime;
    }

    public Date getActualDepartureTime() {
        return ActualDepartureTime;
    }

    public void setActualDepartureTime(Date actualDepartureTime) {
        ActualDepartureTime = actualDepartureTime;
    }

    public Date getPlannedArrivalTime() {
        return PlannedArrivalTime;
    }

    public void setPlannedArrivalTime(Date plannedArrivalTime) {
        PlannedArrivalTime = plannedArrivalTime;
    }

    public Date getPlannedDepartureTime() {
        return PlannedDepartureTime;
    }

    public void setPlannedDepartureTime(Date plannedDepartureTime) {
        PlannedDepartureTime = plannedDepartureTime;
    }

    public int getSequenceNumber() {
        return SequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        SequenceNumber = sequenceNumber;
    }

    public int getParent() {
        return Parent;
    }

    public void setParent(int parent) {
        Parent = parent;
    }

    public boolean isIsLocked() {
        return IsLocked;
    }

    public void setIsLocked(boolean isLocked) {
        IsLocked = isLocked;
    }
}

