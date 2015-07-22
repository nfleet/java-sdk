package fi.cosky.sdk;


import java.util.ArrayList;

public class DepotDataSet extends BaseData {
    public static final String MimeType = "application/vnd.jyu.nfleet.depotset";
    public static final double MimeVersion = 2.2;

    private ArrayList<DepotData> Items;
    private int VersionNumber;

    public ArrayList<DepotData> getItems() {
        return Items;
    }

    public void setItems(ArrayList<DepotData> items) {
        Items = items;
    }


    public int getVersionNumber() {
        return VersionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        VersionNumber = versionNumber;
    }
}
