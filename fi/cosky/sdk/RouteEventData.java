package fi.cosky.sdk;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

import java.util.Date;

public class RouteEventData extends BaseData {
	private int TaskEventId;
	private int VersionNumber;
	private String DataState;
	private String FeasibilityState;
	private double WaitingTimeBefore;
	private Date ActualArrivalTime;
	private Date ActualDepartureTime;
	private String State;
		
	int getVersionNumber() {
		return VersionNumber;
	}
	
	void setVersionNumber(int versionNumber) {
		VersionNumber = versionNumber;
	}
	
	public String getDataState() {
		return DataState;
	}
	public void setDataState(String dataState) {
		DataState = dataState;
	}
	public String isFeasible() {
		return FeasibilityState;
	}
	public void setIsFeasible(String isFeasible) {
		FeasibilityState = isFeasible;
	}
	public double getWaitingTimeBefore() {
		return WaitingTimeBefore;
	}
	public void setWaitingTimeBefore(double waitingTimeBefore) {
		WaitingTimeBefore = waitingTimeBefore;
	}
	public Date getActualArrivalTime() {
		return ActualArrivalTime;
	}
	public void setActualArrivalTime(Date actualArrivalTime) {
		ActualArrivalTime = actualArrivalTime;
	}
	public Date getActualDepartureTime() {
		return ActualDepartureTime;
	}
	public void setActualDepartureTime(Date actualDepartureTime) {
		ActualDepartureTime = actualDepartureTime;
	}

	public int getTaskEventId() {
		return TaskEventId;
	}

	public void setTaskEventId(int taskEventId) {
		TaskEventId = taskEventId;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}
	
}
