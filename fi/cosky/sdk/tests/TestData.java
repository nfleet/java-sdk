package fi.cosky.sdk.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import fi.cosky.sdk.*;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
import fi.cosky.sdk.tests.TestHelper.Location;

public class TestData {
	
	@SuppressWarnings("deprecation")
	public static void CreateDemoData(RoutingProblemData problem, API api) {
        VehicleUpdateRequest vehicleRequest = TestHelper.createVehicleUpdateRequest(UUID.randomUUID().toString());

        try {
        	api.navigate(ResponseData.class, problem.getLink("create-vehicle"), vehicleRequest);
        	
        	List<TaskUpdateRequest> tasks = TestHelper.createListOfTasks(1);
            api.navigate(ResponseData.class, problem.getLink("create-task"), tasks.get(0));
        } catch (NFleetRequestException e) {
        	System.out.println("Something went wrong");
        } catch (IOException e) {
        	System.out.println();
        }
	}
}
