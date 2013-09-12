import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 12.9.2013
 * Time: 12:10
 * To change this template use File | Settings | File Templates.
 */
public class TaskUpdateRequest extends BaseData {
    private String Name;
    private String Info;
    private List<TaskEventUpdateRequest> TaskEvents;
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

    public List<TaskEventUpdateRequest> getTaskEvents() {
        return TaskEvents;
    }

    public void setTaskEvents(List<TaskEventUpdateRequest> taskEvents) {
        TaskEvents = taskEvents;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public TaskUpdateRequest(List<TaskEventUpdateRequest> taskEvents) {
        this.TaskEvents = taskEvents;
    }
}
