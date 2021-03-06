package fi.cosky.sdk;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class RouteUpdateRequest extends BaseData {
	public static final String MimeType =  "application/vnd.jyu.nfleet.route";
	public static final double MimeVersion = 2.0;
	
	private int[] Items;
	private int VersionNumber;
	
	public int[] getSequence() {
		return Items;
	}
	public void setSequence(int[] sequence) {
		Items = sequence;
	}
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
