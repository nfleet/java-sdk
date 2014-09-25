package fi.cosky.sdk.tests;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import fi.cosky.sdk.*;
import fi.cosky.sdk.CoordinateData.CoordinateSystem;
import fi.cosky.sdk.tests.TestHelper.Location;

import org.junit.*;

import static org.junit.Assert.*;

public class SdkTests {
	
	@Test
	public void T00RootLinkTest() {
		String clientKey = TestHelper.clientKey, clientSecret = TestHelper.clientSecret;
		ApiData data2 = null;
		try {
			//##BEGIN EXAMPLE accessingapi##
            API api = new API("https://api.nfleet.fi");
			api.authenticate(clientKey, clientSecret);
			ApiData data = api.navigate(ApiData.class, api.getRoot());
			//##END EXAMPLE##
			data2 = data;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		assertNotNull(data2);
		assertNotNull(data2.getLinks());	
	}
	
	@Test
	public void T01CreatingProblemTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData asdf = null;
		try {
			//##BEGIN EXAMPLE creatingproblem##
			RoutingProblemUpdateRequest update = new RoutingProblemUpdateRequest("TestProblem");
			ResponseData createdProblem = api.navigate(ResponseData.class, user.getLink("create-problem"), update);
			RoutingProblemData problem = api.navigate(RoutingProblemData.class, createdProblem.getLocation());
			//##END EXAMPLE##
			asdf = problem;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		assertNotNull(asdf);
	}
	
	@Test
	public void T02AccesingProblemTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemUpdateRequest requ = new RoutingProblemUpdateRequest("testproblem");
		RoutingProblemData check = null;
		
		try {
			ResponseData created = api.navigate(ResponseData.class, user.getLink("create-problem"), requ);
			
			//##BEGIN EXAMPLE accessingproblem##
			RoutingProblemData problem = api.navigate(RoutingProblemData.class, created.getLocation());
			//##END EXAMPLE##
			check = problem;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		assertEquals(requ.getName(), check.getName());
	}
	
	@Test
	public void T03ListingTasksTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		TaskDataSet collection = null;
		try {
			//##BEGIN EXAMPLE listingtasks##
			TaskDataSet tasks = api.navigate(TaskDataSet.class, problem.getLink("list-tasks"));
			//##END EXAMPLE##
			collection = tasks;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		assertNotNull(collection);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void T04CreatingTaskTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		TaskData asdf = null;
		TaskUpdateRequest update = null;

        try {
    		//##BEGIN EXAMPLE creatingtask##		      
            CoordinateData pickup = new CoordinateData();
            pickup.setLatitude(54.14454);
            pickup.setLongitude(12.108808);
            pickup.setSystem(CoordinateSystem.Euclidian);
            LocationData pickupLocation = new LocationData();
            pickupLocation.setCoordinatesData(pickup);

            CoordinateData delivery = new CoordinateData();
            delivery.setLatitude(53.545867);
            delivery.setLongitude(10.276409);
            delivery.setSystem(CoordinateSystem.Euclidian);
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
            ResponseData result = api.navigate(ResponseData.class, problem.getLink("create-task"), task); 
    		//##END EXAMPLE##
    		update = task;
            asdf = api.navigate(TaskData.class, result.getLocation());
		} catch (Exception e) {
			System.out.println(e.toString());
		}

        assertEquals(asdf.getName(), update.getName());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void T05UpdatingTaskTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		TaskData oldTask = TestHelper.getTask(api, problem);
		List<TaskEventUpdateRequest> events = new ArrayList<TaskEventUpdateRequest>();
		TaskUpdateRequest asdf = null;
		try {
			//##BEGIN EXAMPLE updatingtask##
			TaskUpdateRequest task = oldTask.toRequest();
			task.setName("newName");
			ResponseData newTaskLocation = api.navigate(ResponseData.class, oldTask.getLink("update"), task);
			//##END EXAMPLE##
			oldTask = api.navigate(TaskData.class, oldTask.getLink("self"));
			asdf = task;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
			
		assertEquals(oldTask.getName(), asdf.getName());
	}
	
	@Test
	public void T06DeletingTaskTest() {
		
	}
	
	@Test
	public void T07ListingVehiclesTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		VehicleDataSet asdf = null;
		try { 
			//##BEGIN EXAMPLE listingvehicles##
			VehicleDataSet vehicles = api.navigate(VehicleDataSet.class, problem.getLink("list-vehicles"));
			//##END EXAMPLE##
			asdf = vehicles;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		assertNotNull(asdf.getItems());
	}
	
	@Test
	public void T08AccessingTaskSeqTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		VehicleData vehicle = TestHelper.getVehicle(api, user, problem);
		RouteEventDataSet asdf = null;
		try {
			RouteData routes = api.navigate(RouteData.class, vehicle.getLink("get-route"));
			RouteUpdateRequest route = new RouteUpdateRequest();
			int[] sd = {11,12};
			route.setClientId(user.getClientId());
			route.setProblemId(problem.getId());
			route.setUserId(user.getId());
			route.setSequence(sd);
			
			
			api.navigate(ResponseData.class, vehicle.getLink("set-route"), route);
			
			//##BEGIN EXAMPLE accessingtaskseq##
			RouteEventDataSet events = api.navigate(RouteEventDataSet.class, vehicle.getLink("list-events"));
			//##END EXAMPLE##
			asdf = events;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		assertNotNull(asdf);
	}
	
	@Test
	public void T09AccessingRouteTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		VehicleData vehicle = TestHelper.getVehicle(api, user, problem);
		RouteData routes = null;
		try {
			routes = api.navigate(RouteData.class, vehicle.getLink("get-route"));
			RouteUpdateRequest route = new RouteUpdateRequest();
			int[] sequence = {11,12};
			route.setClientId(user.getClientId());
			route.setProblemId(problem.getId());
			route.setUserId(user.getId());
			route.setSequence(sequence);
			
			
			api.navigate(ResponseData.class, vehicle.getLink("set-route"), route);
			
			//##BEGIN EXAMPLE accessingroute##
			RouteData routeData = api.navigate(RouteData.class, vehicle.getLink("get-route"));
			//##END EXAMPLE##
			routes = routeData;

		} catch (Exception e) {
			System.out.println(e.toString());
		}
				
		assertNotNull(routes);
	}
	
	@Test
	public void T10UpdatingRouteTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		VehicleData vehicle = TestHelper.getVehicle(api, user, problem);
		int[] a = null;
		int[] b = null;
 		try {
			//##BEGIN EXAMPLE updatingroute##
			RouteData routes = api.navigate(RouteData.class, vehicle.getLink("get-route"));
			RouteUpdateRequest route = new RouteUpdateRequest();
			int[] sequence = {11 , 12, 21, 22};		
			route.setSequence(sequence);
			ResponseData asdf = api.navigate(ResponseData.class, vehicle.getLink("set-route"), route);
			//##END EXAMPLE##
			
			RouteData routeData = api.navigate(RouteData.class, vehicle.getLink("get-route"));
			a = sequence;
			b = routeData.getItems();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
		assertArrayEquals(a, b);
	}
	
	@Test
	public void T11StartingOptimizationTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		ResponseData asdf = null;
		try { 
			problem = api.navigate(RoutingProblemData.class, problem.getLink("self"));
			
			//##BEGIN EXAMPLE startingopt##
			RoutingProblemUpdateRequest update = problem.toRequest();
			update.setState("Running");
			ResponseData result = api.navigate(ResponseData.class, problem.getLink("toggle-optimization"), update);
			//##END EXAMPLE##
			asdf = result;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		assertNotNull(asdf);
	}
	
	@Test
	public void T12StoppingOptimizationTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		ResponseData result = null;
		try {
			problem = api.navigate(RoutingProblemData.class, problem.getLink("self"));
			
			RoutingProblemUpdateRequest update = problem.toRequest();
			update.setState("Running");
			result = api.navigate(ResponseData.class, problem.getLink("toggle-optimization"), update);
			
			//##BEGIN EXAMPLE stoppingopt##
			RoutingProblemUpdateRequest updateRequest = problem.toRequest();
			updateRequest.setState("Stopped");
			result = api.navigate(ResponseData.class, problem.getLink("toggle-optimization"), updateRequest);
			//##END EXAMPLE##
		} catch (Exception e) {
			System.out.println(e.toString());
		}
				
		assertNotNull(result);
	}
	
