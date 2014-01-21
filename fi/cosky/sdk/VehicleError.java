package fi.cosky.sdk;

import java.util.List;

public class VehicleError {
	private VehicleData Vehicle;
	private List<ErrorData> Errors;
	
	public VehicleData getVehicle() {
		return Vehicle;
	}
	public void setVehicle(VehicleData vehicle) {
		Vehicle = vehicle;
	}
	public List<ErrorData> getErrors() {
		return Errors;
	}
	public void setErrors(List<ErrorData> errors) {
		Errors = errors;
	}
}
