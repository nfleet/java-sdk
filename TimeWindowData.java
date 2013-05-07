import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 21.3.2013
 * Time: 10:39
 * To change this template use File | Settings | File Templates.
 */
public class TimeWindowData extends GeneralDataType {
    private Date Start;
    private Date End;

    public TimeWindowData(Date start, Date end) {
        this.Start = start;
        this.End = end;
    }

    public static TimeWindowData newTimeWindowDto() {
        Date d = new Date();
        return new TimeWindowData(d, new Date(d.getTime() +10000 ));
    }

    public Date getEnd() {
        return End;
    }

    public void setEnd(Date end) {
        End = end;
    }

    public Date getStart() {
        return Start;
    }

    public void setStart(Date start) {
        Start = start;
    }
}
