package fi.cosky.sdk;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

import java.util.List;

public class PlanData extends BaseData {
	private int VersionNumber;
	private List<FieldsItem> Items;
	private KPIData KPIs;
	private List<TaskData> Unassigned;
	
	public int getVersionNumber() {
		return VersionNumber;
	}
	public void setVersionNumber(int versionNumber) {
		VersionNumber = versionNumber;
	}
	public List<FieldsItem> getItems() {
		return Items;
	}
	public void setItems(List<FieldsItem> items) {
		Items = items;
	}
	
	public KPIData getKPIs() {
		return KPIs;
	}
	public void setKPIs(KPIData kPIs) {
		KPIs = kPIs;
	}
	public List<TaskData> getUnassigned() {
		return Unassigned;
	}
	public void setUnassigned(List<TaskData> unassigned) {
		Unassigned = unassigned;
	}
}
