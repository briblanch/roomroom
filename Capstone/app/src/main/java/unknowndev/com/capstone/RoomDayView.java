package unknowndev.com.capstone;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public class RoomDayView extends ListActivity {

    static final ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_room_day_view);

        Intent intent = getIntent();
        String roomTitle = intent.getStringExtra(MyActivity.ROOM_TITLE);
        setTitle(roomTitle);

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                list,
                R.layout.custom_row_view,
                new String[] {"event","time","creator"},
                new int[] {R.id.text1,R.id.text2, R.id.text3}
        );

        list.clear();
        populateList();

        setListAdapter(adapter);

//        if (savedInstanceState == null) {
//            getFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }
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

    private void populateList() {
        HashMap<String,String> temp = new HashMap<String,String>();
        temp.put("event","QA Meeting");
        temp.put("time", "11:00am-11:30am");
        temp.put("creator", "Mike Robbins");
        list.add(temp);

        HashMap<String,String> temp1 = new HashMap<String,String>();
        temp1.put("event","Dev show and tell");
        temp1.put("time", "12:00pm-1:30pm");
        temp1.put("creator", "Brent Arndorfer");
        list.add(temp1);

        HashMap<String,String> temp2 = new HashMap<String,String>();
        temp2.put("event","Test Blitz");
        temp2.put("time", "3:00pm-5:00pm,");
        temp2.put("creator", "Robert Harwell");
        list.add(temp2);

        HashMap<String,String> temp3 = new HashMap<String,String>();
        temp3.put("event","Pythonista Meeting");
        temp3.put("time", "5:15pm-5:30pm");
        temp3.put("creator", "Shawn Rusaw");
        list.add(temp3);

        HashMap<String,String> temp4 = new HashMap<String,String>();
        temp4.put("event","Date Night");
        temp4.put("time", "6:00pm-7:30pm");
        temp4.put("creator", "Derrick Ruhbar");
        list.add(temp4);
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
}
