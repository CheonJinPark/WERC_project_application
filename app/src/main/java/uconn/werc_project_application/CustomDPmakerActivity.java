package uconn.werc_project_application;

import android.Manifest;
import android.app.ActionBar;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.location.Location;
import android.net.Uri;
import android.provider.ContactsContract;
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
import java.util.UUID;

import uconn.werc_project_application.data.AqiContentContract;
import uconn.werc_project_application.data.DataInterpreter;

import uconn.werc_project_application.data.DummyDataGenerator;
import uconn.werc_project_application.data.SensorContentContract;

public class CustomDPmakerActivity extends AppCompatActivity {

    private EditText PacketId_Edit, UserId_Edit, Device_Edit;
    private TextView CO_textview, NO2_textview, O3_textview, PM_textview, SO2_textview;
    private SeekBar CO_seekbar, NO2_seekbar, O3_seekbar, PM_seekbar, SO2_seekbar;
    private Button save_btn;
    private TextView GPS_Lat_textview, GPS_Long_textview;
    int REQUEST_LOCATION = 2;
    private static final int INSERT_TOKEN = 1003;
    private double longitude, latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_dpmaker);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);

        initialviews();
        initial_seekbar();
//        initial_GPSs();
        initial_Button();

        longitude = DataInterpreter.getInstance().getGpsLong();
        latitude = DataInterpreter.getInstance().getGpsLat();
        GPS_Lat_textview.setText(Double.toString(DataInterpreter.getInstance().getGpsLat()));
        GPS_Long_textview.setText(Double.toString(DataInterpreter.getInstance().getGpsLong()));


    }

    void initialviews() {
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

//    public void initial_GPSs() {
//
//        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
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
//
//        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                // Got last known location. In some rare situations this can be null.
//                if (location != null) {
//                    Toast.makeText(getApplicationContext(), "Location Acquired", Toast.LENGTH_LONG).show();
//                    longitude = location.getLongitude();
//                    latitude = location.getLatitude();
//                    GPS_Lat_textview.setText(Double.toString(location.getLatitude()));
//                    GPS_Long_textview.setText(Double.toString(location.getLongitude()));
//                } else {
//                    Toast.makeText(getApplicationContext(), "No location", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }
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
                DummyDataGenerator ddg = new DummyDataGenerator(AqiContentContract.Aqidata.PROJECTION_ALL);
                ContentValues cv = ddg.generate(0.0, 3.0);

                cv.put(AqiContentContract.Aqidata.DEVICEID, "customdp");


                //Here is a problem the lat and long should be double not long
                //if u fix long to double then u can use this method
               //datapoint.setGps_lat(Double.parseDouble(GPS_Lat_textview.getText().toString()));
               //datapoint.setGps_long(Double.parseDouble(GPS_Long_textview.getText().toString()));
                cv.put(AqiContentContract.Aqidata.GPSLAT, latitude);
                cv.put(AqiContentContract.Aqidata.GPSLONG, longitude);
                cv.put(AqiContentContract.Aqidata.SENSORAQICO, Double.parseDouble(CO_textview.getText().toString()));
                cv.put(AqiContentContract.Aqidata.SENSORAQINO2, Double.parseDouble(NO2_textview.getText().toString()));
                cv.put(AqiContentContract.Aqidata.SENSORAQIO3, Double.parseDouble(O3_textview.getText().toString()));
                cv.put(AqiContentContract.Aqidata.SENSORAQISO2, Double.parseDouble(SO2_textview.getText().toString()));
                cv.put(AqiContentContract.Aqidata.SENSORAQIPM, Double.parseDouble(PM_textview.getText().toString()));
                Double co_aqi = cv.getAsDouble(AqiContentContract.Aqidata.SENSORAQICO);
                Double no2_aqi = cv.getAsDouble(AqiContentContract.Aqidata.SENSORAQINO2);
                Double o3_aqi = cv.getAsDouble(AqiContentContract.Aqidata.SENSORAQIO3);
                Double so2_aqi = cv.getAsDouble(AqiContentContract.Aqidata.SENSORAQISO2);
                Double pm_aqi = cv.getAsDouble(AqiContentContract.Aqidata.SENSORAQIPM);
                Double pml_aqi = cv.getAsDouble(AqiContentContract.Aqidata.SENSORAQIPML);
                // Determine Largest AQI Value and Source
                if (co_aqi >= no2_aqi && co_aqi >= o3_aqi && co_aqi >= so2_aqi && co_aqi >= pm_aqi && co_aqi >= pml_aqi) {
                    cv.put(AqiContentContract.Aqidata.AQISRC, "Carbon Monoxide");
                    cv.put(AqiContentContract.Aqidata.AQIVAL, co_aqi);
                } else if (no2_aqi >= co_aqi && no2_aqi >= o3_aqi && no2_aqi >= so2_aqi && no2_aqi >= pm_aqi && no2_aqi >= pml_aqi) {
                    cv.put(AqiContentContract.Aqidata.AQISRC, "Nitrogen Dioxide");
                    cv.put(AqiContentContract.Aqidata.AQIVAL, no2_aqi);
                } else if (o3_aqi >= co_aqi && o3_aqi >= no2_aqi && o3_aqi >= so2_aqi && o3_aqi >= pm_aqi && o3_aqi >= pml_aqi) {
                    cv.put(AqiContentContract.Aqidata.AQISRC, "Ozone");
                    cv.put(AqiContentContract.Aqidata.AQIVAL, o3_aqi);
                } else if (so2_aqi >= co_aqi && so2_aqi >= o3_aqi && so2_aqi >= no2_aqi && so2_aqi >= pm_aqi && so2_aqi >= pml_aqi) {
                    cv.put(AqiContentContract.Aqidata.AQISRC, "Sulfur Dioxide");
                    cv.put(AqiContentContract.Aqidata.AQIVAL, so2_aqi);
                } else if (pm_aqi >= co_aqi && pm_aqi >= o3_aqi && pm_aqi >= no2_aqi && pm_aqi >= so2_aqi && pm_aqi >= pml_aqi) {
                    cv.put(AqiContentContract.Aqidata.AQISRC, "Small Particulate Matter");
                    cv.put(AqiContentContract.Aqidata.AQIVAL, pm_aqi);
                } else if (pml_aqi >= co_aqi && pml_aqi >= o3_aqi && pml_aqi >= so2_aqi && pml_aqi >= pm_aqi && pml_aqi >= no2_aqi ) {
                    cv.put(AqiContentContract.Aqidata.AQISRC, "Large Particulate Matter");
                    cv.put(AqiContentContract.Aqidata.AQIVAL, pml_aqi);
                } else {
                    cv.put(AqiContentContract.Aqidata.AQISRC, "Unknown. How did you find this");
                    cv.put(AqiContentContract.Aqidata.AQIVAL, co_aqi);
                }

                //check how does it work


                //part to save data to server ****
                ContentResolver contentResolver = getApplicationContext().getContentResolver();
                AsyncQueryHandler queryHandler = new AsyncQueryHandler(contentResolver) {
                    @Override
                    protected void onInsertComplete(int token, Object cookie, Uri uri) {
                        super.onInsertComplete(token, cookie, uri);
                        Log.d("CustomDPMakerActivity", "Custom Datapoint Uploaded");
                        Toast.makeText(getApplicationContext(),"Custom Datapoint Uploaded",Toast.LENGTH_LONG).show();

                    }
                };
                queryHandler.startInsert(INSERT_TOKEN, null, AqiContentContract.Aqidata.CONTENT_URI, cv);
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