	@Test
	public void T13CreatingUserTest() {
		API api = TestHelper.authenticate();
		int b = 0;
		int a = 0;
		try {
			ApiData data = api.navigate(ApiData.class, api.getRoot());
			
			//##BEGIN EXAMPLE creatingauser##
			UserDataSet users = api.navigate(UserDataSet.class, data.getLink("list-users"));
			ArrayList<UserData> before = users.getItems();
			ResponseData result = api.navigate(ResponseData.class, users.getLink("create"), new UserUpdateRequest());
			//##END EXAMPLE##
			b = before.size();
			users = api.navigate(UserDataSet.class, data.getLink("list-users"));
			a = users.getItems().size();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		assertEquals(b+1, a);
	}
	
		
	@Test
	public void T14GetProgressTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		
		RoutingProblemUpdateRequest update = problem.toRequest();
		update.setState("Running");
	
		try {
			ResponseData response = api.navigate(ResponseData.class, problem.getLink("toggle-optimization"), update);
			
			Thread.sleep(5000);
			
			//##BEGIN EXAMPLE getprogress 
			problem = api.navigate(RoutingProblemData.class, response.getLocation());
			
			while ( problem.getProgress() < 100 ) {
				Thread.sleep(1000);
				problem = api.navigate(RoutingProblemData.class, problem.getLink("self"));
			}
			//##END EXAMPLE 
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		assertTrue(problem.getProgress() >= 0);
	}
	
	@Test 
	public void T15UpdatingVehicleTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		VehicleData vehicle = TestHelper.getVehicle(api, user, problem);
		ArrayList<TimeWindowData> akkuna = vehicle.getTimeWindows();
		String updatedName = null;
		try {
			VehicleUpdateRequest updatedVehicle = vehicle.toRequest();
			updatedName = "newName";
			updatedVehicle.setName(updatedName);
			ResponseData result = api.navigate(ResponseData.class,  vehicle.getLink("update"), updatedVehicle);
			
			vehicle = api.navigate(VehicleData.class, vehicle.getLink("self"));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		assertEquals(vehicle.getTimeWindows().get(0).getStart(), akkuna.get(0).getStart());
		assertEquals(vehicle.getName(), updatedName);
	}
	
	@Test
	public void T16BadRequestTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		NFleetRequestException e = null;		

		RoutingProblemUpdateRequest problem = new RoutingProblemUpdateRequest("");
		
		try {
			//##BEGIN EXAMPLE badrequest##
			ResponseData result = api.navigate(ResponseData.class, user.getLink("create-problem"), problem);
			System.out.println(result);
			//##END EXAMPLE##
		} catch (NFleetRequestException ex) {
			e = ex;
		} catch (IOException ex) {
			
		}
		assertNotNull(e);
		assertEquals(e.getItems().get(0).getCode(), 3401);
	}
	
