package fi.cosky.sdk;

import java.util.Date;

public class RouteEventUpdateRequest extends BaseData {
	public static final String MimeType = "application/vnd.jyu.nfleet.route+json";
	public static final double MimeVersion = 2.0;
	
	private int clientId;
	private int userId;
	private int problemId;
	private int vehicleId;
	private int eventSequenceNumber;
	private Date actualArrivalTime;
	private String state;
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getProblemId() {
		return problemId;
	}
	public void setProblemId(int problemId) {
		this.problemId = problemId;
	}
	public int getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}
	public int getEventSequenceNumber() {
		return eventSequenceNumber;
	}
	public void setEventSequenceNumber(int eventSequenceNumber) {
		this.eventSequenceNumber = eventSequenceNumber;
	}
	public Date getActualArrivalTime() {
		return actualArrivalTime;
	}
	public void setActualArrivalTime(Date actualArrivalTime) {
		this.actualArrivalTime = actualArrivalTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
