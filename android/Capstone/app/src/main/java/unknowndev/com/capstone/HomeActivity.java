package unknowndev.com.capstone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class HomeActivity extends Activity {

    public final static String ROOM_TITLE = "unknowndev.com.capstone.ROOMTITLE";
    public final static String ROOM_ID = "unknowndev.com.capstone.ROOMID";

    private static JSONArray mRoomArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("selectedRoom", "");

//        Room selectedRoom;

        if(!json.equals("")) {
            Intent intent = new Intent(this, RoomDayView.class);
            intent.putExtra("selectedRoom", gson.fromJson(json, Room.class));
            startActivity(intent);
        }
//        Room selectedRoom = gson.fromJson(json, Room.class);


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

        private GridViewAdapter mAdapter;
        private ArrayList<String> roomNames = new ArrayList<>();
        private ArrayList<Integer> roomImages = new ArrayList<>();

        private GridView mGridView;

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
                            roomNames.clear();
                            roomImages.clear();
                            requestDataRefresh(rootView);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }, 5000);
                }
            });

            // get data from server
            getData();

            // prepared arraylist and passed it to the Adapter class
            mAdapter = new GridViewAdapter(getActivity(), roomNames, roomImages);

            // Set custom adapter to gridview
            mGridView = (GridView) rootView.findViewById(R.id.gridView1);
            mGridView.setAdapter(mAdapter);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    final Intent intent = new Intent(getActivity(), RoomDayView.class);
                    try {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Set Default Room")
                                .setMessage("Make this your Default Room?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            Room selectedRoom = new Room();
                                            selectedRoom.setName(mRoomArray.getJSONObject(position).getString("name"));
                                            selectedRoom.setId(mRoomArray.getJSONObject(position).getString("id"));
                                            selectedRoom.setBlobKey(mRoomArray.getJSONObject(position).getString("blob_key"));
                                            selectedRoom.setCapacity(mRoomArray.getJSONObject(position).getInt("capacity"));

                                            Gson gson = new Gson();
                                            String json = gson.toJson(selectedRoom);



                                            // Persist selected room locally
                                            String selectedRoomId = mRoomArray.getJSONObject(position).getString("id");
                                            SharedPreferences preferences = mContext.getSharedPreferences("MyPreferences",
                                                    Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();
//                        editor.putString("selectedRoomId", selectedRoomId);
                                            editor.putString("selectedRoom", json);

                                            editor.commit();

                                            intent.putExtra("selectedRoom", selectedRoom);
                                            startActivity(intent);
                                        } catch(Exception e) {

                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();


                    } catch(Exception e) {

                    }
                }

            });

            populateRoomTable(mRoomArray, rootView);

            return rootView;
        }

        private void populateRoomTable(JSONArray roomArray, View rootView) {
            mContext = getActivity().getApplicationContext();

            for(int i = 0; i < mRoomArray.length(); i++) {
                try {
                    roomNames.add(mRoomArray.getJSONObject(i).getString("name"));
                    roomImages.add(R.drawable.ic_launcher);
                } catch(JSONException e) {

                }
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