	@Test
	public void T17StartingAFinishedOptimization() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		
		RoutingProblemUpdateRequest requ = problem.toRequest();
		requ.setState("Running");
		try {
			ResponseData response = api.navigate(ResponseData.class, problem.getLink("toggle-optimization"), requ);
			problem = api.navigate(RoutingProblemData.class, response.getLocation());
			while (problem.getState().equals("Running")) {
				Thread.sleep(1000);
				System.out.println(problem.getProgress());
				problem = api.navigate(RoutingProblemData.class, response.getLocation());
			}
			
			requ = problem.toRequest();
			requ.setState("Running");
			response = api.navigate(ResponseData.class, problem.getLink("toggle-optimization"), requ);
			problem = api.navigate(RoutingProblemData.class, response.getLocation()); 
			while (problem.getState().equals("Running")) {
				Thread.sleep(1000);
				problem = api.navigate(RoutingProblemData.class, response.getLocation());
				System.out.println(problem.getProgress());
			}		
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		assertEquals(problem.getState(), "Stopped");
	}
	
	@Test
	public void T18CheckingHowMuchFasterIsImport() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		int taskCount = 10;
		RoutingProblemUpdateRequest request = new RoutingProblemUpdateRequest("testProblem");
		try {
			ResponseData result = api.navigate(ResponseData.class, user.getLink("create-problem"), request);
			RoutingProblemData problem = api.navigate(RoutingProblemData.class, result.getLocation());
			TaskSetImportRequest vehicles = new TaskSetImportRequest();
			vehicles.setItems(TestHelper.createListOfTasks(taskCount));
			long begin = System.currentTimeMillis();
			
			result = api.navigate(ResponseData.class, problem.getLink("import-tasks"), vehicles);
			long end = System.currentTimeMillis();
			long total = end-begin;
			System.out.println("Import of "+taskCount+" tasks took " + total + " ms");
			
			begin = System.currentTimeMillis();
			for (TaskUpdateRequest tur : vehicles.getItems()) {
				result = api.navigate(ResponseData.class, problem.getLink("create-task"), tur);
			}
			end = System.currentTimeMillis();
			total = end - begin;
			System.out.println("Individually adding "+taskCount+" tasks took " + total + " ms");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		assertNotNull(request);
	}
	
	
	@Test
	public void T19TestingConcatLink() {
		Link self = new Link("self", "/users/1/problems/3/vehicles/2/events/4", "GET");
		Link parentVehicle = new Link("get-vehicle", "../../",  "GET");
		Link parentTask = new Link("get-task", "../../../../tasks/3" ,"GET");
		
		List<Link> meta = new ArrayList<Link>();
		List<Link> meta2 = new ArrayList<Link>();
		
		meta.add(self); meta.add(parentTask);
		meta2.add(parentVehicle); meta2.add(self);
		
		PlanData d = new PlanData();
		d.setLinks(meta);
		Link vehicle = d.getLink("get-task");
		String rel = vehicle.getUri();
		assertEquals(rel, "/users/1/problems/3/tasks/3");
		
		d.setLinks(meta2);
		Link task = d.getLink("get-vehicle");
		assertEquals(task.getUri(), "/users/1/problems/3/vehicles/2");
	}
	
	@Test
	public void T20GetParentVehicleFromPlanData() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblem(api, user);
		PlanData s = null;
		try {
			//##BEGIN EXAMPLE queryrouteevents##
			PlanData plan = api.navigate(PlanData.class, problem.getLink("plan"));
			//##END EXAMPLE##
			s = plan;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		assertNotNull(s);
	}
	
	@Test
	public void T21VehicleMassImport() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblem(api, user);
		
		CapacityData capa = new CapacityData("Weight", 10);
		ArrayList<CapacityData> list = new ArrayList<CapacityData>();
		list.add(capa);
		
		LocationData start = TestHelper.createLocationWithCoordinates(Location.VEHICLE_START);
		LocationData end = TestHelper.createLocationWithCoordinates(Location.VEHICLE_START);
				
		ResponseData a = null;
		
		Date startTime = new Date();
		Date endTime = new Date();
		endTime.setHours(20);
		
