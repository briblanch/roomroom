package unknowndev.com.capstone;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        public Context mContext;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my, container, false);

            // table creation
            mContext = getActivity().getApplicationContext();
            TableLayout tableLayout = (TableLayout) rootView.findViewById(R.id.room_table);

            TableRow tableRow;
            TextView textView;

            for(int i = 0; i < 3; i++) {
                tableRow = new TableRow(mContext);
                for(int j = 0; j < 3; j++) {
                    textView = new TextView(mContext);
                    textView.setTextColor(Color.BLACK);

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(i).append(" ").append(j);

                    textView.setText(stringBuilder.toString());
                    textView.setGravity(Gravity.CENTER);
                    textView.setPadding(20, 20, 20, 20);
                    tableRow.addView(textView);
                }
                tableLayout.addView(tableRow);
            }

            return rootView;
        }
    }
}
