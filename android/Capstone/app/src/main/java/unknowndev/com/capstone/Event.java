package unknowndev.com.capstone;

import java.util.Date;

/**
 * Created by kristophersmith on 1/17/15.
 */
public class Event {
    private String mSummary;
    private Date mStart;
    private Date mEnd;
    private String mCreator;

    public Event(String summary, Date start, Date end, String creator) {
        mSummary = summary;
        mStart = start;
        mEnd = end;
        mCreator = creator;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public Date getStart() {
        return mStart;
    }

    public void setStart(Date start) {
        mStart = start;
    }

    public Date getEnd() {
        return mEnd;
    }

    public void setEnd(Date end) {
        mEnd = end;
    }

    public String getCreator() {
        return mCreator;
    }

    public void setCreator(String creator) {
        mCreator = creator;
    }
}
