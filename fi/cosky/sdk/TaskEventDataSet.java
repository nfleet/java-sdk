package fi.cosky.sdk;
import java.util.ArrayList;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class TaskEventDataSet extends BaseData {
    private ArrayList<TaskEventData> Items;

    public ArrayList<TaskEventData> getItems() {
        return Items;
    }

    public void setItems(ArrayList<TaskEventData> items) {
        Items = items;
    }
}
