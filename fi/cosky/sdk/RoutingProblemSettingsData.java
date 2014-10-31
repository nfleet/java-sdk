package fi.cosky.sdk;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class RoutingProblemSettingsData extends BaseData {
	public static final String MimeType = "application/vnd.jyu.nfleet.problemsettings+json";
	public static final double MimeVersion = 2.0;
	
	private int VersionNumber;
	private double DefaultVehicleSpeedFactor;
	private String DefaultVehicleSpeedProfile;
	
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
}
