package fi.cosky.sdk.tests;
import java.util.ArrayList;
import java.util.List;

import fi.cosky.sdk.*;
import fi.cosky.sdk.CoordinateData.CoordinateSystem;

public class TestHelper {
	
	static API authenticate() {
		String url = "http://localhost:81";
		String clientKey = "testi";
		String clientSecret = "user";
		API api = new API(url);
		api.authenticate(clientKey, clientSecret);
		return api;
	}
	
	static UserData getOrCreateUser( API api ) {
		ApiData apiData = api.navigate(ApiData.class, api.getRoot());
		UserDataSet users = api.navigate(UserDataSet.class, apiData.getLink("list-users"));
		UserData user = null;
		if ( users.getItems() != null && users.getItems().size() < 1) {
			user = new UserData();
			ResponseData createdUser = api.navigate(ResponseData.class, apiData.getLink("create-user"), user);
			user = api.navigate(UserData.class, createdUser.getLocation());
		} else {
			for (UserData u : users.getItems()) {
				if (u.getId() == 1) user = u;
			}
		}
		return user;
	}
	
	static RoutingProblemData createProblem(API api, UserData user) {
		RoutingProblemData problem = new RoutingProblemData("exampleProblem");
		ResponseData created = api.navigate(ResponseData.class, user.getLink("create-problem"), problem);
		problem = api.navigate(RoutingProblemData.class, created.getLocation());
		return problem;
	}
	
	static RoutingProblemData createProblemWithDemoData(API api, UserData user) {
		RoutingProblemData problem = createProblem(api, user);
		TestData.CreateDemoData(problem, api);
		return problem;
	}
	
	static VehicleData getVehicle(API api, UserData user, RoutingProblemData problem) {
		TestData.CreateDemoData(problem, api);
		VehicleDataSet vehicles = api.navigate(VehicleDataSet.class, problem.getLink("list-vehicles"));
		VehicleData vehicle = null;
		for (VehicleData v : vehicles.getItems()) {
			if (vehicle ==  null) vehicle = v;
		}
		return vehicle;
	}
	
	static TaskData getTask(API api, RoutingProblemData problem) {
				
		CoordinateData pickup = new CoordinateData();
		pickup.setLatitude(62.244958);
		pickup.setLongitude(25.747143);
		pickup.setSystem(CoordinateSystem.Euclidian);
		LocationData pi = new LocationData();
		pi.setCoordinatesData(pickup);
		
		
		CoordinateData delivery = new CoordinateData();
		delivery.setLatitude(62.244589);
		delivery.setLongitude(25.74892);
		delivery.setSystem(CoordinateSystem.Euclidian);
		LocationData de = new LocationData();
		de.setCoordinatesData(delivery);
		
		CapacityData capacity = new CapacityData("Weight", 20);
		List<CapacityData> capacities = new ArrayList<CapacityData>();
		capacities.add(capacity);
		
		TaskEventUpdateRequest task1 = new TaskEventUpdateRequest(Type.Pickup, pi, capacities);
		TaskEventUpdateRequest task2 = new TaskEventUpdateRequest(Type.Delivery, de, capacities);
		
		List<TaskEventUpdateRequest> both = new ArrayList<TaskEventUpdateRequest>();
		both.add(task1);
		both.add(task2);
		TaskUpdateRequest task = new TaskUpdateRequest(both);
		task.setName("testTask");
		ResponseData createdTask = api.navigate(ResponseData.class, problem.getLink("create-task"), task);
		TaskData td = api.navigate(TaskData.class, createdTask.getLocation());
		return td;
	}
	
}
