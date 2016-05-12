package fi.cosky.sdk;
import java.util.List;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class TaskUpdateRequest extends BaseData {
	public static final String MimeType = TaskData.MimeType;
	public static final double MimeVersion = TaskData.MimeVersion;
	
    private String Name;
    private String Info;
    private String Info2;
    private String Info3;
    private String Info4;
    private List<TaskEventUpdateRequest> TaskEvents;
    private int TaskId;
    private int VersionNumber;
    private int ClientId;
    private int UserId;
    private List<String> IncompatibleVehicleTypes;
    private List<String> CompatibleVehicleTypes;
	private Boolean IsLockedToVehicle;
    
    @Deprecated
    private double Profit;
    
    private double Priority;
    private String RelocationType;
    private String ActivityState;
    
    
    public TaskUpdateRequest(List<TaskEventUpdateRequest> taskEvents) {
        this.TaskEvents = taskEvents;
    	this.RelocationType = "None";
    	this.ActivityState = "Active";
    }
       
    public TaskUpdateRequest() {
    	this.RelocationType = "None";
    }
    
    public String getInfo() {
        return Info;
    }

    public int getTaskId() {
		return TaskId;
	}

	public void setTaskId(int taskId) {
		TaskId = taskId;
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

	public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public String getInfo2() {
		return Info2;
	}

	public void setInfo2(String info2) {
		Info2 = info2;
	}

	public String getInfo3() {
		return Info3;
	}

	public void setInfo3(String info3) {
		Info3 = info3;
	}

	public String getInfo4() {
		return Info4;
	}

	public void setInfo4(String info4) {
		Info4 = info4;
	}

	public List<String> getIncompatibleVehicleTypes() {
		return IncompatibleVehicleTypes;
	}

	public void setIncompatibleVehicleTypes(List<String> incompatibleVehicleTypes) {
		IncompatibleVehicleTypes = incompatibleVehicleTypes;
	}

	public List<String> getCompatibleVehicleTypes() {
		return CompatibleVehicleTypes;
	}

	public void setCompatibleVehicleTypes(List<String> compatibleVehicleTypes) {
		CompatibleVehicleTypes = compatibleVehicleTypes;
	}

	public List<TaskEventUpdateRequest> getTaskEvents() {
        return TaskEvents;
    }

    public void setTaskEvents(List<TaskEventUpdateRequest> taskEvents) {
        TaskEvents = taskEvents;
    }

    public double getProfit() {
		return Profit;
	}

	public void setProfit(double profit) {
		Profit = profit;
	}

	public String getActivityState() {
		return ActivityState;
	}

	public void setActivityState(String activityState) {
		ActivityState = activityState;
	}

	public double getPriority() {
		return Priority;
	}

	public void setPriority(double priority) {
		Priority = priority;
	}

	public String getRelocationType() {
		return RelocationType;
	}

	public void setRelocationType(String relocationType) {
		RelocationType = relocationType;
	}

	public Boolean getIsLockedToVehicle() {
		return IsLockedToVehicle;
	}

	public void setIsLockedToVehicle(Boolean isLockedToVehicle) {
		IsLockedToVehicle = isLockedToVehicle;
	}
	
}
