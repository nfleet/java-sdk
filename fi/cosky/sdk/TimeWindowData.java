package fi.cosky.sdk;
import java.util.Date;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class TimeWindowData extends BaseData {
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
