package fi.cosky.sdk;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class RouteData extends BaseData {
    private int[] Items;
    private int VersionNumber;

    public int[] getItems() {
        return Items;
    }

    public void setItems(int[] items) {
        Items = items;
    }

	int getVersionNumber() {
		return VersionNumber;
	}

	void setVersionNumber(int versionNumber) {
		VersionNumber = versionNumber;
	}
}
