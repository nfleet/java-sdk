package fi.cosky.sdk;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
public class ImportRequest {
	public static final String MimeType = ImportData.MimeType;
	public static final double MimeVersion = ImportData.MimeVersion;
	
	private VehicleSetImportRequest Vehicles;
	private TaskSetImportRequest Tasks;
	private DepotSetImportRequest Depots;
	
	public VehicleSetImportRequest getVehicles() {
		return Vehicles;
	}
	public void setVehicles(VehicleSetImportRequest vehicles) {
		Vehicles = vehicles;
	}
	public TaskSetImportRequest getTasks() {
		return Tasks;
	}
	public void setTasks(TaskSetImportRequest tasks) {
		Tasks = tasks;
	}
	public DepotSetImportRequest getDepots() {
		return Depots;
	}
	public void setDepots(DepotSetImportRequest depots) {
		Depots = depots;
	}
}
