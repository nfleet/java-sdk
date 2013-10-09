import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 7.10.2013
 * Time: 10:22
 * To change this template use File | Settings | File Templates.
 */
public class UserDataSet extends BaseData {
    private ArrayList<UserData> Items;

    public ArrayList<UserData> getItems() {
        return Items;
    }

    public void setItems(ArrayList<UserData> items) {
        Items = items;
    }
}
