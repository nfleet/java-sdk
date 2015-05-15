package fi.cosky.sdk;
import java.util.ArrayList;
import java.util.List;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class TaskData extends BaseData {
	public static final String MimeType = "application/vnd.jyu.nfleet.task";
	public static final double MimeVersion = 2.1;
	
    private String Name;
    private String Info;
    private List<TaskEventData> TaskEvents;
    private int Id;
    private int VersionNumber;
    private List<String> IncompatibleVehicleTypes;
    private List<String> CompatibleVehicleTypes;
    
    //Profit will be removed at some point, use Priority.
    @Deprecated
    private double Profit;
    
    private double Priority;
    private String RelocationType;
    private String ActivityState;
    
    public TaskData() {
    	this.RelocationType = "None";
    	this.ActivityState = "Active"; // other possibility is "Inactive"
    	this.Priority = 0;
    }
    
    public String getInfo() {
        return Info;
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

    public List<TaskEventData> getTaskEvents() {
        return TaskEvents;
    }

    public void setTaskEvents(List<TaskEventData> taskEvents) {
        TaskEvents = taskEvents;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    int getVersionNumber() {
		return VersionNumber;
	}

	void setVersionNumber(int versionNumber) {
		VersionNumber = versionNumber;
	}

	public TaskData(List<TaskEventData> taskEvents) {
        this.TaskEvents = taskEvents;
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

	public TaskUpdateRequest toRequest() {
		List<TaskEventUpdateRequest> taskevents = new ArrayList<TaskEventUpdateRequest>();
		
		for( TaskEventData ted : this.getTaskEvents()) {
			taskevents.add(ted.toRequest());
		}
		
		TaskUpdateRequest update = new TaskUpdateRequest(taskevents);
		update.setInfo(Info);
		update.setName(Name);
		update.setVersionNumber(VersionNumber);
		update.setTaskId(Id);
		update.setRelocationType(RelocationType);
		update.setActivityState(ActivityState);
		return update;
	}
}
