package fi.cosky.sdk;

import java.util.ArrayList;

public class VehicleTypeData extends BaseData {
	public static final String MimeType = "application/vnd.jyu.nfleet.vehicletypes";
	public static final double MimeVersion = 2.0;
	
	private ArrayList<String> VehicleTypes;

	public ArrayList<String> getVehicleTypes() {
		return VehicleTypes;
	}

	public void setVehicleTypes(ArrayList<String> vehicleTypes) {
		VehicleTypes = vehicleTypes;
	}
}
