package fi.cosky.sdk;
import java.util.ArrayList;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class ObjectiveValueDataSet extends BaseData {
	public static final String MimeType = "application/vnd.jyu.nfleet.objectivevalueset+json";
	public static final double MimeVersion = 2.0;
	
    private ArrayList<ObjectiveValueData> Items;

    public ArrayList<ObjectiveValueData> getItems() {
        return Items;
    }

    public void setItems(ArrayList<ObjectiveValueData> items) {
        Items = items;
    }
}
