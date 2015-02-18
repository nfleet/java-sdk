package fi.cosky.sdk;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class RoutingProblemSettingsUpdateRequest {
	public static final String MimeType = RoutingProblemSettingsData.MimeType;
	public static final double MimeVersion = RoutingProblemSettingsData.MimeVersion;
		
	private double DefaultVehicleSpeedFactor;
	private SpeedProfile DefaultVehicleSpeedProfile;
	private double InsertionAggressiveness;
	private String AlgorithmTree;
	private String DateTimeFormatString;
	
	/**
	 * Default constructor with good values for settings. These
	 * are also the ones that NFleet uses when settings
	 * are not specified 
	 */
	public RoutingProblemSettingsUpdateRequest() {
		this.DefaultVehicleSpeedFactor = 0.9;
		this.DefaultVehicleSpeedProfile = SpeedProfile.Max100Kmh;
		this.InsertionAggressiveness = 1;
	}
	
	public double getDefaultVehicleSpeedFactor() {
		return DefaultVehicleSpeedFactor;
	}
	
	public void setDefaultVehicleSpeedFactor(double defaultVehicleSpeedFactor) {
		DefaultVehicleSpeedFactor = defaultVehicleSpeedFactor;
	}
	
	public SpeedProfile getDefaultVehicleSpeedProfile() {
		return DefaultVehicleSpeedProfile;
	}
	
	public void setDefaultVehicleSpeedProfile(SpeedProfile defaultVehicleSpeedProfile) {
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

