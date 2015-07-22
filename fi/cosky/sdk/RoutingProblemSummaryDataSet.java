package fi.cosky.sdk;

import java.util.List;

public class RoutingProblemSummaryDataSet extends BaseData {
	public static String MIMEType = "application/vnd.jyu.nfleet.problemsummaryset";
	public static String MIMEVersion = "2.0";
	
	private List<RoutingProblemSummaryData> Items;

	public List<RoutingProblemSummaryData> getItems() {
		return Items;
	}

	public void setItems(List<RoutingProblemSummaryData> items) {
		Items = items;
	}
}
