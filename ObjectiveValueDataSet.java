import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 7.10.2013
 * Time: 15:45
 * To change this template use File | Settings | File Templates.
 */
public class ObjectiveValueDataSet extends BaseData {
    private ArrayList<ObjectiveValueData> Items;

    public ArrayList<ObjectiveValueData> getItems() {
        return Items;
    }

    public void setItems(ArrayList<ObjectiveValueData> items) {
        Items = items;
    }
}
