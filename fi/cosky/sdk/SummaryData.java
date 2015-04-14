package fi.cosky.sdk;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class SummaryData {
	private double TravelDistanceSum;
	private double WorkingTimeSum;
	private int TotalTaskCount;
	private int TotalVehicleCount;
	private int UsedVehicleCount;
	
	public double getTravelDistanceSum() {
		return TravelDistanceSum;
	}
	public void setTravelDistanceSum(double travelDistanceSum) {
		TravelDistanceSum = travelDistanceSum;
	}
	public double getWorkingTimeSum() {
		return WorkingTimeSum;
	}
	public void setWorkingTimeSum(double workingTimeSum) {
		WorkingTimeSum = workingTimeSum;
	}
	public int getTotalTaskCount() {
		return TotalTaskCount;
	}
	public void setTotalTaskCount(int totalTaskCount) {
		TotalTaskCount = totalTaskCount;
	}
	public int getTotalVehicleCount() {
		return TotalVehicleCount;
	}
	public void setTotalVehicleCount(int totalVehicleCount) {
		TotalVehicleCount = totalVehicleCount;
	}
	public int getUsedVehicleCount() {
		return UsedVehicleCount;
	}
	public void setUsedVehicleCount(int usedVehicleCount) {
		UsedVehicleCount = usedVehicleCount;
	}
	
	
}
