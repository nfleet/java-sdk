package fi.cosky.sdk;

import java.util.ArrayList;

public class BatchUpdateRequest extends BaseData {
	private ArrayList<Integer> Ids;
	private ArrayList<ModifyOperationData> Ops;
	
	public ArrayList<Integer> getIds() {
		return Ids;
	}
	public void setIds(ArrayList<Integer> ids) {
		Ids = ids;
	}
	public ArrayList<ModifyOperationData> getOps() {
		return Ops;
	}
	public void setOps(ArrayList<ModifyOperationData> ops) {
		Ops = ops;
	}
}
