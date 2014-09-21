package fi.cosky.sdk;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class KPIData {
        private double AccumulatedTravelTimeWithCargo;
        private double AccumulatedTravelTimeEmpty;
        private double AccumulatedLoadingTime;
        private double AccumulatedWaitingTime;
        private double AccumulatedWorkingTime;
        private double AccumulatedTravelDistance;
        private double AccumulatedPickups;	        
        private double AccumulatedDeliveries;    
        private double LoadPercentage;
        private double HighestLoadPercentage;
        private double TravelTimeWithCargoPercentage;
        private double TravelTimeEmptyPercentage;
        private double LoadingTimePercentage;
        private double WaitingTimePercentage;
        
		public double getAccumulatedTravelTimeWithCargo() {
			return AccumulatedTravelTimeWithCargo;
		}
		public void setAccumulatedTravelTimeWithCargo(
				double accumulatedTravelTimeWithCargo) {
			AccumulatedTravelTimeWithCargo = accumulatedTravelTimeWithCargo;
		}
		public double getAccumulatedTravelTimeEmpty() {
			return AccumulatedTravelTimeEmpty;
		}
		public void setAccumulatedTravelTimeEmpty(double accumulatedTravelTimeEmpty) {
			AccumulatedTravelTimeEmpty = accumulatedTravelTimeEmpty;
		}
		public double getAccumulatedLoadingTime() {
			return AccumulatedLoadingTime;
		}
		public void setAccumulatedLoadingTime(double accumulatedLoadingTime) {
			AccumulatedLoadingTime = accumulatedLoadingTime;
		}
		public double getAccumulatedWaitingTime() {
			return AccumulatedWaitingTime;
		}
		public void setAccumulatedWaitingTime(double accumulatedWaitingTime) {
			AccumulatedWaitingTime = accumulatedWaitingTime;
		}
		public double getAccumulatedWorkingTime() {
			return AccumulatedWorkingTime;
		}
		public void setAccumulatedWorkingTime(double accumulatedWorkingTime) {
			AccumulatedWorkingTime = accumulatedWorkingTime;
		}
		public double getAccumulatedTravelDistance() {
			return AccumulatedTravelDistance;
		}
		public void setAccumulatedTravelDistance(double accumulatedTravelDistance) {
			AccumulatedTravelDistance = accumulatedTravelDistance;
		}
		public double getAccumulatedPickups() {
			return AccumulatedPickups;
		}
		public void setAccumulatedPickups(double accumulatedPickups) {
			AccumulatedPickups = accumulatedPickups;
		}
		public double getAccumulatedDeliveries() {
			return AccumulatedDeliveries;
		}
		public void setAccumulatedDeliveries(double accumulatedDeliveries) {
			AccumulatedDeliveries = accumulatedDeliveries;
		}
		public double getLoadPercentage() {
			return LoadPercentage;
		}
		public void setLoadPercentage(double loadPercentage) {
			LoadPercentage = loadPercentage;
		}
		public double getHighestLoadPercentage() {
			return HighestLoadPercentage;
		}
		public void setHighestLoadPercentage(double highestLoadPercentage) {
			HighestLoadPercentage = highestLoadPercentage;
		}
		public double getTravelTimeWithCargoPercentage() {
			return TravelTimeWithCargoPercentage;
		}
		public void setTravelTimeWithCargoPercentage(
				double travelTimeWithCargoPercentage) {
			TravelTimeWithCargoPercentage = travelTimeWithCargoPercentage;
		}
		public double getTravelTimeEmptyPercentage() {
			return TravelTimeEmptyPercentage;
		}
		public void setTravelTimeEmptyPercentage(double travelTimeEmptyPercentage) {
			TravelTimeEmptyPercentage = travelTimeEmptyPercentage;
		}
		public double getLoadingTimePercentage() {
			return LoadingTimePercentage;
		}
		public void setLoadingTimePercentage(double loadingTimePercentage) {
			LoadingTimePercentage = loadingTimePercentage;
		}
		public double getWaitingTimePercentage() {
			return WaitingTimePercentage;
		}
		public void setWaitingTimePercentage(double waitingTimePercentage) {
			WaitingTimePercentage = waitingTimePercentage;
		}
}
