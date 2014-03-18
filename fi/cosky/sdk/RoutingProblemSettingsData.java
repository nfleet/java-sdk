package fi.cosky.sdk;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class RoutingProblemSettingsData extends BaseData {
	private int VersionNumber;
	private double DefaultVehicleSpeedFactor;
	private double DefaultVehicleSpeedProfile;
	
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
	public double getDefaultVehicleSpeedProfile() {
		return DefaultVehicleSpeedProfile;
	}
	public void setDefaultVehicleSpeedProfile(double defaultVehicleSpeedProfile) {
		DefaultVehicleSpeedProfile = defaultVehicleSpeedProfile;
	}
}
