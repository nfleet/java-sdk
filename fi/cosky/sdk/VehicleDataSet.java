package fi.cosky.sdk;
import java.util.ArrayList;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class VehicleDataSet extends BaseData {
	public static final String MimeType = "application/vnd.jyu.nfleet.vehicleset";
	public static final double MimeVersion = 2.1;
	
    private ArrayList<VehicleData> Items;
    private int VersionNumber;
    
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

	int getVersionNumber() {
		return VersionNumber;
	}

	void setVersionNumber(int versionNumber) {
		VersionNumber = versionNumber;
	}

}
