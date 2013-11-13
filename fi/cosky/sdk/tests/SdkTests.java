package fi.cosky.sdk.tests;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fi.cosky.sdk.*;
import fi.cosky.sdk.CoordinateData.CoordinateSystem;

import org.junit.*;

import static org.junit.Assert.*;

public class SdkTests {

	@Test
	public void T00RootLinkTest() {
		API api = TestHelper.authenticate();
		ApiData data = api.navigate(ApiData.class, api.getRoot());
		assertNotNull(data.getLinks());	
	}
	
	@Test
	public void T01CreatingProblemTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		
		//##BEGIN EXAMPLE createProblem##
		RoutingProblemUpdateRequest update = new RoutingProblemUpdateRequest("TestProblem");
		ResultData createdProblem = api.navigate(ResultData.class, user.getLink("create-problem"), update);
		RoutingProblemData problem = api.navigate(RoutingProblemData.class, createdProblem.getLocation());
		//##END EXAMPLE##
		
		assertNotNull(problem);
	}
	
	@Test
	public void T02AccesingProblemTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemUpdateRequest requ = new RoutingProblemUpdateRequest("testproblem");
		ResultData created = api.navigate(ResultData.class, user.getLink("create-problem"), requ);
		
		//##BEGIN EXAMPLE accessingProblem##
		RoutingProblemData problem = api.navigate(RoutingProblemData.class, created.getLocation());
		//##END EXAMPLE##
		
		assertEquals(requ.getName(), problem.getName());
	}
	
	@Test
	public void T03ListingTasksTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		
		//##BEGIN EXAMPLE listingTasks##
		TaskDataSet tasks = api.navigate(TaskDataSet.class, problem.getLink("list-tasks"));
		//##END EXAMPLEe##
		
		assertNotNull(tasks);
	}
	
	@Test
	public void T04CreatingTaskTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		
		//##BEGIN EXAMPLE creatingTask##		      
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

        ResultData result = api.navigate(ResultData.class, problem.getLink("create-task"), task); 
		//##END EXAMPLE##
		
        TaskData asdf = api.navigate(TaskData.class, result.getLocation());
        assertEquals(asdf.getName(), task.getName());
	}
	
	@Test
	public void T05UpdatingTaskTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		TaskData oldTask = TestHelper.getTask(api, problem);
		List<TaskEventUpdateRequest> events = new ArrayList<TaskEventUpdateRequest>();
		
		for (TaskEventData d : oldTask.getTaskEvents()) {
			TaskEventUpdateRequest asdf = new TaskEventUpdateRequest(d.getType(), d.getLocation(), d.getCapacities());
			asdf.setLocation(d.getLocation());
			asdf.setServiceTime(d.getServiceTime());
			asdf.setTaskEventId(d.getId());
			events.add(asdf);
		}
		
		//##BEGIN EXAMPLE updateTask##
		TaskUpdateRequest task = new TaskUpdateRequest(events);
		task.setName("newName");
		task.setVersionNumber(oldTask.getVersionNumber());
		ResultData newTaskLocation = api.navigate(ResultData.class, oldTask.getLink("update"), task);
		//##END EXAMPLE##
		
		oldTask = api.navigate(TaskData.class, oldTask.getLink("self"));
		assertEquals(oldTask.getName(), task.getName());
	}
	
	@Test
	public void T06DeletingTaskTest() {
		
	}
	
	@Test
	public void T07ListingVehiclesTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		
		//##BEGIN EXAMPLE listingVehicles##
		VehicleDataSet vehicles = api.navigate(VehicleDataSet.class, problem.getLink("list-vehicles"));
		//##END EXAMPLE##
		assertNotNull(vehicles.getItems());
	}
	
	@Test
	public void T08AccessingTaskSeqTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		TaskData oldTask = TestHelper.getTask(api, problem);
		VehicleData vehicle = TestHelper.getVehicle(api, user, problem);
		
		RouteUpdateRequest route = new RouteUpdateRequest();
		int[] sd = {11,12};
		route.setClientId(user.getClientId());
		route.setProblemId(problem.getId());
		route.setUserId(user.getId());
		route.setSequence(sd);
		
		
		api.navigate(ResultData.class, vehicle.getLink("set-route"), route);
		
		//##BEGIN EXAMPLE accessingRoute##
		TaskEventDataSet events = api.navigate(TaskEventDataSet.class, vehicle.getLink("list-events"));
		//##END EXAMPLE##
		
		assertNotNull(events);
	}
	
	@Test
	public void T09AccessingRouteTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		TaskData oldTask = TestHelper.getTask(api, problem);
		VehicleData vehicle = TestHelper.getVehicle(api, user, problem);
		
		RouteUpdateRequest route = new RouteUpdateRequest();
		int[] sequence = {11,12};
		route.setClientId(user.getClientId());
		route.setProblemId(problem.getId());
		route.setUserId(user.getId());
		route.setSequence(sequence);
		
		
		api.navigate(ResultData.class, vehicle.getLink("set-route"), route);
		
		//##BEGIN EXAMPLE accessingRoute##
		RouteData routeData = api.navigate(RouteData.class, vehicle.getLink("get-route"));
		//##END EXAMPLE##
		
		assertNotNull(routeData);
	}
	
	@Test
	public void T10UpdatingRouteTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		TaskData oldTask = TestHelper.getTask(api, problem);
		VehicleData vehicle = TestHelper.getVehicle(api, user, problem);
		
		//##BEGIN EXAMPLE updatingRoute##
		RouteUpdateRequest route = new RouteUpdateRequest();
		int[] sequence = {11 , 12, 21, 22};		
		route.setSequence(sequence);
		api.navigate(ResultData.class, vehicle.getLink("set-route"), route);
		//##END EXAMPLE##
		
		RouteData routeData = api.navigate(RouteData.class, vehicle.getLink("get-route"));
		assertArrayEquals(routeData.getItems(), sequence);
	}
	
	@Test
	public void T11StartingOptimizationTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		problem = api.navigate(RoutingProblemData.class, problem.getLink("self"));
		
		//##BEGIN EXAMPLE startingOptimization##
		RoutingProblemUpdateRequest update = problem.toRequest();
		update.setState("Running");
		ResultData result = api.navigate(ResultData.class, problem.getLink("toggle-optimization"), update);
		//##END EXAMPLE##
		
		assertNotNull(result);;
	}
	
	@Test
	public void T12StoppingOptimizationTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		problem = api.navigate(RoutingProblemData.class, problem.getLink("self"));
		
		RoutingProblemUpdateRequest update = problem.toRequest();
		update.setState("Running");
		ResultData result = api.navigate(ResultData.class, problem.getLink("toggle-optimization"), update);
		
		//##BEGIN EXAMPLE startingOptimization##
		RoutingProblemUpdateRequest updateRequest = problem.toRequest();
		update.setState("Stopped");
		result = api.navigate(ResultData.class, problem.getLink("toggle-optimization"), update);
		//##END EXAMPLE##
		
		assertNotNull(result);
	}
		
}
