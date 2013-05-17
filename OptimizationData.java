/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 2.5.2013
 * Time: 12:34
 * To change this template use File | Settings | File Templates.
 */
public class OptimizationData extends BaseData {
    private int Id;
    private String State;
    private VehicleDataSet Results;
    private int Progress;

    public int getProgress() {
        return Progress;
    }

    public void setProgress(int progress) {
        Progress = progress;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        this.State = state;
    }

    public VehicleDataSet getResults() {
        return Results;
    }

    public void setResults(VehicleDataSet results) {
        this.Results = results;
    }

}
