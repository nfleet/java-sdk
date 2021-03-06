package fi.cosky.sdk;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
import java.util.List;

public class ImportData extends BaseData {
	public static final String MimeType = "application/vnd.jyu.nfleet.import";
	public static final double MimeVersion = 2.2;
	
	private int VersionNumber;
	private int ErrorCount;
	private String State;
	private List<VehicleError> Vehicles;
	private List<TaskError> Tasks;
    private List<DepotError> Depots;
	
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
    public List<DepotError> getDepots() { return Depots; }
    public void setDepots(List<DepotError> depots) { Depots = depots; }
}
