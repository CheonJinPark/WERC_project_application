package uconn.werc_project_application;

import android.Manifest;
import android.app.ActionBar;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Information.Information;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
// AWS Database
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsClient;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsEvent;
import com.amazonaws.mobileconnectors.s3.transferutility.*;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import uconn.werc_project_application.ble.BLEDataLinker;
import uconn.werc_project_application.data.AqiContentContract;
import uconn.werc_project_application.data.DataInterpreter;
import uconn.werc_project_application.data.DummyDataGenerator;
import uconn.werc_project_application.data.SendDataService;
import uconn.werc_project_application.data.SensorContentContract;
import uconn.werc_project_application.gps.LocationListenerService;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

// AWS Database


public class MainActivity extends AppCompatActivity {

    Double longitude, latitude;
    Intent intent_test, intent_ble;
    String url;
    GPS g1, g2, g3, g4, g5;
    private static final int INSERT_TOKEN = 1003;
    TextView userName, apiValue, co_value, o3_value, no2_value, so2_value, pm_value, connection_state, data_point;
    Information info = new Information();
    public final static String BLE_CONNECT = "Sensorem Active", BLE_DISCONNECT = "Sensorem not connected";

    private ContentResolver contentResolver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Install the application crash handler.
        ApplicationCrashHandler.installHandler();
        contentResolver = getApplicationContext().getContentResolver();


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);

        // Check GPS Access Permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("GPS Permissions", "Performing Check");


            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    2);
            Log.d("GPS Permissions", "Issuing Request");
        } else {
            // permission has been granted, continue as usual
            startService(new Intent(this, LocationListenerService.class));
            Log.d("GPS Permissions", "Permission Granted");

        }
        startService(new Intent(this, SendDataService.class));

        /* Bill's Section - Temporary */
        /** AWS Initializations **/
        AWSMobileClient.getInstance().initialize(this).execute();

        /** BLE Initializations **/
        BLEDataLinker.initialize(this);
        DataInterpreter.initialize(this);

        /* End of Bill's Section - Temporary */


        //Here is basic set up functions for MainActivity View
        initiateView();
        setColors();
        setConnection(BLE_DISCONNECT);
        setDrawer();

        //Test GPSs
        g1 = new GPS(-72.253981, 41.807741); //Uconn
        g1.setCo(20);
        g1.setDust(40);
        g1.setNo2(60);
        g1.setSo2(10);
        g1.setO3(30);
        g2 = new GPS(-72.250645, 41.803309); //Alumni Dorm
        g3 = new GPS(-72.253430, 41.804776); //Coop
        g4 = new GPS(-72.259929, 41.802675); //Hilltop Community Center
        g5 = new GPS(-72.251748, 41.806629); //Library
        Log.d("JIN", "GPS 문제");

        longitude = -111.11; // default longitude
        latitude = 222.0; // default latitude


