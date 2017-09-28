package fi.cosky.sdk;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

 import java.util.List;

 public class ModificationAreaSetImportRequest extends BaseData {
    	
	private List<ModificationAreaImportRequest> Items;

	public List<ModificationAreaImportRequest> getItems() {
		return Items;
	}

	public void setItems(List<ModificationAreaImportRequest> items) {
		Items = items;
	}
 }