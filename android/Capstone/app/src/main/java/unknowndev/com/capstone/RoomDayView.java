package unknowndev.com.capstone;

import android.app.Fragment;
import android.app.ListActivity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.goebl.david.Webb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;


public class RoomDayView extends ListActivity {

    static final ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

    private static final String baseURL = "http://asu-capstone.appspot.com/api/rooms/events/";

    private static JSONArray mEventArray;
    private static String mRoomTitle;
    private static String mCurrentEventName;
    private static String mCurrentEventStartTime;
    private static String mCurrentEventEndTime;
    private static String mRoomStatus;
    private static String mDashString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_day_view);
        Intent intent = getIntent();
        mRoomTitle = intent.getStringExtra(HomeActivity.ROOM_TITLE);
//        setTitle(roomTitle);

//        SimpleAdapter adapter = new SimpleAdapter(
//                this,
//                list,
//                R.layout.custom_row_view,
//                new String[] {"event", "startTime", "endTime", "creator"},
//                new int[] {R.id.text1,R.id.text2, R.id.text3, R.id.text4}
//        );

        list.clear();
        getData(intent.getStringExtra(HomeActivity.ROOM_ID));

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_room_day_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getData(String roomId) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.
                    ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssssZ");
//            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            Webb webb = Webb.create();
            JSONObject result = webb
                    .get(baseURL + roomId)
                    .param("date", simpleDateFormat.format(new Date()))
                    .ensureSuccess()
                    .asJsonObject()
                    .getBody();

            mEventArray = result.getJSONArray("events");
            Log.d("RoomDayView", "Received " + mEventArray.length() + " events");

            JSONObject[] allTodayEvents = new JSONObject[mEventArray.length()];

            for(int i = 0; i < mEventArray.length(); i++) {
                allTodayEvents[i] = mEventArray.getJSONObject(i);
            }

            Arrays.sort(allTodayEvents, new Comparator<JSONObject>() {
                final SimpleDateFormat sdfToDate = new SimpleDateFormat
                                                                ("yyyy-MM-dd'T'HH:mm:ssssZ");
                @Override
                public int compare(JSONObject lhs, JSONObject rhs) {
                    int compared = 0;
                    try {
                        compared = sdfToDate.parse(lhs.getJSONObject("start").getString("dateTime"))
                                .compareTo(sdfToDate.parse(rhs.getJSONObject("start")
                                        .getString("dateTime")));
                    } catch(Exception e) {

                    }
                    return compared;
                }
            });

//            Log.d("SortedArray", events.toString());
            JSONObject[] validEvents = new JSONObject[mEventArray.length()];
            SimpleDateFormat sdfToDate = new SimpleDateFormat
                    ("yyyy-MM-dd'T'HH:mm:ssssZ");
            int counter = 0;
            for(int i = 0; i < allTodayEvents.length; i++) {
                Date date1 = sdfToDate.parse(allTodayEvents[i].getJSONObject("end")
                        .getString("dateTime"));
                Date currentDate = new Date();
                if(date1.compareTo(currentDate) >= 0) {
                    // remove expired events for the day
                    validEvents[counter] = allTodayEvents[i];
                    counter++;
                }
                System.out.println(allTodayEvents[i].getJSONObject("start").getString("dateTime"));
            }

            Date currentDate = new Date();
            for(int i = 0; i < counter; i++) {
                if(i == 0) {
                    Date eventStart = sdfToDate.parse(validEvents[i].getJSONObject("start").getString("dateTime"));
                    Date eventEnd = sdfToDate.parse(validEvents[i].getJSONObject("end")
                            .getString("dateTime"));
                    if(eventStart.compareTo(currentDate) <= 0 && eventEnd.compareTo(currentDate) >= 0) {
                        mCurrentEventName = validEvents[i].getString("summary");
                        mCurrentEventStartTime = timeBuilder(validEvents[i].getJSONObject("start")
                                .getString("dateTime"));
                        mCurrentEventEndTime = timeBuilder(validEvents[i].getJSONObject("end")
                                .getString("dateTime"));
                        mRoomStatus = "Busy";
                        mDashString = " - ";
                    } else {
                        // set room status
                        mRoomStatus = "Available";

                        HashMap<String,String> temp = new HashMap<String,String>();
                        temp.put("event", validEvents[i].getString("summary"));
                        String startTime = timeBuilder(validEvents[i].getJSONObject("start")
                                .getString("dateTime"));

                        temp.put("startTime", startTime);

                        String endTime = timeBuilder(validEvents[i].getJSONObject("end")
                                .getString("dateTime"));
                        temp.put("endTime", endTime);
                        list.add(temp);
                    }
                } else {
                    HashMap<String,String> temp = new HashMap<String,String>();
                    temp.put("event", validEvents[i].getString("summary"));
                    String startTime = timeBuilder(validEvents[i].getJSONObject("start")
                            .getString("dateTime"));

                    temp.put("startTime", startTime);

                    String endTime = timeBuilder(validEvents[i].getJSONObject("end")
                            .getString("dateTime"));
                    temp.put("endTime", endTime);
                    list.add(temp);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String timeBuilder(String dateString) {
        try {
            Date startTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssssZ").parse(dateString);
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("US/Arizona"));
            calendar.setTime(startTime);

            String amPm;
            if(calendar.get(Calendar.AM_PM) == 0) {
                amPm = " AM";
            } else {
                amPm = " PM";
            }

            int hour = calendar.get(Calendar.HOUR);

            if(hour == 0) {
                hour = 12;
            }

            int minute = calendar.get(Calendar.MINUTE);
            String minuteString = "" + minute;

            if(minute == 0) {
                minuteString = "00";
            }

            return hour + ":" + minuteString + amPm;
        } catch(ParseException e) {}

        return "";
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_room_day_view, container, false);
            ListView lv = (ListView)rootView.findViewById(R.id.eventListView);
            SimpleAdapter adapter = new SimpleAdapter(
                    getActivity(),
                    list,
                    R.layout.custom_row_view,
                    new String[] {"event", "startTime", "endTime"},
                    new int[] {R.id.upcomingEventTextView,R.id.upcomingStartTimeTextView, R.id.upcomingEndTimeTextView}
            );

            lv.setAdapter(adapter);

            if(mRoomTitle != null) {
                ((TextView)rootView.findViewById(R.id.roomNameTextView)).setText(mRoomTitle);
            }
//            ((TextView)rootView.findViewById(R.id.roomNameTextView)).setText(mRoomTitle);
            if(mCurrentEventName != null) {
                ((TextView)rootView.findViewById(R.id.eventNameTextView)).setText(mCurrentEventName);
            }

            if(mCurrentEventStartTime != null) {
                ((TextView)rootView.findViewById(R.id.startTimeTextView)).setText(mCurrentEventStartTime);
            }

            if(mCurrentEventEndTime != null) {
                ((TextView)rootView.findViewById(R.id.endTimeTextView)).setText(mCurrentEventEndTime);
            }

            if(mRoomStatus != null) {
                ((TextView)rootView.findViewById(R.id.inUseTextView)).setText(mRoomStatus);
            }

            if(mDashString != null) {
                ((TextView)rootView.findViewById(R.id.dashTextView)).setText(mDashString);
            }

            return rootView;
        }
    }
}
