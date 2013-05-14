import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 21.3.2013
 * Time: 12:06
 * To change this template use File | Settings | File Templates.
 */
public class VehicleDataSet extends BaseData {
    private ArrayList<VehicleData> Items;

    public VehicleDataSet(ArrayList<VehicleData> vehicles) {
        this.Items = vehicles;
    }

    public ArrayList<VehicleData> getItems() {
        return Items;
    }

    public void setItems(ArrayList<VehicleData> items) {
        Items = items;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (Items == null) {
            sb.append("Items=null\n");
            sb.append(super.toString());
            return sb.toString();
        }

        for (VehicleData d :Items) {
            sb.append(d+"\n");
        }
        sb.append(super.toString()+"\n");
        return sb.toString();
    }

    public boolean addVehicle(VehicleData dto) {
        return this.Items.add(dto);

    }

}
