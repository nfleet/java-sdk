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
		
		//##BEGIN EXAMPLE creatingproblem##
		RoutingProblemUpdateRequest update = new RoutingProblemUpdateRequest("TestProblem");
		ResponseData createdProblem = api.navigate(ResponseData.class, user.getLink("create-problem"), update);
		RoutingProblemData problem = api.navigate(RoutingProblemData.class, createdProblem.getLocation());
		//##END EXAMPLE##
		
		assertNotNull(problem);
	}
	
	@Test
	public void T02AccesingProblemTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemUpdateRequest requ = new RoutingProblemUpdateRequest("testproblem");
		ResponseData created = api.navigate(ResponseData.class, user.getLink("create-problem"), requ);
		
		//##BEGIN EXAMPLE accessingproblem##
		RoutingProblemData problem = api.navigate(RoutingProblemData.class, created.getLocation());
		//##END EXAMPLE##
		
		assertEquals(requ.getName(), problem.getName());
	}
	
	@Test
	public void T03ListingTasksTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		
		//##BEGIN EXAMPLE listingtasks##
		TaskDataSet tasks = api.navigate(TaskDataSet.class, problem.getLink("list-tasks"));
		//##END EXAMPLE##
		
		assertNotNull(tasks);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void T04CreatingTaskTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		
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
		
        TaskData asdf = api.navigate(TaskData.class, result.getLocation());
        assertEquals(asdf.getName(), task.getName());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void T05UpdatingTaskTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		TaskData oldTask = TestHelper.getTask(api, problem);
		List<TaskEventUpdateRequest> events = new ArrayList<TaskEventUpdateRequest>();
		
				
		//##BEGIN EXAMPLE updatingtask##
		TaskUpdateRequest task = oldTask.toRequest();
		task.setName("abbaasdf");
		ResponseData newTaskLocation = api.navigate(ResponseData.class, oldTask.getLink("update"), task);
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
		
		//##BEGIN EXAMPLE listingvehicles##
		VehicleDataSet vehicles = api.navigate(VehicleDataSet.class, problem.getLink("list-vehicles"));
		//##END EXAMPLE##
		assertNotNull(vehicles.getItems());
	}
	
	@Test
	public void T08AccessingTaskSeqTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
				VehicleData vehicle = TestHelper.getVehicle(api, user, problem);
		
		RouteUpdateRequest route = new RouteUpdateRequest();
		int[] sd = {11,12};
		route.setClientId(user.getClientId());
		route.setProblemId(problem.getId());
		route.setUserId(user.getId());
		route.setSequence(sd);
		
		
		api.navigate(ResponseData.class, vehicle.getLink("set-route"), route);
		
		//##BEGIN EXAMPLE accessingtaskseq##
		TaskEventDataSet events = api.navigate(TaskEventDataSet.class, vehicle.getLink("list-events"));
		//##END EXAMPLE##
		
		assertNotNull(events);
	}
	
	@Test
	public void T09AccessingRouteTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		VehicleData vehicle = TestHelper.getVehicle(api, user, problem);
		
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
		
		assertNotNull(routeData);
	}
	
	@Test
	public void T10UpdatingRouteTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		VehicleData vehicle = TestHelper.getVehicle(api, user, problem);
		
		//##BEGIN EXAMPLE updatingroute##
		RouteUpdateRequest route = new RouteUpdateRequest();
		int[] sequence = {11 , 12, 21, 22};		
		route.setSequence(sequence);
		ResponseData asdf = api.navigate(ResponseData.class, vehicle.getLink("set-route"), route);
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
		
		//##BEGIN EXAMPLE startingopt##
		RoutingProblemUpdateRequest update = problem.toRequest();
		update.setState("Running");
		ResponseData result = api.navigate(ResponseData.class, problem.getLink("toggle-optimization"), update);
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
		ResponseData result = api.navigate(ResponseData.class, problem.getLink("toggle-optimization"), update);
		
		//##BEGIN EXAMPLE stoppingopt##
		RoutingProblemUpdateRequest updateRequest = problem.toRequest();
		updateRequest.setState("Stopped");
		result = api.navigate(ResponseData.class, problem.getLink("toggle-optimization"), updateRequest);
		//##END EXAMPLE##
		
		assertNotNull(result);
	}
	
	@Test
	public void T13CreatingUserTest() {
		API api = TestHelper.authenticate();
		
		ApiData data = api.navigate(ApiData.class, api.getRoot());
		
		//##BEGIN EXAMPLE creatingauser##
		UserDataSet users = api.navigate(UserDataSet.class, data.getLink("list-users"));
		ArrayList<UserData> before = users.getItems();
		System.out.println(before);
		ResponseData result = api.navigate(ResponseData.class, users.getLink("create"), new UserUpdateRequest());
		System.out.println(result);
		//##END EXAMPLE##
		
		users = api.navigate(UserDataSet.class, data.getLink("list-users"));
		System.out.println(users.getItems().size());
		assertEquals(before.size()+1, users.getItems().size());
	}
	
		
	@Test
	public void T14GetProgressTest() {
		
	}
	
	@Test 
	public void T15UpdatingVehicleTest() {
		API api = TestHelper.authenticate();
		UserData user = TestHelper.getOrCreateUser(api);
		RoutingProblemData problem = TestHelper.createProblemWithDemoData(api, user);
		VehicleData vehicle = TestHelper.getVehicle(api, user, problem);
		
		VehicleUpdateRequest updatedVehicle = vehicle.toRequest();
		updatedVehicle.setName("asdfasdfasdf");
		ResponseData result = api.navigate(ResponseData.class,  vehicle.getLink("update"), updatedVehicle);
		
		vehicle = api.navigate(VehicleData.class, vehicle.getLink("self"));
		assertEquals(vehicle.getName(), updatedVehicle.getName());
	}
	
}
