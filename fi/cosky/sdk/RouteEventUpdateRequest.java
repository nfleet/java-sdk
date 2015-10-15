package fi.cosky.sdk;

import java.util.Date;

public class RouteEventUpdateRequest extends BaseData {
	public static final String MimeType = RouteEventData.MimeType;
	public static final double MimeVersion = RouteEventData.MimeVersion;
	
	private int eventSequenceNumber;
	private Date actualArrivalTime;
	private String state;
	
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
