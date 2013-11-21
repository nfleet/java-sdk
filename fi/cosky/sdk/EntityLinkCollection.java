package fi.cosky.sdk;
import java.util.ArrayList;

public class EntityLinkCollection extends BaseData {
	private int VersionNumber;
	private ArrayList<EntityLink> Items;
	
	public int getVersionNumber() {
		return VersionNumber;
	}
	public void setVersionNumber(int versionNumber) {
		VersionNumber = versionNumber;
	}
	public ArrayList<EntityLink> getItems() {
		return Items;
	}
	public void setItems(ArrayList<EntityLink> items) {
		Items = items;
	}
}
