package fi.cosky.sdk;
import java.util.List;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class TaskUpdateRequest extends BaseData {
	
    private String Name;
    private String Info;
    private List<TaskEventUpdateRequest> TaskEvents;
    private int TaskId;
    private int VersionNumber;
    private int ClientId;
    private int UserId;
    
    public String getInfo() {
        return Info;
    }

    public int getTaskId() {
		return TaskId;
	}

	public void setTaskId(int taskId) {
		TaskId = taskId;
	}

	public int getVersionNumber() {
		return VersionNumber;
	}

	public void setVersionNumber(int versionNumber) {
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

    public List<TaskEventUpdateRequest> getTaskEvents() {
        return TaskEvents;
    }

    public void setTaskEvents(List<TaskEventUpdateRequest> taskEvents) {
        TaskEvents = taskEvents;
    }


    public TaskUpdateRequest(List<TaskEventUpdateRequest> taskEvents) {
        this.TaskEvents = taskEvents;
    }
}
