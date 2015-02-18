package fi.cosky.sdk;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class UserUpdateRequest extends BaseData {
	public static final String MimeType = UserData.MimeType;
	public static final double MimeVersion = UserData.MimeVersion;
	
	private int VersionNumber;
	private int ClientId;
	private int UserId;
	
	private int ProblemLimit;
    private int OptimizationQueueLimit;
    private int VehicleLimit;
    private int TaskLimit;
    private int DepotLimit;
		
    public UserUpdateRequest() {
    	this.ProblemLimit = 0;
    	this.OptimizationQueueLimit = 0;
    	this.TaskLimit = 0;
    	this.DepotLimit = 0;
    	this.VehicleLimit = 0;
    }
    
	int getVersionNumber() {
		return VersionNumber;
	}
	void setVersionNumber(int versionNumber) {
		VersionNumber = versionNumber;
	}
	public int getClientId() {
		return ClientId;
	}
	public void setClientId(int clientId) {
		ClientId = clientId;
	}
	public int getUserId() {
		return UserId;
	}
	public void setUserId(int userId) {
		UserId = userId;
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
