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
            API api = new API("https://test-api.nfleet.fi");
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
			
			//##BEGIN EXAMPLE accessingnewproblem##
			RoutingProblemData problem = api.navigate(RoutingProblemData.class, created.getLocation());
			//##END EXAMPLE##

            //##BEGIN EXAMPLE accessingproblem##
            RoutingProblemData p = api.navigate(RoutingProblemData.class, created.getLocation());
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
            task.setActivityState("Active");
            
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
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		TaskData oldTask = TestHelper.getTask(api, problem);
		TaskUpdateRequest asdf = null;
		try {
		
			TaskDataSet tasks = api.navigate(TaskDataSet.class, problem.getLink("list-tasks"));
			int taskCount = tasks.getItems().size();
			assertEquals(2, taskCount);
			//##BEGIN EXAMPLE deletingtask##	
			ResponseData response = api.navigate(ResponseData.class, oldTask.getLink("delete"));
			//##END EXAMPLE##
			tasks = api.navigate(TaskDataSet.class, problem.getLink("list-tasks"));
			VehicleDataSet vehicles = api.navigate(VehicleDataSet.class, problem.getLink("list-vehicles"));
			
			assertEquals(1,tasks.getItems().size());

						
		} catch (Exception e) {
			System.out.println(e.toString());
		}
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
			int[] sequence = {11, 12};		
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
            //##BEGIN EXAMPLE getprogress##
			ResponseData response = api.navigate(ResponseData.class, problem.getLink("toggle-optimization"), update);
			
			Thread.sleep(5000);
			

			problem = api.navigate(RoutingProblemData.class, response.getLocation());
			
			while ( problem.getProgress() < 100 ) {
				Thread.sleep(1000);
				problem = api.navigate(RoutingProblemData.class, problem.getLink("self"));
			}
			//##END EXAMPLE##
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
		Link self = new Link("self", "/users/1/problems/3/vehicles/2/events/4","", "GET");
		Link parentVehicle = new Link("get-vehicle", "../../","application/vnd.jyu.nfleet.vehicle-2.1+json",  "GET");
		Link parentTask = new Link("get-task", "../../../../tasks/3" ,"application/vnd.jyu.nfleet.task-2.1+json","GET");
		
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
		
		TimeWindowData tw = TestHelper.createTimeWindow(5, 20);
		ArrayList<TimeWindowData> timeWindows = new ArrayList<TimeWindowData>();
		timeWindows.add(tw);
 		try {
			//##BEGIN EXAMPLE importvehicleset##
			VehicleSetImportRequest set = new VehicleSetImportRequest();
			List<VehicleUpdateRequest> vehicles = new ArrayList<VehicleUpdateRequest>();
			for (int i = 0; i < 10; i++) {
				VehicleUpdateRequest vehicle = new VehicleUpdateRequest("vehicle" + i, list, start, end);
				vehicle.setTimeWindows(timeWindows);
				vehicle.setRelocationType("None");
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

			List<TaskUpdateRequest> tasks = TestHelper.createListOfTasks(10);
			
			//##BEGIN EXAMPLE importtaskset##
			TaskSetImportRequest set = new TaskSetImportRequest();
			set.setItems(tasks);
			ResponseData result = api.navigate(ResponseData.class, problem.getLink("import-tasks"), set);
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
            //##BEGIN EXAMPLE getimportresults##
            ImportData imp = api.navigate(ImportData.class, response.getLocation());
            //##END EXAMPLE##
			
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
				
		TimeWindowData tw = TestHelper.createTimeWindow(7, 20);
		List<TimeWindowData> tws = new ArrayList<TimeWindowData>();
		tws.add(tw);
		pickup.setTimeWindows(tws);
		delivery.setTimeWindows(tws);
		
		List<TaskEventUpdateRequest> task1 = new ArrayList<TaskEventUpdateRequest>();
		task1.add(pickup);
		task1.add(delivery);
		TaskUpdateRequest task = new TaskUpdateRequest(task1);
		task.setActivityState("Active");
		task.setRelocationType("None");
		task.setName("testTask");
		List<String> types = new ArrayList<String>();
		types.add("rekka");
		task.setIncompatibleVehicleTypes(types);
		
		TaskUpdateRequest task2 = new TaskUpdateRequest(task1);
		task2.setRelocationType("None");
		task2.setActivityState("Active");
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
			//##BEGIN EXAMPLE updatingroutingproblemsettings##
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
	
	@Test
	public void T31LockingTaskEventToVehicle() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);				
		RoutingProblemData problem = TestHelper.createProblem(api, user);
	
		VehicleUpdateRequest veh1 = TestHelper.createVehicleUpdateRequest("vehicle1");
		VehicleUpdateRequest veh2 = TestHelper.createVehicleUpdateRequest("vehicle2");
		VehicleData veh1res = TestHelper.createAndGetVehicle(api, problem, veh1);
		VehicleData veh2res = TestHelper.createAndGetVehicle(api, problem, veh2);
		
		TaskUpdateRequest task1 = TestHelper.createTaskUpdateRequest("task1");
		TaskUpdateRequest task2 = TestHelper.createTaskUpdateRequest("task2");
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
				   req.setState("Locked");
				   api.navigate(ResponseData.class, event.getLink("lock-to-vehicle"), req);
			   }
		   }
		   
		   for (RouteEventData re : res2.getItems() ) {
			   RouteEventData event = api.navigate(RouteEventData.class, re.getLink("self"));
			   if (re.getTaskEventId() < 20000 ) {
				   RouteEventUpdateRequest req = new RouteEventUpdateRequest();
				   req.setState("Locked");
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
				   req.setState("Unlocked");
				   api.navigate(ResponseData.class, event.getLink("unlock"), req);
			   }
		   }

		   for (RouteEventData re : res2.getItems() ) {
			   RouteEventData event = api.navigate(RouteEventData.class, re.getLink("self"));
			   if (re.getTaskEventId() < 50) {
				   RouteEventUpdateRequest req = new RouteEventUpdateRequest();
				   req.setState("Unlocked");
				   api.navigate(ResponseData.class, event.getLink("unlock"), req);
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
		   		   
		   assertTrue(res1.getItems().size() > 0);
		   assertTrue(res2.getItems().size() > 0);
		   		   
		} catch (Exception e) {
			System.out.println("Something went wrong.");
		}
		
		
	}
	/*
	@Test
	public void T32UsingCompatibilitysWithTasks() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);				
		RoutingProblemData problem = TestHelper.createProblem(api, user);
		
		List<VehicleUpdateRequest> vehicleList = new ArrayList<VehicleUpdateRequest>();
		
		for (int i = 0; i < 3; i++) {
			VehicleUpdateRequest vehicle = TestHelper.createVehicleUpdateRequest("vehicle"+i);
			vehicle.setVehicleType("auto" + i);
			System.out.println(vehicle.getVehicleType());
			vehicleList.add(vehicle);
			
		}
				
		TaskSetImportRequest tasks = new TaskSetImportRequest();
		
		List<TaskUpdateRequest> taskList = TestHelper.createListOfTasks(10);
		tasks.setItems(taskList);
		
		ArrayList<String> vehicle1 = new ArrayList<String>();
		vehicle1.add("auto1");
		
		ArrayList<String> vehicle2 = new ArrayList<String>();
		vehicle2.add("auto2");
		
		ArrayList<String> vehicle3 = new ArrayList<String>();
		vehicle3.add("auto0");
				
		for ( int i = 0; i < taskList.size(); i++) {
			if (i == 0) 
				taskList.get(i).setCompatibleVehicleTypes(vehicle3);
			if (i % 2 == 0) {
				taskList.get(i).setCompatibleVehicleTypes(vehicle1);
			} else {
				taskList.get(i).setCompatibleVehicleTypes(vehicle2);
			}
		}
		
		VehicleData veh1res = TestHelper.createAndGetVehicle(api, problem, vehicleList.get(0));
		VehicleData veh2res = TestHelper.createAndGetVehicle(api, problem, vehicleList.get(1));
		VehicleData veh3res = TestHelper.createAndGetVehicle(api, problem, vehicleList.get(2));
				
		RouteEventDataSet res1 = null;
		RouteEventDataSet res2 = null;
		RouteEventDataSet res3 = null;
		try {
		
			TaskSetImportRequest taskImport = new TaskSetImportRequest();			
			taskImport.setItems(taskList);
			
			ResponseData response = api.navigate(ResponseData.class, problem.getLink("import-tasks"), taskImport);
			
			problem.setState("Running");
			response = api.navigate(ResponseData.class, problem.getLink("toggle-optimization"), problem.toRequest());
			
			problem = api.navigate(RoutingProblemData.class, response.getLocation());
			System.out.println(problem);
			while (problem.getProgress() < 100 ) {
				Thread.sleep(1500);
				problem = api.navigate(RoutingProblemData.class, problem.getLink("self"));
			}
							
			res1 = api.navigate(RouteEventDataSet.class, veh1res.getLink("list-events"));
			res2 = api.navigate(RouteEventDataSet.class, veh2res.getLink("list-events"));
			res3 = api.navigate(RouteEventDataSet.class, veh3res.getLink("list-events"));
		} catch (Exception e){
			System.out.println(e.toString());
		}
		
		assertEquals(2, res1.getItems().size());
		assertEquals(12, res2.getItems().size());
		assertEquals(12, res3.getItems().size());
	}
	*/
	@Test
	public void T33LockingDrivenRoute() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);				
		RoutingProblemData problem = TestHelper.createProblem(api, user);
		
		List<VehicleUpdateRequest> vehicleList = new ArrayList<VehicleUpdateRequest>();
		
		for (int i = 0; i < 2; i++) {
			VehicleUpdateRequest vehicle = TestHelper.createVehicleUpdateRequest("vehicle"+i);
			vehicleList.add(vehicle);
			
		}
				
		TaskSetImportRequest tasks = new TaskSetImportRequest();
		
		List<TaskUpdateRequest> taskList = TestHelper.createListOfTasks(2);
		tasks.setItems(taskList);
		
		
		VehicleData veh1res = TestHelper.createAndGetVehicle(api, problem, vehicleList.get(0));
		VehicleData veh2res = TestHelper.createAndGetVehicle(api, problem, vehicleList.get(1));
		
		TaskData task1res = TestHelper.createAndGetTask(api, problem, taskList.get(0));		
		TaskData task2res = TestHelper.createAndGetTask(api, problem, taskList.get(1));
		
		RouteEventDataSet res1 = null;
		RouteEventDataSet res2 = null;
			
		try {
				
		    RouteUpdateRequest route = new RouteUpdateRequest();
		    route.setItems(new int[]{11,12});
		    
		    RouteUpdateRequest route2 = new RouteUpdateRequest();
		    route2.setItems(new int[]{21,22});
		    
		    api.navigate(RouteData.class, veh1res.getLink("get-route"));
		    api.navigate(RouteData.class, veh2res.getLink("get-route"));
		    
			ResponseData response = api.navigate(ResponseData.class, veh1res.getLink("set-route"), route);
			response = api.navigate(ResponseData.class, veh2res.getLink("set-route"), route2);
			
			res1 = api.navigate(RouteEventDataSet.class, veh1res.getLink("list-events"));
			res2 = api.navigate(RouteEventDataSet.class, veh2res.getLink("list-events"));
					
			//Locking
			RouteEventUpdateRequest routeUpdate = new RouteEventUpdateRequest();
			routeUpdate.setState("Locked");
			
			api.navigate(ResponseData.class, res1.getItems().get(res1.getItems().size()-2).getLink("lock"), routeUpdate);
			api.navigate(ResponseData.class, res2.getItems().get(res2.getItems().size()-2).getLink("lock"), routeUpdate);
			
			problem.setState("Running");
			response = api.navigate(ResponseData.class, problem.getLink("toggle-optimization"), problem.toRequest());
			
			problem = api.navigate(RoutingProblemData.class, response.getLocation());
			
			while (problem.getProgress() < 100 ) {
				Thread.sleep(1500);
				problem = api.navigate(RoutingProblemData.class, problem.getLink("self"));
			}
							
			res1 = api.navigate(RouteEventDataSet.class, veh1res.getLink("list-events"));
			res2 = api.navigate(RouteEventDataSet.class, veh2res.getLink("list-events"));
			
			assertEquals(4, res1.getItems().size());
			assertEquals(4, res2.getItems().size());
						
			//unlocking,
			routeUpdate.setState("Unlocked");
			//unlocking the first event after the vehicle start, unlocks every event after it.
			api.navigate(ResponseData.class, res1.getItems().get(1).getLink("unlock"), routeUpdate);
			api.navigate(ResponseData.class, res2.getItems().get(1).getLink("unlock"), routeUpdate);
			
			problem.setState("Running");
			response = api.navigate(ResponseData.class, problem.getLink("toggle-optimization"), problem.toRequest());
			
			problem = api.navigate(RoutingProblemData.class, response.getLocation());
			
			while (problem.getProgress() < 100 ) {
				Thread.sleep(1500);
				problem = api.navigate(RoutingProblemData.class, problem.getLink("self"));
			}
			
			res1 = api.navigate(RouteEventDataSet.class, veh1res.getLink("list-events"));
			res2 = api.navigate(RouteEventDataSet.class, veh2res.getLink("list-events"));
			
			assertTrue(res1.getItems().size() > 0);
			assertTrue(res2.getItems().size() > 0);
						
		} catch (Exception e){
			System.out.println(e.toString());
		}
	}
	
	@Test
	public void T34DeletingAllTaskTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		TaskData oldTask = TestHelper.getTask(api, problem);
		TaskUpdateRequest asdf = null;
		try {

			List<TaskUpdateRequest> tasks = TestHelper.createListOfTasks(10);
			TaskSetImportRequest imports = new TaskSetImportRequest();
			imports.setItems(tasks);
			
			api.navigate(ResponseData.class, problem.getLink("import-tasks"), imports);
						
			TaskDataSet taskSet = api.navigate(TaskDataSet.class, problem.getLink("list-tasks"));
			int taskCount = taskSet.getItems().size();
			assertEquals(12, taskCount);
			//##BEGIN EXAMPLE deletingtasks##			
			ResponseData response = api.navigate(ResponseData.class, problem.getLink("delete-tasks"));
			//##END EXAMPLE##
			taskSet = api.navigate(TaskDataSet.class, problem.getLink("list-tasks"));
			VehicleDataSet vehicles = api.navigate(VehicleDataSet.class, problem.getLink("list-vehicles"));
			
			assertEquals(0,taskSet.getItems().size());

						
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	@Test
	public void T35DeletingVehicleTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		VehicleData vehicle = TestHelper.createAndGetVehicle(api, problem, TestHelper.createVehicleUpdateRequest(UUID.randomUUID().toString()));
		int vehicleCountBefore;
		int vehicleCountAfter;
		
		try {
			VehicleDataSet vehicles = api.navigate(VehicleDataSet.class, problem.getLink("list-vehicles"));
			vehicleCountBefore = vehicles.getItems().size();
			
			api.navigate(ResponseData.class, vehicle.getLink("delete"));
			
			vehicles = api.navigate(VehicleDataSet.class, problem.getLink("list-vehicles"));
			vehicleCountAfter = vehicles.getItems().size();
			
			assertEquals(2, vehicleCountBefore);
			assertEquals(1, vehicleCountAfter);
		} catch (Exception e) {
			
		}
	}
	
	@Test
	public void T36DeletingAllVehiclesTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		VehicleData vehicle = TestHelper.createAndGetVehicle(api, problem, TestHelper.createVehicleUpdateRequest(UUID.randomUUID().toString()));
		int vehicleCountBefore = -1;
		int vehicleCountAfter = -1;
		
		try {
			VehicleDataSet vehicles = api.navigate(VehicleDataSet.class, problem.getLink("list-vehicles"));
			vehicleCountBefore = vehicles.getItems().size();
			
			api.navigate(ResponseData.class, problem.getLink("delete-vehicles"));
			
			vehicles = api.navigate(VehicleDataSet.class, problem.getLink("list-vehicles"));
			vehicleCountAfter = vehicles.getItems().size();
			
			assertEquals(2, vehicleCountBefore);
			assertEquals(0, vehicleCountAfter);
		} catch (Exception e) {
			
		}
	}
	/*
	@Test
	public void T37TestingTest() {
		//test creating a case, optimizing it, changing the location of a vehicle, then optimizing again.
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		VehicleData vehicle = TestHelper.createAndGetVehicle(api, problem, TestHelper.createVehicleUpdateRequest(UUID.randomUUID().toString()));
		try {
			problem = api.navigate(RoutingProblemData.class, problem.getLink("self"));
			RoutingProblemUpdateRequest updated = problem.toRequest();
			updated.setState("Running");
			ResponseData data = api.navigate(ResponseData.class, problem.getLink("toggle-optimization"), updated);
			problem = api.navigate(RoutingProblemData.class, data.getLocation());
			
			while( problem.getProgress() <= 100) {
				System.out.println(problem.getState() + " " + problem.getProgress());
				Thread.sleep(1500);
				problem = api.navigate(RoutingProblemData.class, problem.getLink("self"));
				if (!problem.getState().equals("Running")) break;
			}
			
			LocationData newStart = TestHelper.createLocationWithCoordinates(Location.VEHICLE_END);
			
			VehicleDataSet vehicles = api.navigate(VehicleDataSet.class, problem.getLink("list-vehicles"));
			
			VehicleData asdf = api.navigate(VehicleData.class, vehicles.getItems().get(0).getLink("self"));
			
			VehicleUpdateRequest vehicleToUpdate = asdf.toRequest();  
			
			vehicleToUpdate.setStartLocation(newStart);
			
			data = api.navigate(ResponseData.class, asdf.getLink("update"), vehicleToUpdate);
			
			System.out.println(data);
			
			problem = api.navigate(RoutingProblemData.class, problem.getLink("self"));
			System.out.println(problem);
			
			updated = problem.toRequest();
			updated.setState("Running");
			
			data = api.navigate(ResponseData.class, problem.getLink("toggle-optimization"), updated);
			
			problem = api.navigate(RoutingProblemData.class, data.getLocation());	
			
			while( problem.getState().equals("Running")) {
				Thread.sleep(1500);
				problem = api.navigate(RoutingProblemData.class, problem.getLink("self"));
			}
			
			vehicles = api.navigate(VehicleDataSet.class, problem.getLink("list-vehicles"));
			for (VehicleData veh : vehicles.getItems()) {
				System.out.println(veh);
			}
			
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	*/
	@Test
	public void T38GettingVehicleWithCustomLink() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		VehicleData vehicle = TestHelper.createAndGetVehicle(api, problem, TestHelper.createVehicleUpdateRequest(UUID.randomUUID().toString()));
		VehicleData vehicle2 = null;
		try {
			vehicle2 = api.navigate(VehicleData.class, new Link("location", vehicle.getLink("self").getUri(), "GET"));
			assertNotNull(vehicle2);
		} catch (Exception e) {
			
		}		
	}
	
	@Test
	public void T39CreatingUserWithLimits() {
		API api = TestHelper.authenticate();
		ApiData apidata = null;
		UserData user = null;		
		UserUpdateRequest newuser = null;
		int limit = 10;
		try {
			apidata = api.navigate(ApiData.class, api.getRoot());
			newuser = new UserUpdateRequest();
			newuser.setDepotLimit(limit);
			newuser.setOptimizationQueueLimit(limit);
			newuser.setVehicleLimit(limit);
			newuser.setTaskLimit(limit);
			newuser.setProblemLimit(limit);
			
			ResponseData response = api.navigate(ResponseData.class, apidata.getLink("create-user"), newuser);
			
			user = api.navigate(UserData.class, response.getLocation());
		} catch (Exception e) {
			
		}
		assertEquals(user.getDepotLimit(), newuser.getDepotLimit());
	}
	
	@Test
	public void T40UpdatingRoutingProblemSettings() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		RoutingProblemSettingsData settings = null;
		RoutingProblemSettingsData settingsAfterUpdate = null;
		RoutingProblemSettingsUpdateRequest update = null;
		try {
			settings = api.navigate(RoutingProblemSettingsData.class, problem.getLink("view-settings"));
			update = new RoutingProblemSettingsUpdateRequest();
			update.setAlgorithmTree(settings.getAlgorithmTree());
			update.setDateTimeFormatString(settings.getDateTimeFormatString());
			update.setDefaultVehicleSpeedFactor(settings.getDefaultVehicleSpeedFactor());
			update.setDefaultVehicleSpeedProfile(SpeedProfile.Max80Kmh);
			update.setInsertionAggressiveness(1);
			
			api.navigate(ResponseData.class, settings.getLink("update-settings"), update);
		
			settingsAfterUpdate = api.navigate(RoutingProblemSettingsData.class, problem.getLink("view-settings"));
		} catch (Exception e) {
			
		}
		assertNotNull(settings);
		assertNotNull(settingsAfterUpdate);
		assertEquals(update.getInsertionAggressiveness(), settingsAfterUpdate.getInsertionAggressiveness(), 0.001);
	}

    @Test
    public void T41CreateDepot() {
        API api = TestHelper.authenticate();
        UserData user = TestHelper.getOrCreateUser(api);
        RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);

        LocationData location = new LocationData();
        location.setCoordinatesData(new CoordinateData( 0.0, 0.0, CoordinateSystem.Euclidian ));

        ArrayList<CapacityData> capacities = new ArrayList<CapacityData>();
        capacities.add(new CapacityData("weight", 10));
        capacities.add(new CapacityData("volume", 30));

        try {
            //##BEGIN EXAMPLE createdepot##
            DepotUpdateRequest request = new DepotUpdateRequest();
            request.setLocation(location);
            request.setCapacities(capacities);
            request.setName("Depot01");
            request.setType("SomeType");
            request.setInfo1("Info");

            ResponseData response = api.navigate(ResponseData.class, problem.getLink("create-depot"), request);
            DepotData depot = api.navigate(DepotData.class, response.getLocation());
            //##END EXAMPLE##

            assertEquals(request.getName(), depot.getName());
        } catch (Exception e) {

        }
    }
} 
