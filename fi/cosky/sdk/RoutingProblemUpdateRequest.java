package fi.cosky.sdk;
import java.util.Date;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class RoutingProblemUpdateRequest extends BaseData {
	public static final String MimeType = "application/vnd.jyu.nfleet.routingproblem+json";
	public static final double MimeVersion = 2.0;
	
    private int Id;
    private Date CreationDate;
    private Date ModifiedDate;
    private String Name;
    private String State;
    private int VersionNumber;
    
    int getVersionNumber() {
		return VersionNumber;
	}

	void setVersionNumber(int versionNumber) {
		VersionNumber = versionNumber;
	}

	public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public RoutingProblemUpdateRequest(String name) {
        this.Name = name;
    }

    public static RoutingProblemUpdateRequest createSolutionDto() {
        return new RoutingProblemUpdateRequest("exampleSolution");
    }

    public void setId(int id) {
        this.Id = id;
    }

    public void setCreationDate(Date date) {
        this.CreationDate = date;
    }

    public void setModifiedDate(Date date) {
        this.ModifiedDate = date;
    }

    public void setName(String name) {
        this.Name = name;
    }
    public int getId() {
        return Id;
    }

    public Date getCreationDate() {
        return CreationDate;
    }

    public Date getModifiedDate() {
        return ModifiedDate;
    }
    
    public String getName() {
        return Name;
    }
    
    @Override
    public String toString() {
       /* return "RoutingProblemUpdateRequest{" +
                "Id=" + Id +
                ", CreationDate=" + CreationDate +
                ", ModifiedDate=" + ModifiedDate +
                ", Name='" + Name + '\'' +
                '}';*/
        return API.gson.toJson(this);
    }
}
