import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 26.3.2013
 * Time: 15:11
 * To change this template use File | Settings | File Templates.
 */
public class TaskDataSet extends BaseData {

    public List<TaskData> getItems() {
        return Items;
    }

    public void setItems(List<TaskData> items) {
        Items = items;
    }

    private List<TaskData> Items;

    public TaskDataSet(List<TaskData> tasks) {
        this.Items = tasks;
    }

}
