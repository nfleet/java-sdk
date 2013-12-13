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
	private boolean IsFeasible;
	private double WaitingTimeBefore;
	private Date ActualArrivalTime;
	private Date ActualDepartureTime;
		
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
	public boolean isIsFeasible() {
		return IsFeasible;
	}
	public void setIsFeasible(boolean isFeasible) {
		IsFeasible = isFeasible;
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
	
}
