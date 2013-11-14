package fi.cosky.sdk;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

/**
 * Implementation of csv-reader. Reads .csv files that are in a certain format. Is used to automate
 * test case initialization in internal co-sky projects.
 */
public class CsvReader {

    public ArrayList<VehicleData> readVehicles(String path) {
        ArrayList<VehicleData> vehicles = null;
        BufferedReader br = null;
        try {
            vehicles = new ArrayList<VehicleData>();
            br = new BufferedReader(new FileReader(path));
            String line;
            VehicleData vehicle = null;
            while ((line = br.readLine()) != null) {
                if (line.length() > 0) {
                    vehicle = parseVehicle(line);
                    if (vehicle == null) {
                        System.out.println("Troubles with line " + line);
                    }
                    vehicles.add(parseVehicle(line));
                }
            }
            br.close();
        } catch (Exception e) {
            return null;
            
        }
        
        return vehicles;
    }

    public ArrayList<TaskData> readTasks(String path) {
        ArrayList<TaskData> tasks = null;
        BufferedReader br = null;
        try {
            System.out.println("Starting to read tasks");
            tasks = new ArrayList<TaskData>();
            br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.length() > 0) {
                    TaskData data = parseTask(line);
                    if (data == null) {
                        System.out.println("Troubles with line " + line);
                        return null;
                    }
                    tasks.add(data);
                }
            }
            br.close();
        } catch (Exception e) {
            return null;
        } finally {
        	try {
				br.close();
			} catch (IOException e){
				e.printStackTrace();
			}
        }
        return tasks;
    }

    private TaskData parseTask(String line) {
       // DateFormat df = DateFormat.getDateInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.M.yyyy HH:mm");
        TaskData task = null;
        try {
            StringTokenizer st = new StringTokenizer(line, ";");
            String name = st.nextToken();
            String size = st.nextToken();
            String startLong = st.nextToken();
            String startLat = st.nextToken();
            String serviceTime = st.nextToken();
            String twStart1 = st.nextToken();
            String twStart2 = st.nextToken();
            String endLong = st.nextToken();
            String endLat = st.nextToken();
            String endSt = st.nextToken();
            String twEnd1 = st.nextToken();
            String twEnd2 = st.nextToken();

            TimeWindowData start = new TimeWindowData(formatter.parse(twStart1), formatter.parse(twStart2));
            List<TimeWindowData> startTime = new ArrayList<TimeWindowData>();
            startTime.add(start);

            TimeWindowData end = new TimeWindowData(formatter.parse(twEnd1), formatter.parse(twEnd2));
            List<TimeWindowData> endT = new ArrayList<TimeWindowData>();
            endT.add(end);
            CoordinateData startL = new CoordinateData();
            startL.setLongitude(Double.parseDouble(startLong));
            startL.setLatitude(Double.parseDouble(startLat));
            startL.setSystem(CoordinateData.CoordinateSystem.WGS84);

            CoordinateData endL = new CoordinateData();
            endL.setLongitude(Double.parseDouble(endLong));
            endL.setLatitude(Double.parseDouble(endLat));
            endL.setSystem(CoordinateData.CoordinateSystem.WGS84);

            LocationData locationData1 = new LocationData();
            locationData1.setCoordinatesData(startL);
            LocationData locationData = new LocationData();
            locationData.setCoordinatesData(endL);

            CapacityData capacityData = new CapacityData("Weight", Integer.parseInt(size));
            ArrayList<CapacityData> capacities = new ArrayList<CapacityData>();
            capacities.add(capacityData);

            TaskEventData pickup = new TaskEventData(Type.Pickup, locationData1, capacities );
            pickup.setServiceTime(Integer.parseInt(serviceTime)*60);
            pickup.setTimeWindows(startTime);
            TaskEventData delivery = new TaskEventData(Type.Delivery, locationData, capacities);
            delivery.setServiceTime(Integer.parseInt(endSt));
            delivery.setTimeWindows(endT);
            ArrayList<TaskEventData> events = new ArrayList<TaskEventData>();
            events.add(pickup); events.add(delivery);
            task = new TaskData(events);
            task.setName(name);
        } catch (Exception e) {
            return null;
        }
        return task;
    }


    private VehicleData parseVehicle(String line) {
        //DateFormat df = DateFormat.getDateInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.M.yyyy HH:mm");
        StringTokenizer st = new StringTokenizer(line, ";");
        VehicleData vehicle = null;
        try {
            String name = st.nextToken();
            String capacity = st.nextToken();
            String startLong = st.nextToken();
            String startLat = st.nextToken();
            String endLong = st.nextToken();
            String endLat = st.nextToken();
            String start = st.nextToken();
            String end = st.nextToken();

            TimeWindowData twd = new TimeWindowData(formatter.parse(start), formatter.parse(end));
            ArrayList<TimeWindowData> timeWindows = new ArrayList<TimeWindowData>();
            timeWindows.add(twd);

            CoordinateData startloc = new CoordinateData();
            startloc.setLatitude(Double.parseDouble(startLat));
            startloc.setLongitude(Double.parseDouble(startLong));
            startloc.setSystem(CoordinateData.CoordinateSystem.WGS84);

            CoordinateData endloc = new CoordinateData();
            endloc.setLongitude(Double.parseDouble(endLong));
            endloc.setLatitude(Double.parseDouble(endLat));
            endloc.setSystem(CoordinateData.CoordinateSystem.WGS84);

            CapacityData cap = new CapacityData("Weight", Integer.parseInt(capacity));
            ArrayList<CapacityData> capacieties = new ArrayList<CapacityData>() ;
            capacieties.add(cap);
            LocationData data = new LocationData();
            data.setCoordinatesData(startloc);

            LocationData data2 = new LocationData();
            data2.setCoordinatesData(endloc);

            ArrayList<LocationData> locations = new ArrayList<LocationData>();
            locations.add(data);
            locations.add(data2);

            vehicle = new VehicleData(name, capacieties, data,data2);
            vehicle.setTimeWindows(timeWindows);
        } catch (Exception e) {
            return null;
        }
        return vehicle;
    }
}
