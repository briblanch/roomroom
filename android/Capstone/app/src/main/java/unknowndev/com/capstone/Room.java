package unknowndev.com.capstone;

/**
 * Created by kristophersmith on 1/17/15.
 */
public class Room {
    private String mId;
    private String mName;
    private String mCalender;
    private int mCapacity;

    public Room(String id, String name, String calender, int capacity) {
        mId = id;
        mName = name;
        mCalender = calender;
        mCapacity = capacity;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getCalender() {
        return mCalender;
    }

    public void setCalender(String calender) {
        mCalender = calender;
    }

    public int getCapacity() {
        return mCapacity;
    }

    public void setCapacity(int capacity) {
        mCapacity = capacity;
    }
}
