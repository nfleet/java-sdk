package fi.cosky.sdk.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import fi.cosky.sdk.*;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
import fi.cosky.sdk.tests.TestHelper.Location;

public class TestData {
	
	@SuppressWarnings("deprecation")
	public static void CreateDemoData(RoutingProblemData problem, API api) {
		
		LocationData locationData = TestHelper.createLocationWithCoordinates(Location.VEHICLE_START);
        LocationData pickupLocation = TestHelper.createLocationWithCoordinates(Location.TASK_PICKUP);      
        LocationData deliveryLocation = TestHelper.createLocationWithCoordinates(Location.TASK_DELIVERY);
        
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

        try {
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
        } catch (NFleetRequestException e) {
        	System.out.println("Something went wrong");
        } catch (IOException e) {
        	System.out.println();
        }
	}
}
