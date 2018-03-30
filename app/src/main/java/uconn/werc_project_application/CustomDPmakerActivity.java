package uconn.werc_project_application;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.nio.channels.SelectionKey;
import java.text.SimpleDateFormat;
import java.util.Date;

import uconn.werc_project_application.data.Datapoint;

public class CustomDPmakerActivity extends AppCompatActivity {

    private EditText PacketId_Edit, UserId_Edit, Device_Edit;
    private TextView CO_textview, NO2_textview, O3_textview, PM_textview, SO2_textview;
    private SeekBar CO_seekbar, NO2_seekbar, O3_seekbar, PM_seekbar, SO2_seekbar;
    private Button save_btn;
    private TextView GPS_Lat_textview, GPS_Long_textview;
    int REQUEST_LOCATION = 2;
    private double longitude, latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_dpmaker);

        initialviews();
        initial_seekbar();
        initial_GPSs();
        initial_Button();


    }

    void initialviews() {
        PacketId_Edit = (EditText) findViewById(R.id.CDP_PacketId_Edit);
        UserId_Edit = (EditText) findViewById(R.id.CDP_UderID_Edit);
        Device_Edit = (EditText) findViewById(R.id.CDP_DeviceID_Edit);
        CO_textview = (TextView) findViewById(R.id.CDP_CO_value_textview);
        CO_seekbar = (SeekBar) findViewById(R.id.CDP_CO_SeekBar);
        NO2_textview = (TextView) findViewById(R.id.CDP_NO2_value_textview);
        NO2_seekbar = (SeekBar) findViewById(R.id.CDP_NO2_SeekBar);
        O3_seekbar = (SeekBar) findViewById(R.id.CDP_O3_SeekBar);
        O3_textview = (TextView) findViewById(R.id.CDP_O3_value_textview);
        PM_seekbar = (SeekBar) findViewById(R.id.CDP_PM_SeekBar);
        PM_textview = (TextView) findViewById(R.id.CDP_PM_value_textview);
        SO2_seekbar = (SeekBar) findViewById(R.id.CDP_SO2_SeekBar);
        SO2_textview = (TextView) findViewById(R.id.CDP_SO2_value_textview);
        save_btn = (Button) findViewById(R.id.CDP_SAVE_Button);
        GPS_Lat_textview = (TextView) findViewById(R.id.CDP_GPS_Lat_textview);
        GPS_Long_textview = (TextView) findViewById(R.id.CDP_GPS_Long_textview);

    }

    void initial_seekbar() {
        CO_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                CO_textview.setText(Integer.toString(progress));
                CO_textview.setTextColor(Color.parseColor(getTextColor(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        NO2_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                NO2_textview.setText(Integer.toString(progress));
                NO2_textview.setTextColor(Color.parseColor(getTextColor(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        O3_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                O3_textview.setText(Integer.toString(progress));
                O3_textview.setTextColor(Color.parseColor(getTextColor(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PM_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                PM_textview.setText(Integer.toString(progress));
                PM_textview.setTextColor(Color.parseColor(getTextColor(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        SO2_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SO2_textview.setText(Integer.toString(progress));
                SO2_textview.setTextColor(Color.parseColor(getTextColor(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public String getTextColor(int value) {
        final String SAFE_COLOUR = "#7CFC00";
        final String MODERATE_COLOUR = "#FFFF00";
        final String USG_COLOUR = "#FF8C00";
        final String UNHEALTHY_COLOUR = "#000080";
        final String VERY_UNHEALTHY_COLOUR = "#4B0082";
        final String HAZARDOUS_COLOUR = "#FF0000";

        if (value >= 0 && value <= 50) {
            return SAFE_COLOUR;
        }
        if (value >= 51 && value <= 100) {
            return MODERATE_COLOUR;
        }
        if (value >= 101 && value <= 150) {
            return USG_COLOUR;
        }
        if (value >= 151 && value <= 200) {
            return UNHEALTHY_COLOUR;
        }
        if (value >= 201 && value <= 300) {
            return VERY_UNHEALTHY_COLOUR;
        }
        return HAZARDOUS_COLOUR;
    }

    public void initial_GPSs() {

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
                    Toast.makeText(getApplicationContext(), "Location Acquired", Toast.LENGTH_LONG).show();
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    GPS_Lat_textview.setText(Double.toString(location.getLatitude()));
                    GPS_Long_textview.setText(Double.toString(location.getLongitude()));
                } else {
                    Toast.makeText(getApplicationContext(), "No location", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void initial_Button(){


        Button changeLoc_btn = (Button)findViewById(R.id.CDP_ChangeLocation_btn);
        changeLoc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomDPmakerActivity.this,GetLocationMapAcitivy.class);
                intent.putExtra("Long",longitude);
                intent.putExtra("Lat",latitude);
                startActivityForResult(intent,0);
            }
        });
        //get time function
        final long now = System.currentTimeMillis();


        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Datapoint datapoint = new Datapoint();
                datapoint.setPacketId(PacketId_Edit.getText().toString());
                datapoint.setUserId(UserId_Edit.getText().toString());
                datapoint.setDeviceId(Device_Edit.getText().toString());
                datapoint.setTime(now);

                //Here is a problem the lat and long should be double not long
                //if u fix long to double then u can use this method
               //datapoint.setGps_lat(Double.parseDouble(GPS_Lat_textview.getText().toString()));
               //datapoint.setGps_long(Double.parseDouble(GPS_Long_textview.getText().toString()));
                datapoint.setSensor_co(Long.parseLong(CO_textview.getText().toString()));
                datapoint.setSensor_no2(Long.parseLong(NO2_textview.getText().toString()));
                datapoint.setSensor_o3(Long.parseLong(O3_textview.getText().toString()));
                datapoint.setSensor_pm(Long.parseLong(PM_textview.getText().toString()));
                datapoint.setSensor_so2(Long.parseLong(SO2_textview.getText().toString()));

                //check how does it work
                Log.d("JIN","Start Checking" );
                Log.d("JIN, PacketID",PacketId_Edit.getText().toString());
                Log.d("JIN, UserID",UserId_Edit.getText().toString() );
                Log.d("JIN, DeviceID",Device_Edit.getText().toString() );
                Log.d("JIN, Time",Long.toString(now) );
                Log.d("JIN, GPS_Lat",GPS_Lat_textview.getText().toString() );
                Log.d("JIN, GPS_long",GPS_Long_textview.getText().toString() );
                Log.d("JIN, CO",CO_textview.getText().toString());
                Log.d("JIN, NO2",NO2_textview.getText().toString() );
                Log.d("JIN, O3",O3_textview.getText().toString());
                Log.d("JIN, PM",PM_textview.getText().toString());
                Log.d("JIN, SO2",SO2_textview.getText().toString());


                //part to save data to server ****
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        longitude = data.getDoubleExtra("Long",0);
        latitude = data.getDoubleExtra("Lat",0);

        GPS_Lat_textview.setText(Double.toString(latitude));
        GPS_Long_textview.setText(Double.toString(longitude));
    }
}
