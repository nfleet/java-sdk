package fi.cosky.sdk;


import java.util.List;

public class DepotError {
    private DepotData Depot;
    private List<ErrorData> Errors;

    public DepotData getDepot() {
        return Depot;
    }

    public void setDepot(DepotData depot) {
        Depot = depot;
    }

    public List<ErrorData> getErrors() {
        return Errors;
    }

    public void setErrors(List<ErrorData> errors) {
        Errors = errors;
    }
}
