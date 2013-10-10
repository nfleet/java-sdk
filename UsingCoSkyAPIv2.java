import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**
 * Example program to demonstrate how to use the CO-SKY RESTful API
 * For reporting bugs in the SDK, please contact jarkko.p.laitinen (at) jyu.fi
 */
public class UsingCoSkyAPIv2 {

    public static void main (String[] args) {
        API api = new API("http://localhost:81");
        boolean success = api.authenticate("", "");


        if (success) {
            ApiData data = api.navigate(ApiData.class, api.getRoot());

            UserDataSet users = api.navigate(UserDataSet.class, data.getLink("list-users"));
            UserData user = null;
            for (UserData u : users.getItems()) {
                if (u.getId() == 1) {
                    user = u;
                    break;
                }
            }
            if (user != null) {
                ResultData result = api.navigate(ResultData.class, user.getLink("list-problems"));

                RoutingProblemUpdateRequest newProblem = new RoutingProblemUpdateRequest("exampleProblem");
                result = api.navigate(ResultData.class, user.getLink("create-problem"), newProblem );

                RoutingProblemDataSet problems = api.navigate(RoutingProblemDataSet.class, user.getLink("list-problems"));
                RoutingProblemData problem1 = problems.getItems().get(problems.getItems().size()-1);

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

                result = api.navigate(ResultData.class, problem1.getLink("create-vehicle"), vehicleRequest);

                VehicleDataSet d = api.navigate(VehicleDataSet.class, problem1.getLink("list-vehicles"));

                for( VehicleData zdf : d.getItems()) {
                    System.out.println(zdf);
                }

                ArrayList<CapacityData> taskCapacity = new ArrayList<CapacityData>();
                capacities.add(new CapacityData("Weight", 1));

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

                    result = api.navigate(ResultData.class, problem1.getLink("create-task"), task);

                }

                ArrayList<TaskEventData> taskEvents = new ArrayList<TaskEventData>();
                taskEvents.add(new TaskEventData(Type.Pickup, pickupLocation, taskCapacity));
                taskEvents.add(new TaskEventData(Type.Delivery, deliveryLocation, taskCapacity));

                ErrorData errorData;
                if (result.getItems() != null) {
                    System.out.println(result.getItems());
                }

                TaskDataSet taskData = api.navigate(TaskDataSet.class, problem1.getLink("list-tasks") );
                for (TaskData td : taskData.getItems()) {
                    System.out.println(td);
                }

                problem1 = api.navigate(RoutingProblemData.class, problem1.getLink("self"));
                //starting optimization
                problem1.setState("Running");
                result = api.navigate(ResultData.class, problem1.getLink("toggle-optimization"), problem1.toRequest());
                System.out.println(result);
                //stopping optimization would be the same but set state to "Stopped"
                ObjectiveValueDataSet objectiveValues = null;

                // this is how query parameters can be used
                /*HashMap<String,String> qp = new HashMap<String, String>();
                qp.put("start","0");
                qp.put("end","1");*/
                while (true) {
                    try {
                        Thread.sleep(1500);
                        problem1 = api.navigate(RoutingProblemData.class, problem1.getLink("self"));
                        System.out.println("Optimization is " + problem1.getState() +" at percentage " + problem1.getProgress());
                        objectiveValues = api.navigate(ObjectiveValueDataSet.class, problem1.getLink("objective-values"));

                        // if you want to use query parameters
                        //objectiveValues = api.navigate(ObjectiveValueDataSet.class, problem1.getLink("objective-values"),qp);

                        if (objectiveValues != null && !objectiveValues.getItems().isEmpty()) {
                            for (ObjectiveValueData item : objectiveValues.getItems())    {
                                System.out.println( "Objective values : ["+item.getTimeStamp()+"] "+item.getValue());
                            }
                        }
                        //in the future one can ask the objective values using the following command.
                        //ObjectiveValueDataSet objectiveValueDataSet = api.navigate(ObjectiveValueDataSet.class, problem1.getLink("objective-values"));

                        if (problem1.getState().equals("Stopped")) break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                VehicleDataSet vehicleDataSet = api.navigate(VehicleDataSet.class, problem1.getLink("list-vehicles"));

                for (VehicleData vd : vehicleDataSet.getItems()) {
                    System.out.println("Vehicles route " + vd.getRoute());
                    TaskEventDataSet route = api.navigate(TaskEventDataSet.class, vd.getLink("list-events"));
                    for ( TaskEventData ted : route.getItems()) {
                        System.out.println(ted);
                    }
                }



                //Get list of tasks from the optimization, can view the plannedArrival and departure times.
                TaskDataSet taskDataSet = api.navigate(TaskDataSet.class, problem1.getLink("list-tasks"));
                for(TaskData td : taskDataSet.getItems()) {
                    System.out.println("Task " + td);
                    TaskEventData previous = null;
                    for (TaskEventData current : td.getTaskEvents()) {
                        System.out.println("-TaskEvent " + " type " + current.getType() + " " + current.getPlannedArrivalTime() + " " + current.getPlannedDepartureTime() + " " + current.getServiceTime());
                    }
                }
            }
        } else {
            System.out.println("Credentials were wrong, or the service is unavailable");
        }

    }


}
