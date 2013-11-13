package fi.cosky.sdk;
import java.util.ArrayList;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
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
