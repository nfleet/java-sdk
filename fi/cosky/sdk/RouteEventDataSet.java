package fi.cosky.sdk;
import java.util.ArrayList;


public class RouteEventDataSet extends BaseData {
	public static final String MimeType = "application/vnd.jyu.nfleet.routeeventset";
	public static final double MimeVersion = 2.0; 
	
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
