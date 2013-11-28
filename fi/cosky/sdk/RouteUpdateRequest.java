package fi.cosky.sdk;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class RouteUpdateRequest extends BaseData {
	private int ClientId;
	private int UserId;
	private int ProblemId;
	private int VehicleId;
	private int[] Items;
	private int VersionNumber;
	
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
	public int getProblemId() {
		return ProblemId;
	}
	public void setProblemId(int problemId) {
		ProblemId = problemId;
	}
	public int getVehicleId() {
		return VehicleId;
	}
	public void setVehicleId(int vehicleId) {
		VehicleId = vehicleId;
	}
	public int[] getSequence() {
		return Items;
	}
	public void setSequence(int[] sequence) {
		Items = sequence;
	}
	public int[] getItems() {
		return Items;
	}
	public void setItems(int[] items) {
		Items = items;
	}
	int getVersionNumber() {
		return VersionNumber;
	}
	void setVersionNumber(int versionNumber) {
		VersionNumber = versionNumber;
	}
	
}
