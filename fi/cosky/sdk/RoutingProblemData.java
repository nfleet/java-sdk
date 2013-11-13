package fi.cosky.sdk;
import java.util.Date;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class RoutingProblemData extends BaseData{
    private int Id;
    private Date CreationDate;
   

	private Date ModifiedDate;
    private String Name;
    private String State;
    private int Progress;
    private int VersionNumber;
    
    public int getProgress() {
        return Progress;
    }

    public void setProgress(int progress) {
        Progress = progress;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public RoutingProblemData(String name) {
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

    public int getVersionNumber() {
		return VersionNumber;
	}

	public void setVersionNumber(int versionNumber) {
		VersionNumber = versionNumber;
	}
    
    @Override
    public String toString() {
         return API.gson.toJson(this);
    }

    public String getName() {
        return Name;
    }

    public RoutingProblemUpdateRequest toRequest() {
        RoutingProblemUpdateRequest request = new RoutingProblemUpdateRequest(Name);
        request.setCreationDate(this.getCreationDate());
        request.setModifiedDate(this.getModifiedDate());
        request.setId(this.getId());
        request.setState(this.getState());
        request.setVersionNumber(this.getVersionNumber());
        return request;
    }
}
