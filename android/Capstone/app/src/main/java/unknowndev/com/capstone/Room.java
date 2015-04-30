package unknowndev.com.capstone;


import android.os.Parcel;
import android.os.Parcelable;

public class Room implements Parcelable {
    private String mId;
    private String mName;
    private String mCalender;
    private int mCapacity;
    private String mBlobKey;
    private String mMotionIp;

    public Room(String id, String name, String calender, int capacity, String blobKey, String motionIp) {
        mId = id;
        mName = name;
        mCalender = calender;
        mCapacity = capacity;
        mBlobKey = blobKey;
        mMotionIp = motionIp;
    }

    public Room() {
        mId = "";
        mName = "";
        mCalender = "";
        mCapacity = 0;
        mBlobKey = "";
        mMotionIp = "";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mId);
        out.writeString(mName);
        out.writeString(mCalender);
        out.writeInt(mCapacity);
        out.writeString(mBlobKey);
        out.writeString(mMotionIp);
    }

    public static final Parcelable.Creator<Room> CREATOR = new Parcelable.Creator<Room>() {
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    private Room(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mCalender = in.readString();
        mCapacity = in.readInt();
        mBlobKey = in.readString();
        mMotionIp = in.readString();
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

    public String getBlobKey() {
        return mBlobKey;
    }

    public void setBlobKey(String blobKey) {
        mBlobKey = blobKey;
    }

    public String getMotionIp() {
        return mMotionIp;
    }

    public void setMotionIp(String motionIp) {
        mMotionIp = motionIp;
    }
}
