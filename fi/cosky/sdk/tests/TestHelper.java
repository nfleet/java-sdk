package fi.cosky.sdk.tests;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fi.cosky.sdk.*;
import fi.cosky.sdk.CoordinateData.CoordinateSystem;

public class TestHelper {
	public static API apis  = null;
	
	static String apiUrl = "";
	static String clientKey    = "";
	static String clientSecret = "";
	
	static API authenticate() {
 		if (apis == null) {
			API api = new API(apiUrl);
			api.setTimed(true);
			if (api.authenticate(clientKey, clientSecret)) {
				apis = api;
			} else {
				System.out.println("Could not authenticate, please check service and credentials");
				return null;
			}
			
		}
		return apis;
	}
		
	static UserData getOrCreateUser( API api ) {
		return getOrCreateUser(api, false);
	}

	static UserData getOrCreateUser( API api, boolean initialize ) {
		try {
			ApiData apiData = api.navigate(ApiData.class, api.getRoot());
			UserData user = new UserData();

			if (initialize) {
				UserDataSet users = api.navigate(UserDataSet.class, apiData.getLink("list-users"));
				initialize(api, users);
			}

			ResponseData createdUser = api.navigate(ResponseData.class, apiData.getLink("create-user"), user);
			user = api.navigate(UserData.class, createdUser.getLocation());

			return user;
		} catch (NFleetRequestException e) {
			System.out.println("Something went wrong");
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	static RoutingProblemData createProblem(API api, UserData user) {
		try {
			RoutingProblemData problem = new RoutingProblemData("exampleProblem");
			ResponseData created = api.navigate(ResponseData.class, user.getLink("create-problem"), problem);
			problem = api.navigate(RoutingProblemData.class, created.getLocation());
			return problem;
		} catch (NFleetRequestException e) {
			System.out.println("Something went wrong");
			return null;
		} catch (IOException e) {
			return null;
		}
	}
	
	static RoutingProblemData createProblemWithDemoData(API api, UserData user) {
		RoutingProblemData problem = createProblem(api, user);
		TestData.CreateDemoData(problem, api);
		return problem;
	}
	
	static VehicleData getVehicle(API api, UserData user, RoutingProblemData problem) {
		try {
			TestData.CreateDemoData(problem, api);
			VehicleDataSet vehicles = api.navigate(VehicleDataSet.class, problem.getLink("list-vehicles"));
			VehicleData vehicle = null;
			vehicle = api.navigate(VehicleData.class, vehicles.getItems().get(0).getLink("self"));
			return vehicle;
		} catch (NFleetRequestException e) {
			System.out.println("Something went wrong: " + e.toString());
			return null;
		} catch (IOException e) {
			return null;
		}
	} 
	
	static TaskData getTask(API api, RoutingProblemData problem) {		
		LocationData pi = createLocationWithCoordinates(Location.TASK_PICKUP);
		LocationData de = createLocationWithCoordinates(Location.TASK_DELIVERY);
				
		CapacityData capacity = new CapacityData("Weight", 20);
		List<CapacityData> capacities = new ArrayList<CapacityData>();
		capacities.add(capacity);
		
		TaskEventUpdateRequest task1 = new TaskEventUpdateRequest(Type.Pickup, pi, capacities);
		TaskEventUpdateRequest task2 = new TaskEventUpdateRequest(Type.Delivery, de, capacities);
		
		TimeWindowData twstart = createTimeWindow(7, 20);
		ArrayList<TimeWindowData> timewindows = new ArrayList<TimeWindowData>();
		timewindows.add(twstart);
		task1.setTimeWindows(timewindows);
		task2.setTimeWindows(timewindows);
		List<TaskEventUpdateRequest> both = new ArrayList<TaskEventUpdateRequest>();
		both.add(task1);
		both.add(task2);
		TaskUpdateRequest task = new TaskUpdateRequest(both);
		task.setName("testTask");
		task.setRelocationType("None");
		task.setActivityState("Active");
		try {
			ResponseData createdTask = api.navigate(ResponseData.class, problem.getLink("create-task"), task);
			TaskData td = api.navigate(TaskData.class, createdTask.getLocation());
			return td;
		} catch (NFleetRequestException e) {
			System.out.println("Something went wrong " + e.toString());
			return null;
		} catch (IOException e) {
			return null;
		}
	}
	
	static TaskUpdateRequest createTaskUpdateRequest(String name) {
		LocationData pi = createLocationWithCoordinates(Location.TASK_PICKUP);
		LocationData de = createLocationWithCoordinates(Location.TASK_DELIVERY);
		CapacityData capacity = new CapacityData("Weight", 20);
		List<CapacityData> capacities = new ArrayList<CapacityData>();
		capacities.add(capacity);
		TaskEventUpdateRequest task1 = new TaskEventUpdateRequest(Type.Pickup, pi, capacities);
		
		List<TimeWindowData> timeWindows = new ArrayList<TimeWindowData>();
		TimeWindowData tw = createTimeWindow(7, 20);
		timeWindows.add(tw);
		
		task1.setTimeWindows(timeWindows);
		TaskEventUpdateRequest task2 = new TaskEventUpdateRequest(Type.Delivery, de, capacities);
		task2.setTimeWindows(timeWindows);
		List<TaskEventUpdateRequest> both = new ArrayList<TaskEventUpdateRequest>();
		both.add(task1);
		both.add(task2);
		TaskUpdateRequest task = new TaskUpdateRequest(both);
		task.setName(name);
		task.setRelocationType("None");
		return task;
	}
	
	static List<TaskUpdateRequest> createListOfTasks(int howMany) {
		List<TaskUpdateRequest> tasks = new ArrayList<TaskUpdateRequest>();
		for (int i = 0; i < howMany; i++) {
			LocationData pi = createLocationWithCoordinates(Location.TASK_PICKUP);
			LocationData de = createLocationWithCoordinates(Location.TASK_DELIVERY);
				
			CapacityData capacity = new CapacityData("Weight", 20);
			List<CapacityData> capacities = new ArrayList<CapacityData>();
			capacities.add(capacity);
			TaskEventUpdateRequest task1 = new TaskEventUpdateRequest(Type.Pickup, pi, capacities);
			TaskEventUpdateRequest task2 = new TaskEventUpdateRequest(Type.Delivery, de, capacities);
			
			TimeWindowData twstart = createTimeWindow(7, 20);
			ArrayList<TimeWindowData> timewindows = new ArrayList<TimeWindowData>();
			timewindows.add(twstart);
			task1.setTimeWindows(timewindows);
			task2.setTimeWindows(timewindows);
			List<TaskEventUpdateRequest> both = new ArrayList<TaskEventUpdateRequest>();
			both.add(task1);
			both.add(task2);
			TaskUpdateRequest task = new TaskUpdateRequest(both);
			task.setName("testTask" + i);
			task.setRelocationType("None");
			task.setActivityState("Active");
            task.setInfo("task " + i);
			tasks.add(task);
		}
		return tasks;
	}
	
	static VehicleUpdateRequest createVehicleUpdateRequest(String name) {
		ArrayList<CapacityData> capacities = new ArrayList<CapacityData>();
        capacities.add(new CapacityData("Weight", 100000));
        ArrayList<TimeWindowData> timeWindows = new ArrayList<TimeWindowData>();
        
        timeWindows.add(createTimeWindow(7, 20));
                
        LocationData startLocation = createLocationWithCoordinates(Location.VEHICLE_START);
		VehicleUpdateRequest vehicleRequest = new VehicleUpdateRequest(name, capacities, startLocation, startLocation);
		vehicleRequest.setVehicleSpeedProfile( SpeedProfile.Max80Kmh.toString() );
		vehicleRequest.setVehicleSpeedFactor(0.7);
        vehicleRequest.setTimeWindows(timeWindows);
        vehicleRequest.setInfo1("Info1");
        return vehicleRequest;
        
	}
	
    static VehicleUpdateRequest createVehicleUpdateRequestWithAddress(String name) {
        ArrayList<CapacityData> capacities = new ArrayList<CapacityData>();
        capacities.add(new CapacityData("Weight", 100000));
        ArrayList<TimeWindowData> timeWindows = new ArrayList<TimeWindowData>();

        timeWindows.add(createTimeWindow(7, 20));

        LocationData startLocation = createLocationWithAddress();
        VehicleUpdateRequest vehicleRequest = new VehicleUpdateRequest(name, capacities, startLocation, startLocation);
        vehicleRequest.setVehicleSpeedProfile( SpeedProfile.Max80Kmh.toString() );
        vehicleRequest.setVehicleSpeedFactor(0.7);
        vehicleRequest.setTimeWindows(timeWindows);
        vehicleRequest.setInfo1("Info1");
        return vehicleRequest;

    }
    static List<VehicleUpdateRequest> createListOfVehicles(int howMany) {
        List<VehicleUpdateRequest> vehicles = new ArrayList<VehicleUpdateRequest>();

        for (int i = 0; i < howMany; i++) {
            vehicles.add(createVehicleUpdateRequest("vehicle"+i));
        }
        return vehicles;
    }

	static DepotUpdateRequest createDepotUpdateRquest(String name) {
		 LocationData location = new LocationData();
         location.setCoordinatesData(new CoordinateData( 0.0, 0.0, CoordinateSystem.Euclidian ));

         ArrayList<CapacityData> capacities = new ArrayList<CapacityData>();
         capacities.add(new CapacityData("Weight", 1000));
         DepotUpdateRequest request = new DepotUpdateRequest();
         request.setLocation(location);
         request.setCapacities(capacities);
         request.setName(name);
         request.setInfo1("Info");
         return request;
	}
	
	static LocationData createLocationWithCoordinates(Location name) {
		CoordinateData coordinates = new CoordinateData();
		switch (name){
			case TASK_PICKUP: {
				coordinates.setLatitude(62.281020);
				coordinates.setLongitude(25.802570);
				break;
			}
			case TASK_DELIVERY: {
				coordinates.setLatitude(62.290522);
				coordinates.setLongitude(25.738774);
				break;
			}
			case VEHICLE_END: {
				coordinates.setLatitude(62.300666); 
				coordinates.setLongitude(25.727949);
				break;
			}
			case VEHICLE_START:
			default: {
	            coordinates.setLatitude(62.247906);
	            coordinates.setLongitude(25.867395);
	            break;
			}
			
			
		}
		coordinates.setSystem(CoordinateSystem.WGS84);
		LocationData data = new LocationData();
		data.setCoordinatesData(coordinates);
		return data;
	}
	
	static LocationData createLocationWithAddress() {
		AddressData address = new AddressData();
		address.setCity("Jyväskylä");
		address.setCountry("Finland");
		address.setPostalCode("40320");
		address.setStreet("Tuohitie");
		address.setApartmentNumber(22);
		
		LocationData data = new LocationData();
		data.setAddress(address);
		return data;
	}
	
	static VehicleData createAndGetVehicle(API api, RoutingProblemData problem, VehicleUpdateRequest request) {
		try {
			ResponseData result = api.navigate(ResponseData.class, problem.getLink("create-vehicle"), request);
			VehicleData vehicle = api.navigate(VehicleData.class, result.getLocation());
			return vehicle;
		}
		catch (Exception e) {
			System.out.println("Something went wrong. Unable to create a vehicle.");
		}
		return null;
	}
	
	static TaskData createAndGetTask(API api, RoutingProblemData problem, TaskUpdateRequest request) {
		try {
			ResponseData result = api.navigate(ResponseData.class, problem.getLink("create-task"), request);
			TaskData task = api.navigate(TaskData.class, result.getLocation());
			return task;
		}
		catch (Exception e) {
			System.out.println("Something went wrong. Unable to create a task.");
		}
		return null;
	}
	
	enum Location{ VEHICLE_START, TASK_PICKUP, TASK_DELIVERY, VEHICLE_END };
	 
	static TimeWindowData createTimeWindow(int start, int end) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(Calendar.HOUR_OF_DAY, start);
		Date startD = calendar.getTime();
				
		calendar.set(Calendar.HOUR_OF_DAY, end);
		Date endD = calendar.getTime();
				
		TimeWindowData twd = new TimeWindowData(startD, endD);
		return twd;
	}
	
	static void initialize(API api, UserDataSet users) {
		try {
			if (users.getItems().size() > 0) {
				UserData us = null;
				for (UserData u : users.getItems() ) {
					us = api.navigate(UserData.class, u.getLink("self"));
					Link l = null;	
					if ((l = us.getLink("delete-user")) == null) break;  
					ResponseData response = api.navigate(ResponseData.class, us.getLink("delete-user"));
				}		
			}
		} catch (Exception e) { System.out.println("Something went wrong deleting users " + e.getMessage()); }
	}
	
	static String getInfo1WithRouteEvent(RouteEventData routeEvent, TaskDataSet tasks) {
		  if (routeEvent == null) return "";
		  if (tasks == null) return "";
		  		  
		  for (TaskData t : tasks.getItems()) {
		    if (t.getId() == routeEvent.getTaskId()) return t.getInfo();
		  }
		  return "";
	}
}
