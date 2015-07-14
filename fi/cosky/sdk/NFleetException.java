package fi.cosky.sdk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NFleetException extends IOException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ErrorData> Items;

	public NFleetException() {
		Items = new ArrayList<ErrorData>();
	}
	
	public NFleetException(ErrorData data) {
		Items = new ArrayList<ErrorData>();
		Items.add(data);
	}
	
	public List<ErrorData> getItems() {
		return Items;
	}

	public void setItems(List<ErrorData> items) {
		Items = items;
	}	
	
}
