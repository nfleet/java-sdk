package fi.cosky.sdk;
import java.util.ArrayList;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class UserDataSet extends BaseData {
    public static final String MimeType = "application/vnd.jyu.nfleet.userset";
    public static final double MimeVersion = 2.1;

    private ArrayList<UserData> Items;
    private int VersionNumber;
        
    public ArrayList<UserData> getItems() {
        return Items;
    }

    public void setItems(ArrayList<UserData> items) {
        Items = items;
    }

	int getVersionNumber() {
		return VersionNumber;
	}

	void setVersionNumber(int versionNumber) {
		VersionNumber = versionNumber;
	}
    
}
