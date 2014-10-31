package fi.cosky.sdk;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class UserData extends BaseData {
	public static final String MimeType = "application/vnd.jyu.nfleet.user+json";
	public static final double MimeVersion = 2.0;
	
    private int Id;
    private int ClientId;
    private int VersionNumber;
    private int OptimizationCount;
    
    public int getId() {
        return Id;
    }
    
    public void setId(int id) {
        Id = id;
    }

	public int getClientId() {
		return ClientId;
	}

	public void setClientId(int clientId) {
		ClientId = clientId;
	}

	int getVersionNumber() {
		return VersionNumber;
	}

	void setVersionNumber(int versionNumber) {
		VersionNumber = versionNumber;
	}

	public int getOptimizationCount() {
		return OptimizationCount;
	}

	public void setOptimizationCount(int optimizationCount) {
		OptimizationCount = optimizationCount;
	}
}
