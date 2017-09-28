package fi.cosky.sdk;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

 import java.util.List;

 public class ModificationSetImportRequest extends BaseData {
    public static final String MimeType = "application/vnd.jyu.nfleet.modificationset";
    public static final double MimeVersion = ModificationDataSet.MimeVersion;
	
	private List<ModificationUpdateRequest> Items;

	public List<ModificationUpdateRequest> getItems() {
		return Items;
	}

	public void setItems(List<ModificationUpdateRequest> items) {
		Items = items;
	}
 }