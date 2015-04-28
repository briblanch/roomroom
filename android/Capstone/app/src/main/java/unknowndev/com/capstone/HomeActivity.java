package unknowndev.com.capstone;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HomeActivity extends Activity {

    public final static String ROOM_TITLE = "unknowndev.com.capstone.ROOMTITLE";
    public final static String ROOM_ID = "unknowndev.com.capstone.ROOMID";
    public final static String ROOM_STATUS = "unknowndev.com.capstone.ROOMSTATUS";

    private static JSONArray mRoomArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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

        private static final String baseURL = "http://asu-capstone.appspot.com/api/rooms";

        // SwipeRefreshLayout allows the user to swipe the screen down to trigger a manual refresh
        private SwipeRefreshLayout mSwipeRefreshLayout;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

            // swipe to refresh
            mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
            mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            requestDataRefresh(rootView);
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }, 5000);
                }
            });

            // get data from server
            getData();

            populateRoomTable(mRoomArray, rootView);

            return rootView;
        }

        private void populateRoomTable(JSONArray roomArray, View rootView) {
            // table creation
            mContext = getActivity().getApplicationContext();
            TableLayout tableLayout = (TableLayout) rootView.findViewById(R.id.room_table);
            tableLayout.removeAllViewsInLayout();

            TableRow tableRow;
            TextView textView;

            for(int i = 0; i < mRoomArray.length(); i++) {
                tableRow = new TableRow(mContext);
                tableRow.setClickable(true);
                tableRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), RoomDayView.class);
                        TextView roomTitle = (TextView) ((TableRow) v).getChildAt(0);
                        intent.putExtra(ROOM_TITLE, roomTitle.getText());
                        TextView roomId = (TextView) ((TableRow) v).getChildAt(3);
                        intent.putExtra(ROOM_ID, roomId.getText());
                        intent.putExtra(ROOM_STATUS, "False");
                        startActivity(intent);
                    }
                });

                for(int j = 0; j < 4; j++) {
                    textView = new TextView(mContext);
                    textView.setTextColor(Color.BLACK);
                    textView.setId(j);

                    switch(j) {
                        case 0:
                            try {
                                textView.setText(mRoomArray.getJSONObject(i).getString("name"));
                            } catch(JSONException e) {

                            }
                            break;
                        case 1:
                            try {
                                textView.setText(mRoomArray.getJSONObject(i).getString("capacity"));
                            } catch(JSONException e) {

                            }
                            break;
                        case 2:
                            textView.setText("Yes");
                            break;
                        case 3:
                            textView.setVisibility(View.INVISIBLE);
                            try {
                                textView.setText(mRoomArray.getJSONObject(i).getString("id"));
                            } catch(JSONException e) {

                            }
                            break;
                    }

                    textView.setGravity(Gravity.CENTER);
                    textView.setPadding(20, 20, 20, 20);
                    tableRow.addView(textView);
                }
                tableLayout.addView(tableRow);
            }
        }

        private void requestDataRefresh(View rootView) {
            getData();
            populateRoomTable(mRoomArray, rootView);
        }

        private void getData() {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.
                        ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                URL url = new URL(baseURL);
                HttpURLConnection con = (HttpURLConnection) url
                        .openConnection();
                readStream(con.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void readStream(InputStream in) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String jsonString = "";
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonString += line;
                }

                JSONObject jsonObject = new JSONObject(jsonString);
                mRoomArray = jsonObject.getJSONArray("rooms");

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
}
