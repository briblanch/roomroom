package unknowndev.com.capstone;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.SimpleAdapter;

import com.goebl.david.Webb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.jar.JarEntry;


public class RoomDayView extends ListActivity {

    static final ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

    private static final String baseURL = "http://asu-capstone.appspot.com/api/rooms/events/";
    private static final String arduinoURL = "http://10.180.55.86";

    private static JSONArray mEventArray;
    private static JSONObject mRoomStatus;

    private static String roomStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_room_day_view);

        Intent intent = getIntent();
        String roomTitle = intent.getStringExtra(HomeActivity.ROOM_TITLE);

        getRoomStatus();

        setTitle(roomTitle + " - " + roomStatus);

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                list,
                R.layout.custom_row_view,
                new String[] {"event", "startTime", "endTime", "creator"},
                new int[] {R.id.text1,R.id.text2, R.id.text3, R.id.text4}
        );

        list.clear();
        getData(intent.getStringExtra(HomeActivity.ROOM_ID));

        setListAdapter(adapter);

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

//    private void populateList(String roomId) {
//
//        System.out.println("ROOM ID" + roomId);
//        HashMap<String,String> temp = new HashMap<String,String>();
//        temp.put("event","QA Meeting");
//        temp.put("time", "11:00am-11:30am");
//        temp.put("creator", "Mike Robbins");
//        list.add(temp);
//
//        HashMap<String,String> temp1 = new HashMap<String,String>();
//        temp1.put("event","Dev show and tell");
//        temp1.put("time", "12:00pm-1:30pm");
//        temp1.put("creator", "Brent Arndorfer");
//        list.add(temp1);
//
//        HashMap<String,String> temp2 = new HashMap<String,String>();
//        temp2.put("event","Test Blitz");
//        temp2.put("time", "3:00pm-5:00pm");
//        temp2.put("creator", "Robert Harwell");
//        list.add(temp2);
//
//        HashMap<String,String> temp3 = new HashMap<String,String>();
//        temp3.put("event","Pythonista Meeting");
//        temp3.put("time", "5:15pm-5:30pm");
//        temp3.put("creator", "Shawn Rusaw");
//        list.add(temp3);
//
//        HashMap<String,String> temp4 = new HashMap<String,String>();
//        temp4.put("event","Date Night");
//        temp4.put("time", "6:00pm-7:30pm");
//        temp4.put("creator", "Derrick Ruhbar");
//        list.add(temp4);
//    }

    private void getData(String roomId) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.
                    ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssssZ");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            Webb webb = Webb.create();
            JSONObject result = webb
                    .get(baseURL + roomId)
                    .param("date", simpleDateFormat.format(new Date()))
                    .ensureSuccess()
                    .asJsonObject()
                    .getBody();
            System.out.println(result.toString());

            mEventArray = result.getJSONArray("events");

            for(int i = 0; i < mEventArray.length(); i++) {
                HashMap<String,String> temp = new HashMap<String,String>();
                temp.put("event", mEventArray.getJSONObject(i).getString("summary"));
                String startTime = timeBuilder(mEventArray.getJSONObject(i).getJSONObject("start")
                        .getString("dateTime"));

                temp.put("startTime", startTime);

                String endTime = timeBuilder(mEventArray.getJSONObject(i).getJSONObject("end")
                        .getString("dateTime"));
                temp.put("endTime", endTime);
                temp.put("creator", mEventArray.getJSONObject(i).getJSONObject("creator")
                        .getString("displayName"));
                temp.put("time", "5:00pm");
                list.add(temp);
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
                amPm = "am";
            } else {
                amPm = "pm";
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
        } catch(ParseException e) {

        }

        return "";
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_room_day_view, container, false);
            return rootView;
        }
    }

    private void getRoomStatus() {
        try{
            URL url = new URL(arduinoURL);
            HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();
            readStream(con.getInputStream());
            roomStatus = mRoomStatus.getString("roomUsed");
            System.out.println(roomStatus);
        }
        catch  (Exception e) {
            e.printStackTrace();
        }
    }

    private void readStream(InputStream in) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String jsonString = "";
            String line = "";
            while ((line = reader.readLine()) != null) {
                jsonString += line;
            }

//                List<String> list = new ArrayList<String>();
            mRoomStatus = new JSONObject(jsonString);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
