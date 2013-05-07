/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 9.4.2013
 * Time: 9:17
 * To change this template use File | Settings | File Templates.
 */
public class ResultData extends GeneralDataType {
    private Link location;
    private ErrorData data;

    public ErrorData getData() {
        return data;
    }

    public void setData(ErrorData data) {
        this.data = data;
    }

    public Link getLocation() {
        return location;
    }

    public void setLocation(Link l) {
        this.location = l;
    }
}
