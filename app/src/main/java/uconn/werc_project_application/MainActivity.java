package uconn.werc_project_application;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
// AWS Database
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.s3.transferutility.*;
import com.amazonaws.models.nosql.GpsdataDO;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity{

    Double longitude, latitude;
    Intent intent_test, intent_ble;
    String url;
    LocationManager locationManager;
    int REQUEST_LOCATION = 2;
    GPS g1, g2, g3, g4, g5;

    TextView long_textview, lat_textview;
    /** AWS Global Variables **/
    // AWS Pinpoint Data Analytics
    public static PinpointManager pinpointManager;
    // AWS DynamoDB
    DynamoDBMapper dynamoDBMapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("JIN","View는 만듬" );

        //Test GPSs
       g1 = new GPS(-72.253981,41.807741); //Uconn
       g2 = new GPS(-72.250645,41.803309); //Alumni Dorm
       g3 = new GPS(-72.253430,41.804776); //Coop
       g4 = new GPS(-72.259929,41.802675); //Hilltop Community Center
       g5 = new GPS(-72.251748,41.806629); //Library
        Log.d("JIN","GPS 문제" );
        /** AWS Initializations **/
        AWSMobileClient.getInstance().initialize(this).execute();
        // AWS Pinpoint Data Analytics
        PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                getApplicationContext(),
                AWSMobileClient.getInstance().getCredentialsProvider(),
                AWSMobileClient.getInstance().getConfiguration());

        pinpointManager = new PinpointManager(pinpointConfig);

        // AWS DynamoDB
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .build();

        longitude = -111.11; // default longitude
        latitude = 222.0; // default latitude


        long_textview = (TextView)findViewById(R.id.Textview_gps_test_longitude);
        lat_textview = (TextView)findViewById(R.id.Textview_gps_test_latitude);
        // Start a session with Pinpoint
        pinpointManager.getSessionClient().startSession();

        // Stop the session and submit the default app started event
        pinpointManager.getSessionClient().stopSession();
        pinpointManager.getAnalyticsClient().submitEvents();


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

        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    Toast.makeText(getApplicationContext(),"Got location",Toast.LENGTH_LONG).show();
                    lat_textview.setText(Double.toString(location.getLatitude()));
                    long_textview.setText(Double.toString(location.getLongitude()));
                }
                else{
                    Toast.makeText(getApplicationContext(),"No location",Toast.LENGTH_LONG).show();
                }
            }
        });







     // gps_textview = (TextView) findViewById(R.id.textview_gpd_result);
       // gps_textview.setText("GPS is not running");
       // gps_textview.setText("Logitude : " + Double.toString(longitude)+" Latitude : "+Double.toString(latitude));



        Button Send = (Button) findViewById(R.id.Button_Send);


        Send.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                EditText Edit_Longitude = (EditText) findViewById(R.id.EditText_Longitude);
                EditText Edit_Latitude = (EditText) findViewById(R.id.EditText_Latitude);

                String string_long = Edit_Longitude.getText().toString();
                String string_lat = Edit_Latitude.getText().toString();

                if (isStringDouble(string_long)) {
                    longitude = Double.parseDouble(string_long);
                    Toast.makeText(getApplicationContext(), "Longitude is saved", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Logitude is not correct format", Toast.LENGTH_SHORT).show();
                }

                if (isStringDouble(string_lat)) {
                    latitude = Double.parseDouble(string_lat);
                    Toast.makeText(getApplicationContext(), "Latitude is saved", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Latitude is not correct format", Toast.LENGTH_SHORT).show();
                }
                createGdata(longitude,latitude);


            }
        });

        Button GotoMap = (Button) findViewById(R.id.button_gotomap);
        GotoMap.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                intent_test = new Intent(MainActivity.this, MapsActivity.class);
                intent_test.putExtra("g1",g1);
                intent_test.putExtra("g2",g2);
                intent_test.putExtra("g3",g3);
                intent_test.putExtra("g4",g4);
                intent_test.putExtra("g5",g5);
                startActivity(intent_test);
            }
        });
        Button btn_ble_scan = (Button)findViewById(R.id.button_ble);

        btn_ble_scan.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View V)
            {
                intent_ble = new Intent(MainActivity.this, BLEScanActivity.class);
                startActivity(intent_ble);
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
    public void createGdata(double l, double lat){
        final GpsdataDO gdata = new GpsdataDO();

        gdata.setUserId("userID");
        //originally in setUserID : identityManager.getCachedUserID()
        gdata.setDeviceId("Android");
        gdata.setTimeEpoch(00.11);
        gdata.setGpsLat(lat);
        gdata.setGpsLong(l);

        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamoDBMapper.save(gdata);
                // Item saved
            }
        }).start();
    }


}
