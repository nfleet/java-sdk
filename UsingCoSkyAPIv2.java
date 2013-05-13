import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Jarkko Laitinen
 * Date: 21.3.2013
 * Time: 9:21
 * To change this template use File | Settings | File Templates.
 */
public class UsingCoSkyAPIv2 {

    public static void main (String[] args) {
        API api = new API("https://api.co-sky.fi");
        boolean success = api.authenticate("username", "secret");

        if (success) {

            ApiData data = api.navigate(ApiData.class, api.getRoot());
            System.out.println(data);

            ResultData result  = api.navigate(ResultData.class, data.getLink("list-problems"));

            System.out.println(result);

            ProblemData newProblem = new ProblemData("example");

            result = api.navigate(ResultData.class, data.getLink("create-problem"), newProblem );
            System.out.println(result);

            ProblemData problemData = api.navigate(ProblemData.class, result.getLocation());
            System.out.println(problemData);



            ProblemDataSet problems = api.navigate(ProblemDataSet.class, data.getLink("list-problems"));

            ProblemData problem1 = problems.getItems().get(0);

            CoordinateData coordinateData = new CoordinateData();
            //Jossain keskustassa
            coordinateData.setLatitude(62.244588);
            coordinateData.setLongitude(25.742683);
            coordinateData.setSystem(CoordinateData.CoordinateSystem.WGS84);
            LocationData locationData = new LocationData();
            locationData.setCoordinatesData(coordinateData);

            CoordinateData pickup = new CoordinateData();
            //Vaajakoski
            pickup.setLatitude(62.247906);
            pickup.setLongitude(25.867395);
            pickup.setSystem(CoordinateData.CoordinateSystem.WGS84);
            LocationData pickupLocation = new LocationData();
            pickupLocation.setCoordinatesData(pickup);

            CoordinateData delivery = new CoordinateData();
            //Tikkakoskella
            delivery.setLatitude(62.386909);
            delivery.setLongitude(25.654106);
            delivery.setSystem(CoordinateData.CoordinateSystem.WGS84);
            LocationData deliveryLocation = new LocationData();
            deliveryLocation.setCoordinatesData(delivery);

            ArrayList<CapacityData> capacities = new ArrayList<CapacityData>();
            capacities.add(new CapacityData("Weight", 10));

            VehicleData vehicleData = new VehicleData("demoVehicle",capacities, locationData, locationData);

            result = api.navigate(ResultData.class, problem1.getLink("create-vehicle"), vehicleData);

            VehicleDataSetResult d = api.navigate(VehicleDataSetResult.class, problem1.getLink("list-vehicles"));

            for( VehicleDataResult zdf : d.getItems()) {
                System.out.println(zdf);
            }


            ArrayList<TaskEventData> taskEvents = new ArrayList<TaskEventData>();
            taskEvents.add(new TaskEventData(Type.Pickup, pickupLocation, capacities));
            taskEvents.add(new TaskEventData(Type.Delivery, deliveryLocation, capacities));
            TaskData task = new TaskData(taskEvents);
            task.setName("testTask");

            ArrayList<TimeWindowData> timeWindows = new ArrayList<TimeWindowData>();
            Date morning = new Date();
            morning.setHours(7);
            Date evening = new Date();
            evening.setHours(16);
            timeWindows.add(new TimeWindowData(morning, evening));

            taskEvents.get(0).setTimeWindows(timeWindows);
            taskEvents.get(1).setTimeWindows(timeWindows);

            result = api.navigate(ResultData.class, problem1.getLink("create-task"), task);
            ErrorData errorData;
            if (result.getData() != null) {
                System.out.println(result.getData());
            }

            TaskDataSet taskData = api.navigate(TaskDataSet.class, problem1.getLink("list-tasks") );
            for (TaskData td : taskData.getItems()) {
                System.out.println(td);
            }

            problem1 = api.navigate(ProblemData.class, problem1.getLink("self"));
            System.out.println(problem1);
            result = api.navigate(ResultData.class, problem1.getLink("start-new-optimization"));
            System.out.println(result);

            OptimizationData optData = api.navigate(OptimizationData.class, result.getLocation());
                      /*
            while (true) {
                try {
                    Thread.sleep(500);
                    optData = api.navigate(OptimizationData.class, optData.getLink("self"));
                    if (optData.getState().equals("Stopped")) break;
                    System.out.println(optData);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (VehicleData vd : optData.getResults().getItems()) {
                System.out.println("Vehicles route " + vd.getRoute());
            }
                        */
        }  else {
            System.out.println("Credentials were wrong, or the service is unavailable");
        }
    }
}
