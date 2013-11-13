package fi.cosky.sdk;
import java.util.List;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class TaskDataSet extends BaseData {

    public List<TaskData> getItems() {
        return Items;
    }

    public void setItems(List<TaskData> items) {
        Items = items;
    }

    private List<TaskData> Items;

    public TaskDataSet(List<TaskData> tasks) {
        this.Items = tasks;
    }

}
