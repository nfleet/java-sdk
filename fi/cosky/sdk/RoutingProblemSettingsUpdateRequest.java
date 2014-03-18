package fi.cosky.sdk;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class RoutingProblemSettingsUpdateRequest {
	
	private double DefaultVehicleSpeedFactor;
	private double DefaultVehicleSpeedProfile;
	
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
