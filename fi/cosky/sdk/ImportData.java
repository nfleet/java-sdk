package fi.cosky.sdk;

import java.util.List;

public class ImportData extends BaseData {
	private int VersionNumber;
	private int ErrorCount;
	private String State;
	private List<VehicleError> Vehicles;
	private List<TaskError> Tasks;
	
	public int getVersionNumber() {
		return VersionNumber;
	}
	public void setVersionNumber(int versionNumber) {
		VersionNumber = versionNumber;
	}
	public int getErrorCount() {
		return ErrorCount;
	}
	public void setErrorCount(int errorCount) {
		ErrorCount = errorCount;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public List<VehicleError> getVehicles() {
		return Vehicles;
	}
	public void setVehicles(List<VehicleError> vehicles) {
		Vehicles = vehicles;
	}
	public List<TaskError> getTasks() {
		return Tasks;
	}
	public void setTasks(List<TaskError> tasks) {
		Tasks = tasks;
	}

}
