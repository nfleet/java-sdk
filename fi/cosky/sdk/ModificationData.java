package fi.cosky.sdk;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

 import java.util.List;

 public class ModificationData extends BaseData {
    
    public static final String MimeType =  "application/vnd.jyu.nfleet.modification";
	public static final double MimeVersion = 2.0;
    
    private String Name;
    private String Info1;
    private String DataSource;
    private List<String> SpeedProfiles;
    private List<ModificationAreaData> Areas;
    private double SpeedFactor;
    private int VersionNumber;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

    public String getInfo1(){
        return Info1;
    }

    public void setInfo1(String info1){
        Info1 = info1;
    }

    public List<String> getSpeedProfiles(){
        return SpeedProfiles;
    }

    public void setSpeedProfiles(List<String> speedProfiles){
        SpeedProfiles = speedProfiles;
    }

    public double getSpeedFactor(){
        return SpeedFactor;
    }

    public void setSpeedFactor(double speedFactor){
        SpeedFactor = speedFactor;
    }    

    public String getDataSource(){
        return DataSource;
    }

    public void setDataSource(String dataSource){
        DataSource = dataSource;
    }

    public List<ModificationAreaData> getAreas(){
        return Areas;
    }

    public void setAreas(List<ModificationAreaData> areas){
        Areas = areas;
    }
}