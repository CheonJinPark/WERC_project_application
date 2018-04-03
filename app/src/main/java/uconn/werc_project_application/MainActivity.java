package uconn.werc_project_application;

import android.Manifest;
import android.app.Activity;
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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import uconn.werc_project_application.ble.BLEDataLinker;
import uconn.werc_project_application.data.DummyDataGenerator;
import uconn.werc_project_application.data.SendDataService;
import uconn.werc_project_application.data.SensorContentContract;


public class MainActivity extends AppCompatActivity{

    Double longitude, latitude;
    Intent intent_test, intent_ble;
    String url;
    LocationManager locationManager;
    int REQUEST_LOCATION = 2;
    GPS g1, g2, g3, g4, g5;
    private static final int INSERT_TOKEN = 1003;
    TextView userName,apiValue, co_value,o3_value,no2_value,so2_value,pm_value,connection_state,data_point;
    Information info = new Information();
    final String CONNECT = "connected", DISCONNECT = "disconnected";

    private ContentResolver contentResolver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("JIN","View는 만듬" );

        // Install the application crash handler.
        ApplicationCrashHandler.installHandler();
        contentResolver = getApplicationContext().getContentResolver();


        /* Bill's Section - Temporary */
        /** AWS Initializations **/
        AWSMobileClient.getInstance().initialize(this).execute();

        /** BLE Initializations **/
        BLEDataLinker.initialize(this);
        startService(new Intent(this, SendDataService.class));

        /* End of Bill's Section - Temporary */


        //Here is basic set up functions for MainActivity View
        initiateView();
        setColors();
        setUserName("Bill");
        setAQI(48);
        setConnection(DISCONNECT);
        setDataPoints(224);


        //Test GPSs
        g1 = new GPS(-72.253981,41.807741); //Uconn
        g1.setCo(20);
        g1.setDust(40);
        g1.setNo2(60);
        g1.setSo2(10);
        g1.setO3(30);
        g2 = new GPS(-72.250645,41.803309); //Alumni Dorm
        g3 = new GPS(-72.253430,41.804776); //Coop
        g4 = new GPS(-72.259929,41.802675); //Hilltop Community Center
        g5 = new GPS(-72.251748,41.806629); //Library
        Log.d("JIN","GPS 문제" );

        longitude = -111.11; // default longitude
        latitude = 222.0; // default latitude


        //make the Loaction manager for gps
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Toast.makeText(getApplicationContext(),"after get provider",Toast.LENGTH_LONG);
        Log.d("JIN","LOCATION SERVICE IS OK" );
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            Log.d("JIN","CHECKING PERMSSION" );


            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
            Log.d("JIN","Request Permission" );
        }
        else {
            // permission has been granted, continue as usual
            Log.d("JIN","Permission os granted" );

        }
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
                DummyDataGenerator.initialize(SensorContentContract.Sensordata.PROJECTION_ALL);
                ContentValues values = DummyDataGenerator.getInstance().generate(0.0, 3.0);
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
                        .withAttribute("packetId", values.getAsString(SensorContentContract.Sensordata.SENSORCO));
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

    void initiateView(){
        userName = (TextView)findViewById(R.id.main_userName);
        apiValue = (TextView)findViewById(R.id.main_api_value);
        co_value = (TextView)findViewById(R.id.main_CO_value);
        o3_value = (TextView)findViewById(R.id.main_O3_value);
        no2_value = (TextView)findViewById(R.id.main_NO2_value);
        so2_value = (TextView)findViewById(R.id.main_SO2_value);
        pm_value = (TextView)findViewById(R.id.main_PM_value);
        connection_state = (TextView)findViewById(R.id.main_device_connectionState);
        data_point = (TextView)findViewById(R.id.main_data_point);


    }
    void setColors(){


        co_value.setTextColor(Color.parseColor(info.getTextColor(Integer.parseInt(co_value.getText().toString()))));
        o3_value.setTextColor(Color.parseColor(info.getTextColor(Integer.parseInt(o3_value.getText().toString()))));
        no2_value.setTextColor(Color.parseColor(info.getTextColor(Integer.parseInt(no2_value.getText().toString()))));
        so2_value.setTextColor(Color.parseColor(info.getTextColor(Integer.parseInt(so2_value.getText().toString()))));
        pm_value.setTextColor(Color.parseColor(info.getTextColor(Integer.parseInt(pm_value.getText().toString()))));

    }
    void setUserName(String name){
        userName.setText(name+"!");
    }
    void setAQI(int value){
        apiValue.setText(Integer.toString(value));
        apiValue.setTextColor(Color.parseColor(info.getTextColor(value)));
    }
    void setConnection(String state) {
        switch (state) {
            case CONNECT:
                connection_state.setText(CONNECT);
                connection_state.setTextColor(Color.parseColor("#7CFC00"));
                break;
            case DISCONNECT:
                connection_state.setText(DISCONNECT);
                connection_state.setTextColor(Color.parseColor("#FF0000"));
                break;
        }
    }
    void setDataPoints(int value){
        data_point.setText(Integer.toString(value)+ " data points");
    }




}
