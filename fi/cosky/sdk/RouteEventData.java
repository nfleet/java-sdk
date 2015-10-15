package fi.cosky.sdk;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

import java.util.Date;
import java.util.List;

public class RouteEventData extends BaseData {
	public static final String MimeType =  "application/vnd.jyu.nfleet.routeevent";
	public static final double MimeVersion = 2.0;
	
	private int TaskEventId;
	private int VersionNumber;
	private String DataState;
	private String FeasibilityState;
	private double WaitingTimeBefore;
	private Date ArrivalTime;
	private Date DepartureTime;
	private String State;
	private KPIData KPIs;
	private List<Float> Geometry;
	private String Type;
	private int SequenceNumber;
	private int TaskId;
    private String LockState;
    private String TimeState;


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
	public Date getArrivalTime() {
		return ArrivalTime;
	}
	public void setArrivalTime(Date arrivalTime) {
		ArrivalTime = arrivalTime;
	}
	public Date getDepartureTime() {
		return DepartureTime;
	}
	public void setDepartureTime(Date departureTime) {
		DepartureTime = departureTime;
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

	public String getFeasibilityState() {
		return FeasibilityState;
	}

	public void setFeasibilityState(String feasibilityState) {
		FeasibilityState = feasibilityState;
	}

	public KPIData getKPIs() {
		return KPIs;
	}

	public void setKPIs(KPIData kPIs) {
		KPIs = kPIs;
	}

	public List<Float> getGeometry() {
		return Geometry;
	}

	public void setGeometry(List<Float> geometry) {
		Geometry = geometry;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public int getSequenceNumber() {
		return SequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		SequenceNumber = sequenceNumber;
	}

	public int getTaskId() {
		return TaskId;
	}

	public void setTaskId(int taskId) {
		TaskId = taskId;
	}

    public String getTimeState() {
        return TimeState;
    }

    public void setTimeState(String timeState) {
        TimeState = timeState;
    }

    public String getLockState() {
        return LockState;
    }

    public void setLockState(String lockState) {
        LockState = lockState;
    }
}
