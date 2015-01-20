package fi.cosky.sdk;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class UserData extends BaseData {
	public static final String MimeType = "application/vnd.jyu.nfleet.user";
	public static final double MimeVersion = 2.1;
	
    private int Id;
    private int ClientId;
    private int VersionNumber;
    private int OptimizationCount;
    
    private int ProblemLimit;
    private int OptimizationQueueLimit;
    private int VehicleLimit;
    private int TaskLimit;
    private int DepotLimit;
        
    public int getId() {
        return Id;
    }
    
    public void setId(int id) {
        Id = id;
    }

	public int getClientId() {
		return ClientId;
	}

	public void setClientId(int clientId) {
		ClientId = clientId;
	}

	int getVersionNumber() {
		return VersionNumber;
	}

	void setVersionNumber(int versionNumber) {
		VersionNumber = versionNumber;
	}

	public int getOptimizationCount() {
		return OptimizationCount;
	}

	public void setOptimizationCount(int optimizationCount) {
		OptimizationCount = optimizationCount;
	}

	public int getProblemLimit() {
		return ProblemLimit;
	}

	public void setProblemLimit(int problemLimit) {
		ProblemLimit = problemLimit;
	}

	public int getOptimizationQueueLimit() {
		return OptimizationQueueLimit;
	}

	public void setOptimizationQueueLimit(int optimizationQueueLimit) {
		OptimizationQueueLimit = optimizationQueueLimit;
	}

	public int getVehicleLimit() {
		return VehicleLimit;
	}

	public void setVehicleLimit(int vehicleLimit) {
		VehicleLimit = vehicleLimit;
	}

	public int getTaskLimit() {
		return TaskLimit;
	}

	public void setTaskLimit(int taskLimit) {
		TaskLimit = taskLimit;
	}

	public int getDepotLimit() {
		return DepotLimit;
	}

	public void setDepotLimit(int depotLimit) {
		DepotLimit = depotLimit;
	}
}
