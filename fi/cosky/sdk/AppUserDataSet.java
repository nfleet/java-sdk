package fi.cosky.sdk;

import java.util.List;

public class AppUserDataSet extends BaseData {
	public List<AppUserData> Items;

	public List<AppUserData> getItems() {
		return Items;
	}

	public void setItems(List<AppUserData> items) {
		Items = items;
	}
}
