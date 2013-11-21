package fi.cosky.sdk;
import java.util.ArrayList;


public class RouteEventDataSet extends BaseData {
	private int VersionNumber;
	private ArrayList<RouteEventData> Items;
	
	int getVersionNumber() {
		return VersionNumber;
	}
	void setVersionNumber(int versionNumber) {
		VersionNumber = versionNumber;
	}
	public ArrayList<RouteEventData> getItems() {
		return Items;
	}
	public void setItems(ArrayList<RouteEventData> items) {
		Items = items;
	}
	
	
}
