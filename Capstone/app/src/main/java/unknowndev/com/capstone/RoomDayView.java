package unknowndev.com.capstone;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListActivity;
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

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                list,
                R.layout.custom_row_view,
                new String[] {"pen","price","color"},
                new int[] {R.id.text1,R.id.text2, R.id.text3}
        );

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
        temp.put("pen","MONT Blanc");
        temp.put("price", "200.00$");
        temp.put("color", "Silver, Grey, Black");
        list.add(temp);
        HashMap<String,String> temp1 = new HashMap<String,String>();
        temp1.put("pen","Gucci");
        temp1.put("price", "300.00$");
        temp1.put("color", "Gold, Red");
        list.add(temp1);
        HashMap<String,String> temp2 = new HashMap<String,String>();
        temp2.put("pen","Parker");
        temp2.put("price", "400.00$");
        temp2.put("color", "Gold, Blue");
        list.add(temp2);
        HashMap<String,String> temp3 = new HashMap<String,String>();
        temp3.put("pen","Sailor");
        temp3.put("price", "500.00$");
        temp3.put("color", "Silver");
        list.add(temp3);
        HashMap<String,String> temp4 = new HashMap<String,String>();
        temp4.put("pen","Porsche Design");
        temp4.put("price", "600.00$");
        temp4.put("color", "Silver, Grey, Red");
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
