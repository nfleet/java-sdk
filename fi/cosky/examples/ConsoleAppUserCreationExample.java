package fi.cosky.examples;

import java.io.Console;

import fi.cosky.sdk.API;
import fi.cosky.sdk.ApiData;
import fi.cosky.sdk.AppService;
import fi.cosky.sdk.AppUserData;
import fi.cosky.sdk.AppUserDataSet;
import fi.cosky.sdk.AppUserUpdateRequest;
import fi.cosky.sdk.ResponseData;
import fi.cosky.sdk.UserData;

public class ConsoleAppUserCreationExample {
	private static Console console;
	private static API api;
	private static AppService app;
	private static String appServiceUrl = "https://test-appservice.nfleet.fi";
	private static String apiUrl = "https://test-api.nfleet.fi";
	private static String appUrl = "https://test-app.nfleet.fi";
	private static String clientKey = "";   //TODO: use your API credentials here
	private static String clientSecret = "";

		
	public static void main(String[] args) {
		try {
			console = System.console();
			
			api = new API(apiUrl);
			boolean success = api.authenticate(clientKey, clientSecret);
			
			if (!success){
				console.printf("Failed to login to API \n");
				return;
			} else 
				console.printf("Login success to API \n");
			
			
			ApiData apiData = api.navigate(ApiData.class, api.getRoot());
			UserData user = new UserData();
			
			ResponseData createdUser = api.navigate(ResponseData.class, apiData.getLink("create-user"), user);
			user = api.navigate(UserData.class, createdUser.getLocation());
			
			console.printf("Created user to API with id %d \n", user.getId()); //TODO: Store id and username to database
			
			app = new AppService(appServiceUrl, appUrl, clientKey, clientSecret);
					
			AppUserDataSet users = app.Root;
			AppUserUpdateRequest req = new AppUserUpdateRequest();
			req.setEmail("somsadfe@thing.ccom"); //TODO: replace these with proper values
			req.setPassword("password");     //Note that App does not allow the same username or email on multiple users.
			req.setUsername("user2");
			req.setId(user.getId()); // this should be the same id as the one that has been created 
			
			AppUserData appuser = null;
			
			ResponseData response = app.navigate(ResponseData.class, users.getLink("create-user"), req);
			appuser = app.navigate(AppUserData.class, response.getLocation());
			console.printf("Created user to app with id %d \n", appuser.getId() );
		
		

		} catch (Exception e) {
			console.printf(e.toString());
		}
	}
}