		ArrayList<TimeWindowData> timeWindows = new ArrayList<TimeWindowData>();
		timeWindows.add(new TimeWindowData(startTime, endTime));
 		try {
			//##BEGIN EXAMPLE importvehicleset##
			VehicleSetImportRequest set = new VehicleSetImportRequest();
			List<VehicleUpdateRequest> vehicles = new ArrayList<VehicleUpdateRequest>();
			for (int i = 0; i < 10; i++) {
				VehicleUpdateRequest vehicle = new VehicleUpdateRequest("vehicle" + i, list, start, end);
				vehicle.setTimeWindows(timeWindows);
				vehicles.add(vehicle);
			}
			set.setItems(vehicles);
			ResponseData result = api.navigate(ResponseData.class, problem.getLink("import-vehicles"), set);
			//##END EXAMPLE##
			a = result;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		assertNotNull(a.getLocation());
	}
	
	@Test
	public void T22TaskMassImport() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblem(api, user);
		
		CapacityData capa = new CapacityData("Weight", 10);
		ArrayList<CapacityData> list = new ArrayList<CapacityData>();
		list.add(capa);
	
		LocationData pickupLocation = TestHelper.createLocationWithCoordinates(Location.TASK_PICKUP);
		LocationData deliveryLocation = TestHelper.createLocationWithCoordinates(Location.TASK_DELIVERY);
		ResponseData r = null;
		try {
			//##BEGIN EXAMPLE importtaskset##
			List<TaskUpdateRequest> tasks = new ArrayList<TaskUpdateRequest>();
			
			Date start = new Date();
			Date end = new Date();
			end.setHours(20);
			
			ArrayList<TimeWindowData> timeWindows = new ArrayList<TimeWindowData>();
			timeWindows.add(new TimeWindowData(start, end));
			for (int i = 0; i < 10; i++) {
				List<TaskEventUpdateRequest> taskEvents = new ArrayList<TaskEventUpdateRequest>();
				TaskEventUpdateRequest pickup = new TaskEventUpdateRequest(Type.Pickup, pickupLocation, list);
				pickup.setTimeWindows(timeWindows);
				TaskEventUpdateRequest delivery = new TaskEventUpdateRequest(Type.Delivery, deliveryLocation, list);
				delivery.setTimeWindows(timeWindows);
				taskEvents.add(pickup); taskEvents.add(delivery);
				TaskUpdateRequest task = new TaskUpdateRequest(taskEvents);
				task.setName("kivikasat" + i);
				tasks.add(task);
			}
			TaskSetImportRequest set = new TaskSetImportRequest();
			set.setItems(tasks);
			ResponseData result = api.navigate(ResponseData.class, problem.getLink("import-tasks"), set);
			System.out.println(result.toString());
			//##END EXAMPLE##
			r = result;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		assertNotNull(r.getLocation());
	}
	
	@Test
	public void T23ImportVehiclesAndTasks() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblem(api, user);
		
		VehicleSetImportRequest vehicles = new VehicleSetImportRequest();
		List<VehicleUpdateRequest> vehicleList = new ArrayList<VehicleUpdateRequest>();
		
		for (int i = 0; i < 3; i++) {
			vehicleList.add(TestHelper.createVehicleUpdateRequest("vehicle"+i));
		}
		vehicles.setItems(vehicleList);
		
