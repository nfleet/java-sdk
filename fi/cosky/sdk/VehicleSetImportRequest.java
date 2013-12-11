package fi.cosky.sdk;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

import java.util.List;

public class VehicleSetImportRequest {
	
	private List<VehicleUpdateRequest> Items;

	public List<VehicleUpdateRequest> getItems() {
		return Items;
	}

	public void setItems(List<VehicleUpdateRequest> items) {
		Items = items;
	}
	
}
