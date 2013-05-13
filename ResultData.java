import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 9.4.2013
 * Time: 9:17
 * To change this template use File | Settings | File Templates.
 */
public class ResultData extends GeneralDataType {
    private Link Location;
    private ArrayList<ErrorData> ErrorDataSet;
    private int Id;
    private String Message;

    public ArrayList<ErrorData> getErrorDataSet() {
        return ErrorDataSet;
    }

    public void setErrorDataSet(ArrayList<ErrorData> errorDataSet) {
        ErrorDataSet = errorDataSet;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<ErrorData> getData() {
        return ErrorDataSet;
    }

    public void setData(ArrayList<ErrorData> data) {
        this.ErrorDataSet = data;
    }

    public Link getLocation() {
        return Location;
    }

    public void setLocation(Link l) {
        this.Location = l;
    }
}
