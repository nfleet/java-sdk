package fi.cosky.sdk.tests;

import java.util.ArrayList;
import java.util.Date;

import fi.cosky.sdk.*;

public class TestData {
	
	@SuppressWarnings("deprecation")
	public static void CreateDemoData(RoutingProblemData problem, API api) {
		
		CoordinateData coordinateData = new CoordinateData();
		coordinateData.setLatitude(54.130888);
        coordinateData.setLongitude(12.00938);
        coordinateData.setSystem(CoordinateData.CoordinateSystem.WGS84);
        LocationData locationData = new LocationData();
        locationData.setCoordinatesData(coordinateData);
        
        CoordinateData pickup = new CoordinateData();
        pickup.setLatitude(54.14454);
        pickup.setLongitude(12.108808);
        pickup.setSystem(CoordinateData.CoordinateSystem.WGS84);
        LocationData pickupLocation = new LocationData();
        pickupLocation.setCoordinatesData(pickup);

        CoordinateData delivery = new CoordinateData();
        delivery.setLatitude(53.545867);
        delivery.setLongitude(10.276409);
        delivery.setSystem(CoordinateData.CoordinateSystem.WGS84);
        LocationData deliveryLocation = new LocationData();
        deliveryLocation.setCoordinatesData(delivery);

        ArrayList<CapacityData> capacities = new ArrayList<CapacityData>();
        capacities.add(new CapacityData("Weight", 100000));
        ArrayList<TimeWindowData> timeWindows = new ArrayList<TimeWindowData>();
        Date morning = new Date();
        morning.setHours(7);
        Date evening = new Date();
        evening.setHours(16);
        timeWindows.add(new TimeWindowData(morning, evening));

        VehicleUpdateRequest vehicleRequest = new VehicleUpdateRequest("demoVehicle",capacities, locationData, locationData);
        vehicleRequest.setTimeWindows(timeWindows);

        api.navigate(ResponseData.class, problem.getLink("create-vehicle"), vehicleRequest);
                
        ArrayList<CapacityData> taskCapacity = new ArrayList<CapacityData>();
        taskCapacity.add(new CapacityData("Weight", 1));
        
        ArrayList<TaskEventUpdateRequest> taskEvents = new ArrayList<TaskEventUpdateRequest>();
        taskEvents.add(new TaskEventUpdateRequest(Type.Pickup, pickupLocation, taskCapacity));
        taskEvents.add(new TaskEventUpdateRequest(Type.Delivery, deliveryLocation, taskCapacity));
        TaskUpdateRequest task = new TaskUpdateRequest(taskEvents);
        task.setName("testTask");
        taskEvents.get(0).setTimeWindows(timeWindows);
        taskEvents.get(1).setTimeWindows(timeWindows);
        taskEvents.get(0).setServiceTime(10);
        taskEvents.get(1).setServiceTime(10);

        api.navigate(ResponseData.class, problem.getLink("create-task"), task);
        
	}
}
