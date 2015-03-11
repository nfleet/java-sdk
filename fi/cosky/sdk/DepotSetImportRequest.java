package fi.cosky.sdk;


import java.util.ArrayList;

public class DepotSetImportRequest {
    public static final String MimeType = DepotDataSet.MimeType;
    public static final double MimeVersion = DepotDataSet.MimeVersion;

    private ArrayList<DepotUpdateRequest> Items;

    public ArrayList<DepotUpdateRequest> getItems() {
        return Items;
    }

    public void setItems(ArrayList<DepotUpdateRequest> items) {
        Items = items;
    }
}
