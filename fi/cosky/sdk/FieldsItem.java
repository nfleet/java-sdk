package fi.cosky.sdk;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

import java.util.List;

public class FieldsItem {
	private String Name;
	private String Uri;
	private List<RouteEventData> Events;
	private KPIData KPIs;
		
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getUri() {
		return Uri;
	}
	public void setUri(String uri) {
		Uri = uri;
	}
	public KPIData getKPIs() {
		return KPIs;
	}
	public void setKPIs(KPIData kPIs) {
		KPIs = kPIs;
	}
	public List<RouteEventData> getEvents() {
		return Events;
	}
	public void setEvents(List<RouteEventData> events) {
		Events = events;
	}
	
	@Override
	public String toString() {
		return this.Name + " " + this.Uri + " " + Events;
	}
	
	
}
