import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import fi.cosky.sdk.*;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

/**
 * Example program to demonstrate how to use the CO-SKY RESTful API
 * For reporting bugs in the SDK, please contact dev-support (at) co-sky.fi
 */
public class UsingCoSkyAPIv2 {

    @SuppressWarnings("deprecation")
	public static void main (String[] args) {
        API api = new API("");
        boolean success = api.authenticate("", "");


        if (success) {
            ApiData data = api.navigate(ApiData.class, api.getRoot());
            System.out.println(data);
            
            ResponseData datas = api.navigate(ResponseData.class, data.getLink("create-user"), new UserData());
            System.out.println(datas);
            UserData user = api.navigate(UserData.class, datas.getLocation());
            
            EntityLinkCollection users = api.navigate(EntityLinkCollection.class, data.getLink("list-users"));
            
            for (EntityLink u : users.getItems()) {
            	System.out.println(u);
            }
                   
            
            if (user != null) {
            
                EntityLinkCollection problems = api.navigate(EntityLinkCollection.class, user.getLink("list-problems"));
                	
                RoutingProblemUpdateRequest newProblem = new RoutingProblemUpdateRequest("exampleProblem");
                ResponseData result = api.navigate(ResponseData.class, user.getLink("create-problem"), newProblem );
                System.out.println(result);
                problems = api.navigate(EntityLinkCollection.class, user.getLink("list-problems"));
                RoutingProblemData problem1 = api.navigate(RoutingProblemData.class, problems.getItems().get(0).getLink("self"));
                
                System.out.println(problem1);
                CoordinateData coordinateData = new CoordinateData();

                //coordinateData.setLatitude(62.244588);
                //coordinateData.setLongitude(25.742683);

                //Saksa
                coordinateData.setLatitude(54.130888);
                coordinateData.setLongitude(12.00938);
                coordinateData.setSystem(CoordinateData.CoordinateSystem.WGS84);
                LocationData locationData = new LocationData();
                locationData.setCoordinatesData(coordinateData);

                CoordinateData pickup = new CoordinateData();


                //pickup.setLatitude(62.247906);
                //pickup.setLongitude(25.867395);

                //Saksa
                pickup.setLatitude(54.14454);
                pickup.setLongitude(12.108808);
                pickup.setSystem(CoordinateData.CoordinateSystem.WGS84);
                LocationData pickupLocation = new LocationData();
                pickupLocation.setCoordinatesData(pickup);

                CoordinateData delivery = new CoordinateData();

                //delivery.setLatitude(61.386909);
                //delivery.setLongitude(24.654106);

                //Saksa
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

                result = api.navigate(ResponseData.class, problem1.getLink("create-vehicle"), vehicleRequest);

                EntityLinkCollection vehicles = api.navigate(EntityLinkCollection.class, problem1.getLink("list-vehicles"));

                for( EntityLink zdf : vehicles.getItems()) {
                    System.out.println(zdf);
                }

                ArrayList<CapacityData> taskCapacity = new ArrayList<CapacityData>();
                taskCapacity.add(new CapacityData("Weight", 1));

                for (int i = 0 ; i < 4; i++) {
                    ArrayList<TaskEventUpdateRequest> taskEvents = new ArrayList<TaskEventUpdateRequest>();
                    taskEvents.add(new TaskEventUpdateRequest(Type.Pickup, pickupLocation, taskCapacity));
                    taskEvents.add(new TaskEventUpdateRequest(Type.Delivery, deliveryLocation, taskCapacity));
                    TaskUpdateRequest task = new TaskUpdateRequest(taskEvents);
                    task.setName("testTask");
                    taskEvents.get(0).setTimeWindows(timeWindows);
                    taskEvents.get(1).setTimeWindows(timeWindows);
                    taskEvents.get(0).setServiceTime(10);
                    taskEvents.get(1).setServiceTime(10);

                    result = api.navigate(ResponseData.class, problem1.getLink("create-task"), task);

                }

                ArrayList<TaskEventData> taskEvents = new ArrayList<TaskEventData>();
                taskEvents.add(new TaskEventData(Type.Pickup, pickupLocation, taskCapacity));
                taskEvents.add(new TaskEventData(Type.Delivery, deliveryLocation, taskCapacity));

                if (result.getItems() != null) {
                    System.out.println(result.getItems());
                }

                TaskDataSet taskData = api.navigate(TaskDataSet.class, problem1.getLink("list-tasks") );
                for (TaskData td : taskData.getItems()) {
                    System.out.println(td);
                }

                problem1 = api.navigate(RoutingProblemData.class, problem1.getLink("self"));
                System.out.println(problem1);
                //starting optimization
                problem1.setState("Running");
                result = api.navigate(ResponseData.class, problem1.getLink("toggle-optimization"), problem1.toRequest());
                System.out.println(result);
                //stopping optimization would be the same but set state to "Stopped"
                ObjectiveValueDataSet objectiveValues = null;
                
                HashMap<String, String> qp = new HashMap<String, String>();
                qp.put("start", "0");
                qp.put("end", "10");
                while (true) {
                    try {
                        Thread.sleep(1500);
                        problem1 = api.navigate(RoutingProblemData.class, problem1.getLink("self"));
                        System.out.println("Optimization is " + problem1.getState() +" at percentage " + problem1.getProgress());
                        objectiveValues = api.navigate(ObjectiveValueDataSet.class, problem1.getLink("objective-values"), qp);

                        if (objectiveValues != null && !objectiveValues.getItems().isEmpty()) {
                            for (ObjectiveValueData item : objectiveValues.getItems())    {
                                System.out.println( "Objective values from " + qp.get("start") + " to " + qp.get("end") + ": [" + item.getTimeStamp() + "] " + item.getValue() );
                            }
                        }

                        if (problem1.getState().equals("Stopped")) break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                EntityLinkCollection v = api.navigate(EntityLinkCollection.class, problem1.getLink("list-vehicles"));

                //Gets the routeevent
                for (EntityLink el : v.getItems()) {
                	VehicleData vd = api.navigate(VehicleData.class, el.getLink("self"));
                    System.out.println("Vehicles route: ");
                    RouteEventDataSet routeEvents = api.navigate(RouteEventDataSet.class, vd.getLink("list-events"));
                    for (RouteEventData red : routeEvents.getItems()) {
                    	System.out.println(red);
                    }
                }
                //Tasks do not contain the information about their task events anymore.
            }
        } else {
            System.out.println("Credentials were wrong, or the service is unavailable");
        }

    }


}
