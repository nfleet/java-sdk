package fi.cosky.sdk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NFleetRequestException extends IOException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ErrorData> Items;

	public NFleetRequestException() {
		Items = new ArrayList<ErrorData>();
	}
	
	public NFleetRequestException(ErrorData data) {
		Items = new ArrayList<ErrorData>();
		Items.add(data);
	}
	
	public List<ErrorData> getItems() {
		return Items;
	}

	public void setItems(List<ErrorData> items) {
		Items = items;
	}	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for( ErrorData e : Items) {
			sb.append(" ErrorCode: "+e.getCode() + " ErrorMessage: " + e.getMessage());
		}
		return sb.toString();
	}
}
