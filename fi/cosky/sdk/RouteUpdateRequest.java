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
	private int[] Sequence;
	
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
		return Sequence;
	}
	public void setSequence(int[] sequence) {
		Sequence = sequence;
	}
	
}