//        //make the Loaction manager for gps
//        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        Toast.makeText(getApplicationContext(),"after get provider",Toast.LENGTH_LONG);
//        Log.d("JIN","LOCATION SERVICE IS OK" );
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Check Permissions Now
//            Log.d("JIN","CHECKING PERMSSION" );
//
//
//            // Check Permissions Now
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    REQUEST_LOCATION);
//            Log.d("JIN","Request Permission" );
//        }
//        else {
//            // permission has been granted, continue as usual
//            Log.d("JIN","Permission os granted" );
//
//        }
        Button btn_gotoMap = (Button) findViewById(R.id.button_gotomap);
        btn_gotoMap.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                intent_test = new Intent(MainActivity.this, MapsActivity.class);
                intent_test.putExtra("g1", g1);
                intent_test.putExtra("g2", g2);
                intent_test.putExtra("g3", g3);
                intent_test.putExtra("g4", g4);
                intent_test.putExtra("g5", g5);
                startActivity(intent_test);
            }
        });

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.main_relativelayout);
        mainLayout.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            @Override
            public void onSwipeLeft() {
                Log.d("onSwipeTouchListener", "onSwipeLeft");
                intent_test = new Intent(MainActivity.this, MapsActivity.class);
                intent_test.putExtra("g1", g1);
                intent_test.putExtra("g2", g2);
                intent_test.putExtra("g3", g3);
                intent_test.putExtra("g4", g4);
                intent_test.putExtra("g5", g5);
                startActivity(intent_test);
            }
        });

        Button btn_ble_scan = (Button) findViewById(R.id.button_ble);

        btn_ble_scan.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View V) {
                intent_ble = new Intent(MainActivity.this, BLEScanActivity.class);
                startActivity(intent_ble);
            }
        });

        Button btn_send_data = (Button) findViewById(R.id.button_senddata);
        btn_send_data.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View V) {
                DummyDataGenerator ddg = new DummyDataGenerator(SensorContentContract.Sensordata.PROJECTION_ALL);
                ContentValues values = ddg.generate(0.0, 3.0);
                values.put(SensorContentContract.Sensordata.DEVICEID, "dummydata");
                AsyncQueryHandler queryHandler = new AsyncQueryHandler(contentResolver) {
                    @Override
                    protected void onInsertComplete(int token, Object cookie, Uri uri) {
                        super.onInsertComplete(token, cookie, uri);
                        Log.d("DummyDataButtonPress", "insert completed");
                        Toast.makeText(getApplicationContext(), "Dummy Data Uploaded", Toast.LENGTH_LONG).show();

                    }
                };
                queryHandler.startInsert(INSERT_TOKEN, null, SensorContentContract.Sensordata.CONTENT_URI, values);


                final AnalyticsClient mgr = AWSProvider.getInstance()
                        .getPinpointManager()
                        .getAnalyticsClient();
                final AnalyticsEvent evt = mgr.createEvent("SendDummyData")
                        .withAttribute("packetId", values.getAsString(AqiContentContract.Aqidata.PACKETID));
                mgr.recordEvent(evt);
                mgr.submitEvents();
            }


        });

        Button gcdp = (Button) findViewById(R.id.gotoDPmaker);
        gcdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomDPmakerActivity.class);
                startActivity(intent);
            }
        });


    }

    public static boolean isStringDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void initiateView() {
        co_value = (TextView) findViewById(R.id.main_CO_value);
        o3_value = (TextView) findViewById(R.id.main_O3_value);
        no2_value = (TextView) findViewById(R.id.main_NO2_value);
        so2_value = (TextView) findViewById(R.id.main_SO2_value);
        pm_value = (TextView) findViewById(R.id.main_PM_value);
        connection_state = (TextView) findViewById(R.id.main_device_connectionState);


    }

    public void setColors() {


        co_value.setTextColor(Color.parseColor(info.getTextColor(Integer.parseInt(co_value.getText().toString()))));
        o3_value.setTextColor(Color.parseColor(info.getTextColor(Integer.parseInt(o3_value.getText().toString()))));
        no2_value.setTextColor(Color.parseColor(info.getTextColor(Integer.parseInt(no2_value.getText().toString()))));
        so2_value.setTextColor(Color.parseColor(info.getTextColor(Integer.parseInt(so2_value.getText().toString()))));
        pm_value.setTextColor(Color.parseColor(info.getTextColor(Integer.parseInt(pm_value.getText().toString()))));

    }


    public void setConnection(String state) {
        switch (state) {
            case BLE_CONNECT:
                connection_state.setText(BLE_CONNECT);
                connection_state.setTextColor(Color.parseColor("#FFBB80CB"));
                break;
            case BLE_DISCONNECT:
                connection_state.setText(BLE_DISCONNECT);
                connection_state.setTextColor(Color.parseColor("#FF8B8B8B"));
                break;
        }
    }

    public void setCoDisplay(int aqi) {

    }

    void setDrawer() {
        final String[] items = {"Go to Map", "Bloo", "Custom Data Maker"};
        ArrayList<String> drawlist = new ArrayList<>();
        drawlist.add("Go to Map");
        drawlist.add("Bluetooth");
        drawlist.add("Custom Data\n Maker");
        CustomAdapter adapter = new CustomAdapter(this, 0, drawlist);

        ListView listview = (ListView) findViewById(R.id.drawer_menulist);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                switch (position) {
                    case 0: // Go to Map
                        intent_test = new Intent(MainActivity.this, MapsActivity.class);
                        intent_test.putExtra("g1", g1);
                        intent_test.putExtra("g2", g2);
                        intent_test.putExtra("g3", g3);
                        intent_test.putExtra("g4", g4);
                        intent_test.putExtra("g5", g5);
                        startActivity(intent_test);
                        break;
                    case 1: // BLUETOOTH
                        intent_ble = new Intent(MainActivity.this, BLEScanActivity.class);
                        startActivity(intent_ble);
                        break;
                    case 2: // Custom Data maker
                        Intent intent = new Intent(MainActivity.this, CustomDPmakerActivity.class);
                        startActivity(intent);
                        break;


                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
                drawer.closeDrawer(Gravity.LEFT);


            }
        });
    }

    private class CustomAdapter extends ArrayAdapter<String> {
        private ArrayList<String> items;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.drawer_listview_layout, null);
            }


            ImageView imageView = (ImageView) v.findViewById(R.id.imageView);


            if (position == 0)
                imageView.setImageResource(R.drawable.ic_map_white_24dp);
            else if (position == 1)
                imageView.setImageResource(R.drawable.ic_bluetooth_white_24dp);
            else if (position == 2)
                imageView.setImageResource(R.drawable.ic_edit_location_white_24dp);
            TextView textView = (TextView) v.findViewById(R.id.textView);
            textView.setText(" " + items.get(position));


            return v;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        BLEDataLinker.getInstance().close();
    }


}
