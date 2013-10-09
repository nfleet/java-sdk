import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 9.10.2013
 * Time: 14:57
 * To change this template use File | Settings | File Templates.
 */
public class TaskEventDataSet extends BaseData {
    private ArrayList<TaskEventData> Items;

    public ArrayList<TaskEventData> getItems() {
        return Items;
    }

    public void setItems(ArrayList<TaskEventData> items) {
        Items = items;
    }
}
