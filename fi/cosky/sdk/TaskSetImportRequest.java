package fi.cosky.sdk;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */


import java.util.List;

public class TaskSetImportRequest {
	private List<TaskUpdateRequest> Items;

	public List<TaskUpdateRequest> getItems() {
		return Items;
	}

	public void setItems(List<TaskUpdateRequest> items) {
		Items = items;
	}
}
