package fi.cosky.sdk;
import java.util.ArrayList;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class ObjectiveValueDataSet extends BaseData {
    private ArrayList<ObjectiveValueData> Items;

    public ArrayList<ObjectiveValueData> getItems() {
        return Items;
    }

    public void setItems(ArrayList<ObjectiveValueData> items) {
        Items = items;
    }
}
