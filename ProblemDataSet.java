import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 5.4.2013
 * Time: 11:16
 * To change this template use File | Settings | File Templates.
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
