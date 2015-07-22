package fi.cosky.sdk;

import java.util.List;

public class DepotSetImportRequest {
    public static final String MimeType = DepotDataSet.MimeType;
    public static final double MimeVersion = DepotDataSet.MimeVersion;

    private List<DepotUpdateRequest> Items;

    public List<DepotUpdateRequest> getItems() {
        return Items;
    }

    public void setItems(List<DepotUpdateRequest> items) {
        Items = items;
    }
}