		TaskSetImportRequest tasks = new TaskSetImportRequest();
		List<TaskUpdateRequest> taskList = TestHelper.createListOfTasks(10);
		tasks.setItems(taskList);
		ImportData r = null;
		try {
			//##BEGIN EXAMPLE importtasksandvehicles##
			ImportRequest importRequest = new ImportRequest();
			importRequest.setVehicles(vehicles);
			importRequest.setTasks(tasks);
			
			ResponseData response = api.navigate(ResponseData.class, problem.getLink("import-data"), importRequest);
			System.out.println(response.getLocation());
			ImportData result = api.navigate(ImportData.class, response.getLocation());
			//##END EXAMPLE##
			r = result;
			
		} catch (Exception e){
			System.out.println(e.toString());
		}
		assertEquals(0, r.getErrorCount());
	}
	
	@Test
	public void T24DifferentCompatibilityTypesTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblem(api, user);
		
		LocationData pi = TestHelper.createLocationWithCoordinates(Location.TASK_PICKUP);
		LocationData de = TestHelper.createLocationWithCoordinates(Location.TASK_DELIVERY);
				
		CapacityData capacity = new CapacityData("Weight", 20);
		List<CapacityData> capacities = new ArrayList<CapacityData>();
		capacities.add(capacity);
		
		TaskEventUpdateRequest pickup = new TaskEventUpdateRequest(Type.Pickup, pi, capacities);
		TaskEventUpdateRequest delivery = new TaskEventUpdateRequest(Type.Delivery, de, capacities);
		
		List<TaskEventUpdateRequest> task1 = new ArrayList<TaskEventUpdateRequest>();
		task1.add(pickup);
		task1.add(delivery);
		TaskUpdateRequest task = new TaskUpdateRequest(task1);
		task.setName("testTask");
		List<String> types = new ArrayList<String>();
		types.add("rekka");
		task.setIncompatibleVehicleTypes(types);
		
		TaskUpdateRequest task2 = new TaskUpdateRequest(task1);
		List<String> other = new ArrayList<String>();
		other.add("henkiloauto");		
		task2.setName("testTask2");
		task2.setIncompatibleVehicleTypes(other);
		
		
		VehicleUpdateRequest vehicle = TestHelper.createVehicleUpdateRequest("Auto");
		vehicle.setVehicleType("rekka");
		List<FieldsItem> items = null;
		
		try {
			ResponseData result = api.navigate(ResponseData.class, problem.getLink("create-vehicle"), vehicle);
			result = api.navigate(ResponseData.class, problem.getLink("create-task"), task);
			result = api.navigate(ResponseData.class, problem.getLink("create-task"), task2);
			
			problem.setState("Running");
			result = api.navigate(ResponseData.class, problem.getLink("toggle-optimization"), problem);
			problem = api.navigate(RoutingProblemData.class, result.getLocation());
			while (problem.getState().equals("Running")) {
				Thread.sleep(1000);
				problem = api.navigate(RoutingProblemData.class, problem.getLink("self"));
			}
			
			 PlanData v = api.navigate(PlanData.class, problem.getLink("plan"));
             
             //Go through the plan.
			 items = v.getItems();
             for (FieldsItem el : v.getItems()) {
             	System.out.println(el);                      
             }
			
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		assertNotNull(items);
	}
	
	@Test
	public void T25ApplyImportTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);				
		RoutingProblemData routingProblemData = TestHelper.createProblem(api, user);
		
		VehicleSetImportRequest vehicles = new VehicleSetImportRequest();
		List<VehicleUpdateRequest> vehicleList = new ArrayList<VehicleUpdateRequest>();
		
		for (int i = 0; i < 3; i++) {
			vehicleList.add(TestHelper.createVehicleUpdateRequest("vehicle"+i));
		}
		vehicles.setItems(vehicleList);
		
		TaskSetImportRequest tasks = new TaskSetImportRequest();
		List<TaskUpdateRequest> taskList = TestHelper.createListOfTasks(10);
		tasks.setItems(taskList);
		VehicleDataSet veh = null;
		TaskDataSet tas = null;
		ResponseData response = null;
		try {
		
			ImportRequest importRequest = new ImportRequest();
			importRequest.setVehicles(vehicles);
			importRequest.setTasks(tasks);
			
			response = api.navigate(ResponseData.class, routingProblemData.getLink("import-data"), importRequest);
			System.out.println(response.getLocation());
			ImportData result = api.navigate(ImportData.class, response.getLocation());
	
			//##BEGIN EXAMPLE applyimport##
			response = api.navigate(ResponseData.class, result.getLink("apply-import"));
			//##END EXAMPLE##
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		assertNotEquals(response, null);
		assertNotEquals(response.getLocation(), null);
	}
	
	@Test
	public void T26TestGeocodingThruAPI() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);				
		RoutingProblemData routingProblemData = TestHelper.createProblem(api, user);
		
		LocationData location = TestHelper.createLocationWithAddress();
		CapacityData capacity = new CapacityData("Weight", 20);
		ArrayList<CapacityData> capacities = new ArrayList<CapacityData>();
		capacities.add(capacity);
		
		VehicleUpdateRequest vehicle = TestHelper.createVehicleUpdateRequest("TestiAuto");
		VehicleData response = null;
		try {
			ResponseData res = api.navigate(ResponseData.class, routingProblemData.getLink("create-vehicle"), vehicle);
			System.out.println(res);
			response = api.navigate(VehicleData.class, res.getLocation());
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		System.out.println(response);
		assertNotEquals(0, response.getEndLocation().getCoordinate().getLatitude());
	}

	@Test
	public void T27TestUpdatingRoutingProblemSettings() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);				
		RoutingProblemData routingProblemData = TestHelper.createProblem(api, user);
		
		RoutingProblemSettingsData before = null;
		RoutingProblemSettingsData after = null;
		try {
			//##BEGIN EXAMPLE changeproblemsettings##
			RoutingProblemSettingsData settings = api.navigate(RoutingProblemSettingsData.class, routingProblemData.getLink("view-settings"));
			RoutingProblemSettingsUpdateRequest updatedSettings = new RoutingProblemSettingsUpdateRequest();
			updatedSettings.setDefaultVehicleSpeedFactor(0.8);
			updatedSettings.setDefaultVehicleSpeedProfile(SpeedProfile.Max120Kmh);
			//##END EXAMPLE##
			ResponseData response = api.navigate(ResponseData.class, settings.getLink("update-settings"), updatedSettings);
			before = settings;
			settings = api.navigate(RoutingProblemSettingsData.class, routingProblemData.getLink("view-settings"));
			after = settings;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		assertNotEquals(before.getDefaultVehicleSpeedProfile(), after.getDefaultVehicleSpeedProfile());
	}
	
	@Test
	public void T28TestRequestingVehicleTypesFromProblem() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);				
		RoutingProblemData routingProblemData = TestHelper.createProblem(api, user);
		ArrayList<String> vehicleTypes = new ArrayList<String>();
		vehicleTypes.add("Rekka");
		vehicleTypes.add("Auto");
		
		ArrayList<String> vehicleTypesFromServer = null;
		try {
			LocationData start = TestHelper.createLocationWithCoordinates(Location.VEHICLE_START);
			LocationData end = TestHelper.createLocationWithCoordinates(Location.TASK_DELIVERY);
						
			VehicleUpdateRequest vehicle1 = TestHelper.createVehicleUpdateRequest(UUID.randomUUID().toString());
			vehicle1.setVehicleType(vehicleTypes.get(0));
						
			VehicleUpdateRequest vehicle2 = TestHelper.createVehicleUpdateRequest(UUID.randomUUID().toString());
			vehicle2.setVehicleType(vehicleTypes.get(1));
			ArrayList<VehicleUpdateRequest> both = new ArrayList<VehicleUpdateRequest>();
			both.add(vehicle1); both.add(vehicle2);
			VehicleSetImportRequest vehicles = new VehicleSetImportRequest();
			vehicles.setItems(both);
			
			ResponseData response = api.navigate(ResponseData.class, routingProblemData.getLink("import-vehicles"), vehicles);
			
			VehicleTypeData vehicleType = api.navigate(VehicleTypeData.class, routingProblemData.getLink("get-types"));
			vehicleTypesFromServer = vehicleType.getVehicleTypes();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		assertEquals(vehicleTypesFromServer.size(), vehicleTypes.size());
		assertEquals(vehicleTypesFromServer.get(0), vehicleTypes.get(0));
		assertEquals(vehicleTypesFromServer.get(1), vehicleTypes.get(1));
	}
	
	@Test
	public void T29TestRequestingTasksAndVehiclesWhileOptimizing() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);				
		RoutingProblemData problem = TestHelper.createProblem(api, user);
		
		VehicleSetImportRequest vehicles = new VehicleSetImportRequest();
		List<VehicleUpdateRequest> vehicleList = new ArrayList<VehicleUpdateRequest>();
		
		for (int i = 0; i < 3; i++) {
			vehicleList.add(TestHelper.createVehicleUpdateRequest("vehicle"+i));
		}
		vehicles.setItems(vehicleList);
		
		TaskSetImportRequest tasks = new TaskSetImportRequest();
		List<TaskUpdateRequest> taskList = TestHelper.createListOfTasks(10);
		tasks.setItems(taskList);
		VehicleDataSet veh = null;
		TaskDataSet tas = null;
		try {
		
			ImportRequest importRequest = new ImportRequest();
			importRequest.setVehicles(vehicles);
			importRequest.setTasks(tasks);
			
			ResponseData response = api.navigate(ResponseData.class, problem.getLink("import-data"), importRequest);
			System.out.println(response.getLocation());
			ImportData result = api.navigate(ImportData.class, response.getLocation());
		
			response = api.navigate(ResponseData.class, result.getLink("apply-import"));
			System.out.println(response);
			problem.setState("Running");
			response = api.navigate(ResponseData.class, problem.getLink("toggle-optimization"), problem.toRequest());
			
			problem = api.navigate(RoutingProblemData.class, response.getLocation());
			
			while (problem.getProgress() < 10 ) {
				Thread.sleep(1500);
				problem = api.navigate(RoutingProblemData.class, problem.getLink("self"));
			}
			
			veh = api.navigate(VehicleDataSet.class, problem.getLink("list-vehicles"));
			tas = api.navigate(TaskDataSet.class, problem.getLink("list-tasks"));
			
		} catch (Exception e){
			System.out.println(e.toString());
		}
		
		assertNotNull(veh.getItems());
		assertNotNull(tas.getItems());
	}
	
	
	@Test
	public void T30TestGettingKPIsThroughtTheAPI() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);				
		RoutingProblemData problem = TestHelper.createProblem(api, user);
		
		PlanData plan = null;
		VehicleSetImportRequest vehicles = new VehicleSetImportRequest();
		List<VehicleUpdateRequest> vehicleList = new ArrayList<VehicleUpdateRequest>();
		
		for (int i = 0; i < 3; i++) {
			vehicleList.add(TestHelper.createVehicleUpdateRequest("vehicle"+i));
		}
		vehicles.setItems(vehicleList);
		
		TaskSetImportRequest tasks = new TaskSetImportRequest();
		List<TaskUpdateRequest> taskList = TestHelper.createListOfTasks(10);
		tasks.setItems(taskList);
				
		try {
			ResponseData response;
			response = api.navigate(ResponseData.class, problem.getLink("import-vehicles"), vehicles);
			response = api.navigate(ResponseData.class, problem.getLink("import-tasks"), tasks);
			
			problem.setState("Running");
			response = api.navigate(ResponseData.class, problem.getLink("toggle-optimization"), problem.toRequest());
									
			problem = api.navigate(RoutingProblemData.class, response.getLocation());
			System.out.println(problem.getState() + " " + problem.getProgress());
			while (problem.getState().equals("Running")) {
				Thread.sleep(1000);
				System.out.println(problem.getProgress());
				problem = api.navigate(RoutingProblemData.class, response.getLocation());
			}
			
			plan = api.navigate(PlanData.class, problem.getLink("plan"));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		assertNotNull(plan.getKPIs());
	}
	
	/*
	@Test
	public void T30LockingTaskEventToVehicle() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);				
		RoutingProblemData problem = TestHelper.createProblem(api, user);
		
		CoordinateData pickupCoord = new CoordinateData();
		CoordinateData deliveryCoord = new CoordinateData();
		pickupCoord.setLatitude(62.244958);
		pickupCoord.setLongitude(25.747143);
		pickupCoord.setSystem(CoordinateSystem.Euclidian);
		
		LocationData pickupLoc = new LocationData();
		pickupLoc.setCoordinatesData(pickupCoord);
		
		deliveryCoord.setLatitude(62.244589);
		deliveryCoord.setLongitude(25.74892);
		deliveryCoord.setSystem(CoordinateSystem.Euclidian);
		
		LocationData deliveryLoc = new LocationData();
		deliveryLoc.setCoordinatesData(deliveryCoord);
		
		TaskUpdateRequest task1 = TestHelper.createTaskUpdateRequest("task1");
		task1.getTaskEvents().get(0).setLocation(pickupLoc);
		task1.getTaskEvents().get(1).setLocation(deliveryLoc);
		
		TaskUpdateRequest task2 = TestHelper.createTaskUpdateRequest("task2");
		task2.getTaskEvents().get(0).setLocation(pickupLoc);
		task2.getTaskEvents().get(1).setLocation(deliveryLoc);
		
		
		VehicleUpdateRequest veh1 = TestHelper.createVehicleUpdateRequest("vehicle1");
		VehicleUpdateRequest veh2 = TestHelper.createVehicleUpdateRequest("vehicle2");
		
		LocationData vehicleLoc = new LocationData();
		vehicleLoc.setCoordinatesData(pickupCoord);
		veh1.setStartLocation(vehicleLoc);
		veh1.setEndLocation(vehicleLoc);
		
		veh2.setStartLocation(vehicleLoc);
		veh2.setEndLocation(vehicleLoc);
		
		VehicleData veh1res = TestHelper.createAndGetVehicle(api, problem, veh1);
		VehicleData veh2res = TestHelper.createAndGetVehicle(api, problem, veh2);
		
		TaskData task1res = TestHelper.createAndGetTask(api, problem, task1);
		TaskData task2res = TestHelper.createAndGetTask(api, problem, task2);
		try {
			api.navigate(RouteData.class, veh1res.getLink("get-route"));
			api.navigate(RouteData.class, veh2res.getLink("get-route"));
			
			RouteUpdateRequest routeReq1 = new RouteUpdateRequest();
			int[] seq1 = { task1res.getTaskEvents().get(0).getId(), task1res.getTaskEvents().get(1).getId() };
			routeReq1.setSequence(seq1);
			RouteUpdateRequest routeReq2 = new RouteUpdateRequest();
			int[] seq2 = { task2res.getTaskEvents().get(0).getId(), task2res.getTaskEvents().get(1).getId() };
			routeReq2.setSequence(seq2);
			
			api.navigate(ResponseData.class, veh1res.getLink("set-route"), routeReq1);
			api.navigate(ResponseData.class, veh2res.getLink("set-route"), routeReq2);
			
		   
		   RouteEventDataSet res1 = api.navigate(RouteEventDataSet.class, veh1res.getLink("list-events"));
		   RouteEventDataSet res2 = api.navigate(RouteEventDataSet.class, veh2res.getLink("list-events"));
		   
		   for (RouteEventData re : res1.getItems() ) {
			   RouteEventData event = api.navigate(RouteEventData.class, re.getLink("self"));
			   if (re.getTaskEventId() < 20000 ) {
				   RouteEventUpdateRequest req = new RouteEventUpdateRequest();
				   req.setState("LockedToVehicle");
				   api.navigate(ResponseData.class, event.getLink("lock-to-vehicle"), req);
			   }
		   }
		   
		   for (RouteEventData re : res2.getItems() ) {
			   RouteEventData event = api.navigate(RouteEventData.class, re.getLink("self"));
			   if (re.getTaskEventId() < 20000 ) {
				   RouteEventUpdateRequest req = new RouteEventUpdateRequest();
				   req.setState("LockedToVehicle");
				   api.navigate(ResponseData.class, event.getLink("lock-to-vehicle"), req);
			   }
		   }
		   
		   RoutingProblemUpdateRequest update = problem.toRequest();
			update.setState("Running");
		
			
		   ResponseData response = api.navigate(ResponseData.class, problem.getLink("toggle-optimization"), update);
				
		   Thread.sleep(5000);
				
		   problem = api.navigate(RoutingProblemData.class, response.getLocation());
				
		   while ( problem.getProgress() < 100 ) {
		       Thread.sleep(1000);
			   problem = api.navigate(RoutingProblemData.class, problem.getLink("self"));
		   }
		   
		   res1 = api.navigate(RouteEventDataSet.class, veh1res.getLink("list-events"));
		   res2 = api.navigate(RouteEventDataSet.class, veh2res.getLink("list-events"));
		   
		   assertEquals(4, res1.getItems().size());
		   assertEquals(4, res2.getItems().size());
		   
			// unlocking task events
		   
		   veh1res = api.navigate(VehicleData.class, veh1res.getLink("self"));
		   veh2res = api.navigate(VehicleData.class, veh2res.getLink("self"));
		   
		   res1 = api.navigate(RouteEventDataSet.class, veh1res.getLink("list-events"));
		   res2 = api.navigate(RouteEventDataSet.class, veh2res.getLink("list-events"));

		   for (RouteEventData re : res1.getItems() ) {
			   RouteEventData event = api.navigate(RouteEventData.class, re.getLink("self"));
			   if (re.getTaskEventId() < 50 ) {
				   RouteEventUpdateRequest req = new RouteEventUpdateRequest();
				   req.setState("UnlockedFromVehicle");
				   api.navigate(ResponseData.class, event.getLink("lock-to-vehicle"), req);
			   }
		   }

		   for (RouteEventData re : res2.getItems() ) {
			   RouteEventData event = api.navigate(RouteEventData.class, re.getLink("self"));
			   if (re.getTaskEventId() < 50) {
				   RouteEventUpdateRequest req = new RouteEventUpdateRequest();
				   req.setState("UnlockedFromVehicle");
				   api.navigate(ResponseData.class, event.getLink("lock-to-vehicle"), req);
			   }
		   }
		   
		   update = problem.toRequest();
		   update.setState("Running");
		
			
		   response = api.navigate(ResponseData.class, problem.getLink("toggle-optimization"), update);
				
		   Thread.sleep(5000);
				
		   problem = api.navigate(RoutingProblemData.class, response.getLocation());
				
		   while ( problem.getProgress() < 100 ) {
		       Thread.sleep(1000);
			   problem = api.navigate(RoutingProblemData.class, problem.getLink("self"));
		   }
		   
		   res1 = api.navigate(RouteEventDataSet.class, veh1res.getLink("list-events"));
		   res2 = api.navigate(RouteEventDataSet.class, veh2res.getLink("list-events"));
		   
		   assertEquals(2, res1.getItems().size());
		   assertEquals(6, res2.getItems().size());
		} catch (Exception e) {
			System.out.println("Something went wrong.");
		}
		
		/*
        api.Navigate<ResponseData>(vehicleResult1.GetLink("set-route"), new RouteUpdateRequest { Items = route1 });
        api.Navigate<ResponseData>(vehicleResult2.GetLink("set-route"), new RouteUpdateRequest { Items = route2 });

        var events1 = api.Navigate<RouteEventDataSet>(vehicleResult1.GetLink("list-events"));
        var events2 = api.Navigate<RouteEventDataSet>(vehicleResult2.GetLink("list-events"));

        foreach (var item in events1.Items)
        {
            var @event = api.Navigate<RouteEventData>(item.GetLink("self"));
            if (@event.TaskEventId < 20000) api.Navigate<ResponseData>(@event.GetLink("lock-to-vehicle"), new RouteEventUpdateRequest
            {
                State = "LockedToVehicle"
            });
        }
        foreach (var item in events2.Items)
        {
            var @event = api.Navigate<RouteEventData>(item.GetLink("self"));
            if (@event.TaskEventId < 20000) api.Navigate<ResponseData>(@event.GetLink("lock-to-vehicle"), new RouteEventUpdateRequest
            {
                State = "LockedToVehicle"
            } );
        }

        problem = api.Navigate<RoutingProblemData>(problem.GetLink("self"));

        var result = api.Navigate<ResponseData>(problem.GetLink("toggle-optimization"), new RoutingProblemUpdateRequest
        {
            Name = problem.Name,
            State = "Running"
        });

        problem = api.Navigate<RoutingProblemData>(problem.GetLink("self"));
        Thread.Sleep(1000);
        while (problem.State.Equals("Running"))
        {
            Thread.Sleep(1000);
            problem = api.Navigate<RoutingProblemData>(problem.GetLink("self"));
        }

        var plan = api.Navigate<PlanData>(problem.GetLink("plan"));
        Assert.AreEqual(4, plan.Items[0].Events.Count);
        Assert.AreEqual(4, plan.Items[1].Events.Count);

        // unlocking

        vehicleResult1 = api.Navigate<VehicleData>(v1.GetLink("self"));
        vehicleResult2 = api.Navigate<VehicleData>(v2.GetLink("self"));

        events1 = api.Navigate<RouteEventDataSet>(vehicleResult1.GetLink("list-events"));
        events2 = api.Navigate<RouteEventDataSet>(vehicleResult2.GetLink("list-events"));

        foreach (var item in events1.Items)
        {
            var @event = api.Navigate<RouteEventData>(item.GetLink("self"));
            if (@event.TaskEventId < 20000) api.Navigate<ResponseData>(@event.GetLink("lock-to-vehicle"), new RouteEventUpdateRequest
            {
                State = "UnlockedFromVehicle"
            });
        }
        foreach (var item in events2.Items)
        {
            var @event = api.Navigate<RouteEventData>(item.GetLink("self"));
            if (@event.TaskEventId < 20000) api.Navigate<ResponseData>(@event.GetLink("lock-to-vehicle"), new RouteEventUpdateRequest
            {
                State = "UnlockedFromVehicle"
            });
        }

        problem = api.Navigate<RoutingProblemData>(problem.GetLink("self"));

        result = api.Navigate<ResponseData>(problem.GetLink("toggle-optimization"), new RoutingProblemUpdateRequest
        {
            Name = problem.Name,
            State = "Running"
        });

        problem = api.Navigate<RoutingProblemData>(problem.GetLink("self"));
        Thread.Sleep(1000);
        while (problem.State.Equals("Running"))
        {
            Thread.Sleep(1000);
            problem = api.Navigate<RoutingProblemData>(problem.GetLink("self"));
        }

        plan = api.Navigate<PlanData>(problem.GetLink("plan"));
        Assert.AreEqual(2, plan.Items[0].Events.Count);
        Assert.AreEqual(6, plan.Items[1].Events.Count);
	}*/
	
} 
