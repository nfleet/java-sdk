package fi.cosky.sdk;
import java.util.List;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class ProblemDataSet extends BaseData {
    private List<ProblemData> Items;

    public List<ProblemData> getItems() {
        return Items;
    }

    public void setItems(List<ProblemData> items) {
        Items = items;
    }
}
