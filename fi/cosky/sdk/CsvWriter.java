package fi.cosky.sdk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class CsvWriter {
	private final String vehicleFile = "vehicles.csv";
	private final String tasksFile = "tasks.csv";
	private final String[] taskHeaders = { "TaskID","Info1","Info2","Info3","Info4","Cap1","Cap2","Cap3","Prio","PAdd",
										   "PPc","PCity","PCtry","PLat","PLon","PSerT","PStopT","PTWStart","PTWEnd","DAdd",
										   "DPc","DCity","DCtry","DLat","DLon","DSerT","DStopT","DTWStart","DTWEnd","Incomp",
										   "Comp","TRel", "PATime","TVehId","PSeq","DSeq","IsLocked" };
	
	private final String[] vehicleHeaders = { "VehID","VehType","VCap1","VCap2","VCap3","VFixedC","VKmC","VHourC","VPAdd",
											  "VPPc","VPCity","VPCtry","VPLat","VPLon","VDAdd","VDPc","VDCity","VDCtry",
											  "VDLat","VDLon","VTWStart","VTWEnd","VRel"};
	
	private static final String noString = '"' + "(no)" + '"' + ";";
	
	/**
	 * Writes vehicles.csv and tasks.csv for importing to NFleet web app. 
	 *  
	 * @param plan 
	 * @param vehicles
	 * @param tasks
	 */
	public void write(PlanData plan, VehicleDataSet vehicles, TaskDataSet tasks) {
		if (plan == null || vehicles == null || tasks == null) {
			System.out.println("one of the parameters for write is null, breaking");
			return;
		}
		writeVehicles(vehicles);
		HashMap<Integer, String> fromPlan = extractFromPlan(plan);
		writeTasks(tasks, fromPlan);
	}
	
	
	private void writeTasks(TaskDataSet tasks, HashMap<Integer, String> fromPlan) {
		File f = null;
		BufferedWriter output = null;
		StringBuilder sb = null;
		try {
			f = new File(tasksFile);
			output = new BufferedWriter(new FileWriter(f));
			output.write(buildStringFromHeaders(taskHeaders));
			
			for (TaskData t : tasks.getItems()) {
				sb = new StringBuilder();
				sb.append("\"" + t.getName() + "\";");   //taskid
				
				sb.append(valueOrNoString(t.getInfo())); //info1
				sb.append(valueOrNoString(t.getInfo2())); //info2
				sb.append(valueOrNoString(t.getInfo3())); //info3
				sb.append(valueOrNoString(t.getInfo4())); //info4

				sb.append( capacityStrings(t.getTaskEvents().get(0).getCapacities()) ); //cap1, cap2, cap3	
				sb.append( "\"" + t.getPriority() + "\";"); //Priority
				sb.append( locationString(t.getTaskEvents().get(0).getLocation()) ); // "PAdd", "PPc","PCity","PCtry","PLat","PLon"
				sb.append( "\"" + t.getTaskEvents().get(0).getServiceTime() + "\";"); //PSerT
				sb.append( "\"" + t.getTaskEvents().get(0).getStoppingTime() + "\";" ); //PStopt
				sb.append( "\"" + buildDateTimeString(t.getTaskEvents().get(0).getTimeWindows().get(0).getStart()) + "\";"); //PTWStart (pickupTimewindowStart)
				sb.append( "\"" + buildDateTimeString(t.getTaskEvents().get(0).getTimeWindows().get(0).getEnd()) + "\";"); //PTWEnd (pickupTimeWindowEnd
				sb.append( locationString(t.getTaskEvents().get(1).getLocation()) ); //"DAdd", "DPc","DCity","DCtry","DLat","DLon"
				sb.append( "\"" + t.getTaskEvents().get(1).getServiceTime() + "\";"); //DSerT 
				sb.append( "\"" + t.getTaskEvents().get(1).getStoppingTime() +"\";" ); //Stopt
				sb.append( "\"" + buildDateTimeString(t.getTaskEvents().get(1).getTimeWindows().get(0).getStart()) + "\";"); //DTWStart
				sb.append( "\"" + buildDateTimeString(t.getTaskEvents().get(1).getTimeWindows().get(0).getEnd()) + "\";"); //DTWEnd
				if ( t.getIncompatibleVehicleTypes() != null && t.getIncompatibleVehicleTypes().size() > 1 ) //Incomp
					sb.append("\"" + t.getIncompatibleVehicleTypes().toString() + "\";");
				else 
					sb.append(noString);
				
				if ( t.getCompatibleVehicleTypes() != null && t.getCompatibleVehicleTypes().size() > 1) //Comp
					sb.append("\"" + t.getCompatibleVehicleTypes().toString() + "\";");
				else
					sb.append(noString);
				
				sb.append( "\"" + t.getRelocationType() + "\";" ); //TRel
				sb.append( fromPlan.get(t.getId()) ); //PATime(pickup time), VehId(vehicle id), PSeq(pickup sequence number), DSeq (delivery sequence number) 
				sb.append( "\"Unlocked\";" ); //IsLocked (if task laready has pa time the lock is created, thus this can be unlocked)
				sb.append( "\n" );
				output.write(sb.toString());
			}
			
		} catch (Exception e) {
			System.out.println("Something went wrong when writing " + tasksFile);
		} finally {
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	
	private HashMap<Integer, String> extractFromPlan(PlanData plan) {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		for (FieldsItem fi : plan.getItems()) {
			for (RouteEventData event : fi.getEvents()) {
				if (event.getType().equals("Pickup")) {
					map.put(event.getTaskId(), "\"" + buildDateTimeString(event.getArrivalTime()) + "\";\"" + fi.getName() + "\";\"" +event.getSequenceNumber() + "\";");
				}
				if (event.getType().equals("Delivery")) {
					String s = map.get(event.getTaskId());
					map.remove(event.getTaskId());
					s += "\"" + event.getSequenceNumber() + "\";";
					map.put(event.getTaskId(), s);
				}
			}
		}
		return map;
	}
	
	private void writeVehicles(VehicleDataSet vehicles) {
		File f = null;
		BufferedWriter output = null;
		StringBuilder sb = null;
		try {
			f = new File(vehicleFile);
			String headers = buildStringFromHeaders(vehicleHeaders);
			output = new BufferedWriter(new FileWriter(f));
			output.write(headers);
			
			for (VehicleData vehicle : vehicles.getItems()) {
				sb = new StringBuilder();
				sb.append( "\"" + vehicle.getName() + "\";" ); //VehID
				sb.append( "\"" + valueOrNoString(vehicle.getVehicleType()) + "\";" ); //VType
				sb.append( capacityStrings(vehicle.getCapacities()) );  // vcap1, vcap2, vcap3
				sb.append( "\"" + vehicle.getFixedCost() + "\";" ); //VFixedC
				sb.append( "\"" + vehicle.getKilometerCost() + "\";"); //VKmC
				sb.append( "\"" + vehicle.getHourCost() + "\";" ); //VHourC
				sb.append( locationString( vehicle.getStartLocation()) ); //"VPAdd", "VPPc","VPCity","VPCtry","VPLat","VPLon"
				sb.append( locationString( vehicle.getEndLocation()) ); // "DAdd", "DPc","DCity","DCtry","DLat","DLon"
				if (vehicle.getTimeWindows().size() == 1) {
					sb.append("\"" + buildDateTimeString(vehicle.getTimeWindows().get(0).getStart()) + "\";"); //VTWStart (vehicleTimeWindowStart)
					sb.append("\"" + buildDateTimeString(vehicle.getTimeWindows().get(0).getEnd()) + "\";"); //VTWEnd (VehicleTimeWindowEnd)
				}
				sb.append("\"" + vehicle.getRelocationType() + "\";"); //VRel (Can be relocated)
				sb.append("\n");
				output.write(sb.toString());
			}			
		} catch ( Exception e ){
			System.out.println("Something went wrong when writing " + vehicleFile);
		} finally {
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	private String locationString(LocationData location) {
		if (location == null ) return noString;
		
		StringBuilder sb = new StringBuilder();
		
		if (location.getAddress() != null) {
			if (isNullOrEmpty(location.getAddress().getStreet()))
				sb.append(noString);
			else 
				sb.append("\"" + location.getAddress().getStreet() + " " + location.getAddress().getApartmentNumber() + "\";");
			if (isNullOrEmpty(location.getAddress().getPostalCode())) 
				sb.append(noString);
			else
				sb.append( "\"" + location.getAddress().getPostalCode()+ "\";");
			if (isNullOrEmpty(location.getAddress().getCity()))
				sb.append(noString);
			else 
				sb.append("\"" + location.getAddress().getCity() + "\";");
			if (isNullOrEmpty(location.getAddress().getCountry()))
				sb.append(noString);
			else
				sb.append("\"" + location.getAddress().getCountry()+ "\";");
		} else {
			sb.append(noString + noString + noString + noString );
		}
		
		if (location.getCoordinatesData() != null) {
			sb.append("\"");
			sb.append(location.getCoordinatesData().getLatitude());
			sb.append( "\";\"");
			sb.append(location.getCoordinatesData().getLongitude());
			sb.append( "\";");
		} else {
			sb.append(noString + noString);
		}
		return sb.toString();
	}
	
	
	private static boolean isNullOrEmpty(String param) {
		return param == null || param.trim().length() == 0;
	}
	
	private static String valueOrNoString(String param) {
		return (isNullOrEmpty(param)) ? noString : "\"" + param + "\";";  
	}
	
	private String capacityStrings(List<CapacityData> caps) {
		StringBuilder sb = new StringBuilder();
		
		switch (caps.size()){
			case 1: {
				sb.append("\"" + caps.get(0).getAmount() + "\";\"(no)\";\"(no)\";");
				break;
			}
			case 2: {
				sb.append('"' + caps.get(0).getAmount() + "\";\"" + caps.get(1).getAmount() + "\";\"(no)\";" );
				break;
			}
			case 3: {
				sb.append('"' + caps.get(0).getAmount() + "\";\"" + caps.get(1).getAmount() + "\";\"" + caps.get(2).getAmount() + "\";");
				break;
			}
		}
		return sb.toString();
	}
	
	private String buildStringFromHeaders(String[] headers) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < headers.length; i++) {
			sb.append("\"" + headers[i] + "\";");
		}
		
		sb.append("\n");
		
		return sb.toString();
	}
	
	private String buildDateTimeString(Date date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT);
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		return format.format(date);
	}
}
