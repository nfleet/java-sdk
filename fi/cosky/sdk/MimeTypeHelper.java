package fi.cosky.sdk;

import java.util.HashMap;

public class MimeTypeHelper {
	private final String versionPrefix = "-";
	private final String versionPostfix = "+json";
	
	private static final HashMap<String,String> supportedTypes = new HashMap<String,String>();
	
	public MimeTypeHelper() {
		initialize();
	}
	
	private void initialize() {
		supportedTypes.put(UserData.MimeType, UserData.MimeType + versionPrefix + UserData.MimeVersion + versionPostfix);
		supportedTypes.put(UserDataSet.MimeType, UserDataSet.MimeType + versionPrefix + UserDataSet.MimeVersion + versionPostfix );
		supportedTypes.put(ImportData.MimeType, ImportData.MimeType + versionPrefix + ImportData.MimeVersion + versionPostfix);
		supportedTypes.put(ObjectiveValueDataSet.MimeType, ObjectiveValueDataSet.MimeType + versionPrefix + ObjectiveValueDataSet.MimeVersion + versionPostfix);
		supportedTypes.put(PlanData.MimeType, PlanData.MimeType + versionPrefix + PlanData.MimeVersion + versionPostfix);
		supportedTypes.put(RouteData.MimeType, RouteData.MimeType + versionPrefix + RouteData.MimeVersion + versionPostfix);
		supportedTypes.put(RouteEventData.MimeType, RouteEventData.MimeType + versionPrefix + RouteEventData.MimeVersion + versionPostfix);
		supportedTypes.put(RouteEventDataSet.MimeType, RouteEventDataSet.MimeType + versionPrefix + RouteEventDataSet.MimeVersion + versionPostfix);
		supportedTypes.put(RoutingProblemData.MimeType, RoutingProblemData.MimeType + versionPrefix + RoutingProblemData.MimeVersion + versionPostfix);
		supportedTypes.put(RoutingProblemDataSet.MimeType, RoutingProblemDataSet.MimeType + versionPrefix + RoutingProblemDataSet.MimeVersion + versionPostfix);
		supportedTypes.put(RoutingProblemSettingsData.MimeType, RoutingProblemSettingsData.MimeType + versionPrefix + RoutingProblemSettingsData.MimeVersion + versionPostfix);
		supportedTypes.put(TaskData.MimeType, TaskData.MimeType + versionPrefix + TaskData.MimeVersion + versionPostfix);
		supportedTypes.put(TaskDataSet.MimeType, TaskDataSet.MimeType + versionPrefix + TaskDataSet.MimeVersion + versionPostfix);
		supportedTypes.put(VehicleData.MimeType, VehicleData.MimeType + versionPrefix + VehicleData.MimeVersion + versionPostfix);
		supportedTypes.put(VehicleDataSet.MimeType, VehicleDataSet.MimeType + versionPrefix + VehicleDataSet.MimeVersion + versionPostfix);
		supportedTypes.put(VehicleTypeData.MimeType, VehicleTypeData.MimeType + versionPrefix + VehicleTypeData.MimeVersion + versionPostfix);
		supportedTypes.put(ErrorData.MimeType, ErrorData.MimeType + versionPrefix + ErrorData.MimeVersion + versionPostfix);
	}
	
	public String getSupportedType(String type) {
		if (type == null || type.length() < 1) return "application/json";
		String[] parts = type.split("-");
		return supportedTypes.containsKey(parts[0]) ? supportedTypes.get(parts[0]) : "application/json";		
	}
}
