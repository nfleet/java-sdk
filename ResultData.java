import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 9.4.2013
 * Time: 9:17
 * To change this template use File | Settings | File Templates.
 */
public class ResultData extends BaseData {
    private Link Location;
    private ArrayList<ErrorData> Items;
    private int Id;
    private String Message;

    public ArrayList<ErrorData> getItems() {
        return Items;
    }

    public void setItems(ArrayList<ErrorData> items) {
        Items = items;
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

    public Link getLocation() {
        return Location;
    }

    public void setLocation(Link l) {
        this.Location = l;
    }
}
