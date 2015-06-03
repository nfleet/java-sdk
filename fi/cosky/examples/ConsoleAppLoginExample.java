package fi.cosky.examples;

import java.awt.Desktop;
import java.io.Console;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fi.cosky.sdk.*;
import fi.cosky.sdk.CoordinateData.CoordinateSystem;

public class ConsoleAppLoginExample {
	private static boolean loggedIn = false;
	private static Console console;
	private static AppService app;
	private static API api;
	private static String username;
	private static String appServiceUrl = "https://test-appservice.nfleet.fi";
	private static String apiUrl = "https://test-api.nfleet.fi";
	private static String appUrl = "https://test-app.nfleet.fi";
	private static String clientKey = "";   //TODO: use your credentials here
	private static String clientSecret = "";
	
	public static void main(String[] args) throws Exception {
		console = System.console();
		
		api = new API(apiUrl);
		boolean success = api.authenticate(clientKey, clientSecret);
		
		if (!success){
			console.printf("Failed to login to API");
			return;
		}
		
		app = new AppService(appServiceUrl, appUrl, clientKey, clientSecret);
		LoginResult userResult = LoginResult.Failure;
		
        if ( ( loggedIn || ( !loggedIn && ( userResult = Login() ) == LoginResult.Success ) ) && ( app.HasValidToken() || app.Login() ) )
        {
        	 String url = ExportToAPI( "example" );
             if ( url != null ){
            	 String appUrl = app.MakeAppUrl( url );
            	 if (appUrl != null) {
	        		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    		    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			        	URI uri = new URI(appUrl);
			            desktop.browse(uri);
			        } else {
			        	console.printf("Could not open browser");
			        }
            	 }
             }
                 
         }
         else if ( userResult != LoginResult.Canceled )
         {
             console.printf( "Could not send data to NFleet. Username or password does not match." );
         }
        
	}

        
    private static LoginResult Login() {
    	console.printf("Please enter your username: ");
    	username = console.readLine();

    	if (username.length() < 1) return LoginResult.Canceled;
    	
    	console.printf("Please enter your password: ");
    	String password = new String(console.readPassword());
    	try {
    		boolean success = app.Login(username, password);
    		return success ? LoginResult.Success : LoginResult.Failure;
    				
    	} catch (IOException e) {
    		console.printf(e.toString() + "\n");
    		return LoginResult.Failure;
    	}
    }
    
    private static String ExportToAPI(String caseName) throws Exception {
    	
    	int id = 4; //TODO: get id from database based on username 
    	
    	ApiData data = api.navigate(ApiData.class, api.getRoot());
    	UserDataSet users = api.navigate(UserDataSet.class, data.getLink("list-users"));
    	
    	UserData user = null;
    	
    	for (UserData u : users.getItems()) {
    		if (u.getId() == id) {
    			user = u;
    			break;
    		}
    	}
    	    	
    	if (user == null) {
    		console.printf("Trouble with user" );
			return null;
    	}
    	
    	user = api.navigate(UserData.class, user.getLink("self"));
    	
    	RoutingProblemData problem = new RoutingProblemData(caseName);
		ResponseData created = api.navigate(ResponseData.class, user.getLink("create-problem"), problem);
		problem = api.navigate(RoutingProblemData.class, created.getLocation());
		
		ArrayList<CapacityData> capacities = new ArrayList<CapacityData>();
        capacities.add(new CapacityData("Weight", 100000));
        ArrayList<TimeWindowData> timeWindows = new ArrayList<TimeWindowData>();
        
        Calendar calendar = Calendar.getInstance();
		
		calendar.set(Calendar.HOUR_OF_DAY, 7);
		Date startD = calendar.getTime();
				
		calendar.set(Calendar.HOUR_OF_DAY, 20);
		Date endD = calendar.getTime();
				
		TimeWindowData twd = new TimeWindowData(startD, endD);
		
        timeWindows.add(twd);
                
        CoordinateData coordinates = new CoordinateData();
					
        coordinates.setLatitude(62.247906);
        coordinates.setLongitude(25.867395);
	    coordinates.setSystem(CoordinateSystem.WGS84);
	    
		LocationData locationData = new LocationData();
		locationData.setCoordinatesData(coordinates);
        VehicleUpdateRequest vehicleRequest = new VehicleUpdateRequest("Vehicle", capacities, locationData, locationData);
		vehicleRequest.setVehicleSpeedProfile( SpeedProfile.Max80Kmh.toString() );
		vehicleRequest.setVehicleSpeedFactor(0.7);
        vehicleRequest.setTimeWindows(timeWindows);
		vehicleRequest.setRelocationType("None");
        CoordinateData de = new CoordinateData();
		
        de.setLatitude(62.347906);
        de.setLongitude(25.267395);
	    de.setSystem(CoordinateSystem.WGS84);
	    
		LocationData des = new LocationData();
		des.setCoordinatesData(de);
		CapacityData capacity = new CapacityData("Weight", 20);
		List<CapacityData> taskcapacities = new ArrayList<CapacityData>();
		taskcapacities.add(capacity);
		TaskEventUpdateRequest pickup = new TaskEventUpdateRequest(Type.Pickup, locationData, taskcapacities);
		TaskEventUpdateRequest delivery = new TaskEventUpdateRequest(Type.Delivery, des, taskcapacities);
				
		pickup.setTimeWindows(timeWindows);
		delivery.setTimeWindows(timeWindows);
		List<TaskEventUpdateRequest> both = new ArrayList<TaskEventUpdateRequest>();
		both.add(pickup);
		both.add(delivery);
		TaskUpdateRequest task = new TaskUpdateRequest(both);
		task.setName("testTask");
		task.setRelocationType("None");
		task.setActivityState("Active");
        
		ArrayList<TaskUpdateRequest> tasks = new ArrayList<TaskUpdateRequest>();
		tasks.add(task);
		TaskSetImportRequest taskImport = new TaskSetImportRequest();
		taskImport.setItems(tasks);
		
		ArrayList<VehicleUpdateRequest> vehicles = new ArrayList<VehicleUpdateRequest>();
		vehicles.add(vehicleRequest);
		VehicleSetImportRequest vehicleImport = new VehicleSetImportRequest();
		vehicleImport.setItems(vehicles);
		
				
		ImportRequest importRequest = new ImportRequest();
		importRequest.setTasks(taskImport);
		importRequest.setVehicles(vehicleImport);
		
		ResponseData response = api.navigate(ResponseData.class, problem.getLink("import-data"), importRequest);

		ImportData result = api.navigate(ImportData.class, response.getLocation());
		
		if (result.getErrorCount() > 0 ) {
			console.printf("Trouble with import" );
			return null;
		}
		
		response = api.navigate(ResponseData.class, result.getLink("apply-import"));

		problem = api.navigate(RoutingProblemData.class, problem.getLink("self"));

		while (problem.getDataState().equals("Pending")) {
			System.out.println("Waiting for initialization");
			Thread.sleep(1000);
			problem = api.navigate(RoutingProblemData.class, problem.getLink("self"));
		}
		
		return problem.getLink("self").getUri();
    }
	
}


enum LoginResult {
	Success,
	Failure,
	Canceled
}