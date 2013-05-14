import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 11.4.2013
 * Time: 8:50
 * To change this template use File | Settings | File Templates.
 */
public class VehicleDataResult extends BaseData {
    private int Id;
    private String Name;
    private ArrayList<CapacityData> Capacities;
    private int StartLocationId;

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

    public ArrayList<CapacityData> getCapacities() {
        return Capacities;
    }

    public void setCapacities(ArrayList<CapacityData> capacities) {
        Capacities = capacities;
    }

    public int getStartLocationId() {
        return StartLocationId;
    }

    public void setStartLocationId(int startLocationId) {
        StartLocationId = startLocationId;
    }

    public int getEndLocationId() {
        return EndLocationId;
    }

    public void setEndLocationId(int endLocationId) {
        EndLocationId = endLocationId;
    }

    public ArrayList<TimeWindowData> getTimeWindows() {
        return TimeWindows;
    }

    public void setTimeWindows(ArrayList<TimeWindowData> timeWindows) {
        TimeWindows = timeWindows;
    }

    public ArrayList<Integer> getTaskEventSequence() {
        return TaskEventSequence;
    }

    public void setTaskEventSequence(ArrayList<Integer> taskEventSequence) {
        TaskEventSequence = taskEventSequence;
    }

    private int EndLocationId;
    private ArrayList<TimeWindowData> TimeWindows;
    private ArrayList<Integer> TaskEventSequence;

}
