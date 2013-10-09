import java.lang.reflect.Field;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 12.9.2013
 * Time: 11:51
 * To change this template use File | Settings | File Templates.
 */
public class RoutingProblemData extends BaseData{
    private int Id;
    private Date CreationDate;
    private Date ModifiedDate;
    private String Name;
    private String State;
    private int Progress;

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
        return request;
    }
}
