package fi.cosky.sdk;

public class ImportRequest {
	private VehicleSetImportRequest Vehicles;
	private TaskSetImportRequest Tasks;
	
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
}
