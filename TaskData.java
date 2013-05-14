import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 26.3.2013
 * Time: 15:11
 * To change this template use File | Settings | File Templates.
 */
public class TaskData extends BaseData {
    private String Name;
    private String Info;
    private List<TaskEventData> TaskEvents;
    private int Id;

    public String getInfo() {
        return Info;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public List<TaskEventData> getTaskEvents() {
        return TaskEvents;
    }

    public void setTaskEvents(List<TaskEventData> taskEvents) {
        TaskEvents = taskEvents;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public TaskData(List<TaskEventData> taskEvents) {
        this.TaskEvents = taskEvents;
    }
}
