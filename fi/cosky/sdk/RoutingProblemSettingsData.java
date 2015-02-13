package fi.cosky.sdk;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class RoutingProblemSettingsData extends BaseData {
	public static final String MimeType = "application/vnd.jyu.nfleet.problemsettings";
	public static final double MimeVersion = 2.1;
	
	private int VersionNumber;
	private double DefaultVehicleSpeedFactor;
	private String DefaultVehicleSpeedProfile;
	private double InsertionAggressiveness;
	private String AlgorithmTree;
	private String DateTimeFormatString;
		
	public int getVersionNumber() {
		return VersionNumber;
	}
	public void setVersionNumber(int versionNumber) {
		VersionNumber = versionNumber;
	}
	public double getDefaultVehicleSpeedFactor() {
		return DefaultVehicleSpeedFactor;
	}
	public void setDefaultVehicleSpeedFactor(double defaultVehicleSpeedFactor) {
		DefaultVehicleSpeedFactor = defaultVehicleSpeedFactor;
	}
	public String getDefaultVehicleSpeedProfile() {
		return DefaultVehicleSpeedProfile;
	}
	public void setDefaultVehicleSpeedProfile(String defaultVehicleSpeedProfile) {
		DefaultVehicleSpeedProfile = defaultVehicleSpeedProfile;
	}
	public double getInsertionAggressiveness() {
		return InsertionAggressiveness;
	}
	public void setInsertionAggressiveness(double insertionAggressiveness) {
		InsertionAggressiveness = insertionAggressiveness;
	}
	public String getAlgorithmTree() {
		return AlgorithmTree;
	}
	public void setAlgorithmTree(String algorithmTree) {
		AlgorithmTree = algorithmTree;
	}
	public String getDateTimeFormatString() {
		return DateTimeFormatString;
	}
	public void setDateTimeFormatString(String dateTimeFormatString) {
		DateTimeFormatString = dateTimeFormatString;
	}
}
