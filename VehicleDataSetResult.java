import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 11.4.2013
 * Time: 8:49
 * To change this template use File | Settings | File Templates.
 */
public class VehicleDataSetResult extends GeneralDataType {
    private ArrayList<VehicleDataResult> Items;

    public ArrayList<VehicleDataResult> getItems() {
        return Items;
    }

    public void setItems(ArrayList<VehicleDataResult> items) {
        Items = items;
    }
}
