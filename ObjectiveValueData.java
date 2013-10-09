import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 7.10.2013
 * Time: 15:45
 * To change this template use File | Settings | File Templates.
 */
public class ObjectiveValueData extends BaseData {
    private Date TimeStamp;
    private double Value;

    public Date getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        TimeStamp = timeStamp;
    }

    public double getValue() {
        return Value;
    }

    public void setValue(double value) {
        Value = value;
    }
}
