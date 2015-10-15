package fi.cosky.sdk;
import java.util.Date;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */


public class RoutingProblemSummaryData extends BaseData{
	public static String MIMEType = "application/vnd.jyu.nfleet.problemsummary";
	public static String MIMEVersion = "2.0";
	
	private int Id;
	private String Name;
	private Date Modified;
	private String State;
	private int Progress;
	private RoutingProblemSettingsData Settings;
	private SummaryData Summary;
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Date getModified() {
		return Modified;
	}
	public void setModified(Date modified) {
		Modified = modified;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public int getProgress() {
		return Progress;
	}
	public void setProgress(int progress) {
		Progress = progress;
	}
	public RoutingProblemSettingsData getSettings() {
		return Settings;
	}
	public void setSettings(RoutingProblemSettingsData settings) {
		Settings = settings;
	}
	public SummaryData getSummary() {
		return Summary;
	}
	public void setSummary(SummaryData summary) {
		Summary = summary;
	}
}
