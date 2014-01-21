package fi.cosky.sdk;

import java.util.List;

public class TaskError {
	private TaskData Task;
	private List<ErrorData> Errors;
	
	public TaskData getTask() {
		return Task;
	}
	public void setTask(TaskData task) {
		Task = task;
	}
	public List<ErrorData> getErrors() {
		return Errors;
	}
	public void setErrors(List<ErrorData> errors) {
		Errors = errors;
	}
}
