package fi.cosky.sdk;
import java.util.List;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class TaskData extends BaseData {
    private String Name;
    private String Info;
    private List<TaskEventData> TaskEvents;
    private int Id;
    private int VersionNumber;
    
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

    public int getVersionNumber() {
		return VersionNumber;
	}

	public void setVersionNumber(int versionNumber) {
		VersionNumber = versionNumber;
	}

	public TaskData(List<TaskEventData> taskEvents) {
        this.TaskEvents = taskEvents;
    }
}
