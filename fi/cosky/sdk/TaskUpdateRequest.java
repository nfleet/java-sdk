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
    private List<TaskEventUpdateRequest> TaskEvents;
    private int TaskId;
    private int VersionNumber;
    private int ClientId;
    private int UserId;
    private List<String> IncompatibleVehicleTypes;
    private List<String> CompatibleVehicleTypes;
    private double Profit;
    private String CanBeRelocated;
    private String ActivityState;
    
    
    public TaskUpdateRequest(List<TaskEventUpdateRequest> taskEvents) {
        this.TaskEvents = taskEvents;
    	this.CanBeRelocated = "None";
    }
       
    public TaskUpdateRequest() {
    	this.CanBeRelocated = "None";
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

	public String getCanBeRelocated() {
		return CanBeRelocated;
	}

	public void setCanBeRelocated(String canBeRelocated) {
		CanBeRelocated = canBeRelocated;
	}

	public String getActivityState() {
		return ActivityState;
	}

	public void setActivityState(String activityState) {
		ActivityState = activityState;
	}

	
}
