package fi.cosky.sdk.tests;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fi.cosky.sdk.*;
import fi.cosky.sdk.CoordinateData.CoordinateSystem;

public class TestHelper {
	private static API apis  = null;
	static String clientKey    = "";
	static String clientSecret = "";
	
	static API authenticate() {
		String url = "https://api.nfleet.fi";
		if (apis == null) {
			API api = new API(url);
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
		try {
			ApiData apiData = api.navigate(ApiData.class, api.getRoot());
			UserData user = new UserData();
			
			UserDataSet users = api.navigate(UserDataSet.class, apiData.getLink("list-users"));
			
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
		task.setCanBeRelocated("None");
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
		task.setCanBeRelocated("None");
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
			task.setCanBeRelocated("None");
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
        //vehicleRequest.setCanBeRelocated("None");
        return vehicleRequest;
        
	}
	
	static LocationData createLocationWithCoordinates(Location name) {
		CoordinateData coordinates = new CoordinateData();
		switch (name){
			case TASK_PICKUP: {
				coordinates.setLatitude(62.244958);
				coordinates.setLongitude(25.747143);
				break;
			}
			case TASK_DELIVERY: {
				coordinates.setLatitude(62.244589);
				coordinates.setLongitude(25.74892);
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
		coordinates.setSystem(CoordinateSystem.Euclidian);
		LocationData data = new LocationData();
		data.setCoordinatesData(coordinates);
		return data;
	}
	
	static LocationData createLocationWithAddress() {
		AddressData address = new AddressData();
		address.setCity("Jyväskylä");
		address.setCountry("Finland");
		address.setPostalCode("40100");
		address.setStreet("Mattilanniemi 2");
		
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
		Date startD = new Date();
		startD.setHours(start);
		
		Date endD = new Date();
		endD.setHours(end);
		
		TimeWindowData twd = new TimeWindowData(startD, endD);
		return twd;
	}
}
