package uconn.werc_project_application;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.ProgressBar;
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


public class MainActivity extends AppCompatActivity{
    /*
        Constants
     */
    private static final String TAG = "MainActivity";

    // Colors
    private static final String MAROON = "#B03060";
    private static final String PURPLE = "#7851A9";
    private static final String RED = "#CE2029";
    private static final String ORANGE = "#FF9966";
    private static final String YELLOW = "#FFD300";
    private static final String GREEN = "#03C03C";

    Double longitude, latitude;
    Intent intent_test, intent_ble;
    String url;
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
                startActivity(intent_test);
            }
        });

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.main_relativelayout);
        mainLayout.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            @Override
            public void onSwipeLeft() {
                Log.d("onSwipeTouchListener", "onSwipeLeft");
                intent_test = new Intent(MainActivity.this, MapsActivity.class);
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

        final TextView aqi_src = (TextView) findViewById(R.id.tv_aqi_src);

        final TextView aqi_val = (TextView) findViewById(R.id.tv_aqi_val);
        aqi_val.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage(DataInterpreter.getInstance().getAdvisory(Integer.parseInt(aqi_val.getText().toString()), aqi_src.getText().toString()));
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


                final AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        final TextView aqi_str = (TextView) findViewById(R.id.tv_aqi);
        aqi_str.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage(DataInterpreter.getInstance().getAdvisory(Integer.parseInt(aqi_val.getText().toString()), aqi_src.getText().toString()));
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


                final AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });



        setCoDisplay(40);
        setO3Display(80);
        setNo2Display(22);
        setSo2Display(156);
        setPmDisplay(350);

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
                connection_state.setTypeface(null, Typeface.BOLD);
                break;
            case BLE_DISCONNECT:
                connection_state.setText(BLE_DISCONNECT);
                connection_state.setTextColor(Color.parseColor("#FF8B8B8B"));
                connection_state.setTypeface(null, Typeface.NORMAL);
                break;
        }
    }

    public void updateDisplay(ContentValues cv)
    {
        setAqiDisplay(cv.getAsDouble(AqiContentContract.Aqidata.AQIVAL).intValue(), cv.getAsString(AqiContentContract.Aqidata.AQISRC));
        setCoDisplay(cv.getAsDouble(AqiContentContract.Aqidata.SENSORAQICO).intValue());
        setNo2Display(cv.getAsDouble(AqiContentContract.Aqidata.SENSORAQINO2).intValue());
        setO3Display(cv.getAsDouble(AqiContentContract.Aqidata.SENSORAQIO3).intValue());
        setSo2Display(cv.getAsDouble(AqiContentContract.Aqidata.SENSORAQISO2).intValue());
        setPmDisplay(cv.getAsDouble(AqiContentContract.Aqidata.SENSORAQIPM).intValue());

    }

    private void setAqiDisplay(int aqi_val, String aqi_src)
    {
        TextView tv_aqi_val = (TextView) findViewById(R.id.tv_aqi_val);
        TextView tv_aqi = (TextView) findViewById(R.id.tv_aqi);
        ProgressBar pb_aqi = (ProgressBar) findViewById(R.id.pb_aqi);

        tv_aqi_val.setText(Integer.toString(aqi_val));
        if (aqi_val > 300) {
            tv_aqi_val.setTextColor(Color.parseColor(MAROON));
            tv_aqi.setTextColor(Color.parseColor(MAROON));
            pb_aqi.setMax(200);
            pb_aqi.setProgress((aqi_val - 300) , true);
            pb_aqi.setProgressTintList(ColorStateList.valueOf(Color.parseColor(MAROON)));
        } else if (aqi_val > 200) {
            tv_aqi_val.setTextColor(Color.parseColor(PURPLE));
            tv_aqi.setTextColor(Color.parseColor(PURPLE));
            pb_aqi.setMax(100);
            pb_aqi.setProgress((aqi_val - 200) , true);
            pb_aqi.setProgressTintList(ColorStateList.valueOf(Color.parseColor(PURPLE)));
        } else if (aqi_val > 150) {
            tv_aqi_val.setTextColor(Color.parseColor(RED));
            tv_aqi.setTextColor(Color.parseColor(RED));
            pb_aqi.setMax(50);
            pb_aqi.setProgress((aqi_val - 150) , true);
            pb_aqi.setProgressTintList(ColorStateList.valueOf(Color.parseColor(RED)));
        } else if (aqi_val > 100) {
            tv_aqi_val.setTextColor(Color.parseColor(ORANGE));
            tv_aqi.setTextColor(Color.parseColor(ORANGE));
            pb_aqi.setMax(50);
            pb_aqi.setProgress((aqi_val - 100) , true);
            pb_aqi.setProgressTintList(ColorStateList.valueOf(Color.parseColor(ORANGE)));
        } else if (aqi_val > 50) {
            tv_aqi_val.setTextColor(Color.parseColor(YELLOW));
            tv_aqi.setTextColor(Color.parseColor(YELLOW));
            pb_aqi.setMax(50);
            pb_aqi.setProgress((aqi_val - 50) , true);
            pb_aqi.setProgressTintList(ColorStateList.valueOf(Color.parseColor(YELLOW)));
        } else if (aqi_val >= 0) {
            tv_aqi_val.setTextColor(Color.parseColor(GREEN));
            tv_aqi.setTextColor(Color.parseColor(GREEN));
            pb_aqi.setProgress(aqi_val);
            pb_aqi.setProgressTintList(ColorStateList.valueOf(Color.parseColor(GREEN)));
        } else {
            tv_aqi_val.setTextColor(Color.parseColor(GREEN));
            tv_aqi.setTextColor(Color.parseColor(GREEN));
            pb_aqi.setMax(50);
            pb_aqi.setProgress(aqi_val);
            pb_aqi.setProgressTintList(ColorStateList.valueOf(Color.parseColor(GREEN)));
        }

    }

    public void setCoDisplay(int aqi) {
        TextView tv_co_val = (TextView) findViewById(R.id.main_CO_value);
        ProgressBar pb_co = (ProgressBar) findViewById(R.id.pb_co);
        Drawable drawable = pb_co.getProgressDrawable();
        tv_co_val.setText(Integer.toString(aqi));
        if (aqi > 300) {
            tv_co_val.setTextColor(Color.parseColor(MAROON));
            pb_co.setMax(200);
            pb_co.setProgress((aqi - 300) , true);
            pb_co.setProgressTintList(ColorStateList.valueOf(Color.parseColor(MAROON)));
        } else if (aqi > 200) {
            tv_co_val.setTextColor(Color.parseColor(PURPLE));
            pb_co.setMax(100);
            pb_co.setProgress((aqi - 200) , true);
            pb_co.setProgressTintList(ColorStateList.valueOf(Color.parseColor(PURPLE)));
        } else if (aqi > 150) {
            tv_co_val.setTextColor(Color.parseColor(RED));
            pb_co.setMax(50);
            pb_co.setProgress((aqi - 150) , true);
            pb_co.setProgressTintList(ColorStateList.valueOf(Color.parseColor(RED)));
        } else if (aqi > 100) {
            tv_co_val.setTextColor(Color.parseColor(ORANGE));
            pb_co.setMax(50);
            pb_co.setProgress((aqi - 100) , true);
            pb_co.setProgressTintList(ColorStateList.valueOf(Color.parseColor(ORANGE)));
        } else if (aqi > 50) {
            tv_co_val.setTextColor(Color.parseColor(YELLOW));
            pb_co.setMax(50);
            pb_co.setProgress((aqi - 50) , true);
            pb_co.setProgressTintList(ColorStateList.valueOf(Color.parseColor(YELLOW)));
        } else if (aqi >= 0) {
            tv_co_val.setTextColor(Color.parseColor(GREEN));
            pb_co.setProgress(aqi);
            pb_co.setProgressTintList(ColorStateList.valueOf(Color.parseColor(GREEN)));
        } else {
            tv_co_val.setTextColor(Color.parseColor(GREEN));
            pb_co.setMax(50);
            pb_co.setProgress(aqi);
            pb_co.setProgressTintList(ColorStateList.valueOf(Color.parseColor(GREEN)));
        }
    }

    public void setNo2Display(int aqi) {
        TextView tv_no2_val = (TextView) findViewById(R.id.main_NO2_value);
        ProgressBar pb_no2 = (ProgressBar) findViewById(R.id.pb_no2);
        Drawable drawable = pb_no2.getProgressDrawable();
        tv_no2_val.setText(Integer.toString(aqi));
        if (aqi > 300) {
            tv_no2_val.setTextColor(Color.parseColor(MAROON));
            pb_no2.setMax(200);
            pb_no2.setProgress((aqi - 300) , true);
            pb_no2.setProgressTintList(ColorStateList.valueOf(Color.parseColor(MAROON)));
        } else if (aqi > 200) {
            tv_no2_val.setTextColor(Color.parseColor(PURPLE));
            pb_no2.setMax(100);
            pb_no2.setProgress((aqi - 200) , true);
            pb_no2.setProgressTintList(ColorStateList.valueOf(Color.parseColor(PURPLE)));
        } else if (aqi > 150) {
            tv_no2_val.setTextColor(Color.parseColor(RED));
            pb_no2.setMax(50);
            pb_no2.setProgress((aqi - 150) , true);
            pb_no2.setProgressTintList(ColorStateList.valueOf(Color.parseColor(RED)));
        } else if (aqi > 100) {
            tv_no2_val.setTextColor(Color.parseColor(ORANGE));
            pb_no2.setMax(50);
            pb_no2.setProgress((aqi - 100) , true);
            pb_no2.setProgressTintList(ColorStateList.valueOf(Color.parseColor(ORANGE)));
        } else if (aqi > 50) {
            tv_no2_val.setTextColor(Color.parseColor(YELLOW));
            pb_no2.setMax(50);
            pb_no2.setProgress((aqi - 50) , true);
            pb_no2.setProgressTintList(ColorStateList.valueOf(Color.parseColor(YELLOW)));
        } else if (aqi >= 0) {
            tv_no2_val.setTextColor(Color.parseColor(GREEN));
            pb_no2.setProgress(aqi);
            pb_no2.setProgressTintList(ColorStateList.valueOf(Color.parseColor(GREEN)));
        } else {
            tv_no2_val.setTextColor(Color.parseColor(GREEN));
            pb_no2.setMax(50);
            pb_no2.setProgress(aqi);
            pb_no2.setProgressTintList(ColorStateList.valueOf(Color.parseColor(GREEN)));
        }
    }

    public void setO3Display(int aqi) {
        TextView tv_o3_val = (TextView) findViewById(R.id.main_O3_value);
        ProgressBar pb_o3 = (ProgressBar) findViewById(R.id.pb_o3);
        Drawable drawable = pb_o3.getProgressDrawable();
        tv_o3_val.setText(Integer.toString(aqi));
        if (aqi > 300) {
            tv_o3_val.setTextColor(Color.parseColor(MAROON));
            pb_o3.setMax(200);
            pb_o3.setProgress((aqi - 300) , true);
            pb_o3.setProgressTintList(ColorStateList.valueOf(Color.parseColor(MAROON)));
        } else if (aqi > 200) {
            tv_o3_val.setTextColor(Color.parseColor(PURPLE));
            pb_o3.setMax(100);
            pb_o3.setProgress((aqi - 200) , true);
            pb_o3.setProgressTintList(ColorStateList.valueOf(Color.parseColor(PURPLE)));
        } else if (aqi > 150) {
            tv_o3_val.setTextColor(Color.parseColor(RED));
            pb_o3.setMax(50);
            pb_o3.setProgress((aqi - 150) , true);
            pb_o3.setProgressTintList(ColorStateList.valueOf(Color.parseColor(RED)));
        } else if (aqi > 100) {
            tv_o3_val.setTextColor(Color.parseColor(ORANGE));
            pb_o3.setMax(50);
            pb_o3.setProgress((aqi - 100) , true);
            pb_o3.setProgressTintList(ColorStateList.valueOf(Color.parseColor(ORANGE)));
        } else if (aqi > 50) {
            tv_o3_val.setTextColor(Color.parseColor(YELLOW));
            pb_o3.setMax(50);
            pb_o3.setProgress((aqi - 50) , true);
            pb_o3.setProgressTintList(ColorStateList.valueOf(Color.parseColor(YELLOW)));
        } else if (aqi >= 0) {
            tv_o3_val.setTextColor(Color.parseColor(GREEN));
            pb_o3.setProgress(aqi);
            pb_o3.setProgressTintList(ColorStateList.valueOf(Color.parseColor(GREEN)));
        } else {
            tv_o3_val.setTextColor(Color.parseColor(GREEN));
            pb_o3.setMax(50);
            pb_o3.setProgress(aqi);
            pb_o3.setProgressTintList(ColorStateList.valueOf(Color.parseColor(GREEN)));
        }
    }

    public void setSo2Display(int aqi) {
        TextView tv_so2_val = (TextView) findViewById(R.id.main_SO2_value);
        ProgressBar pb_So2 = (ProgressBar) findViewById(R.id.pb_so2);
        Drawable drawable = pb_So2.getProgressDrawable();
        tv_so2_val.setText(Integer.toString(aqi));
        if (aqi > 300) {
            tv_so2_val.setTextColor(Color.parseColor(MAROON));
            pb_So2.setMax(200);
            pb_So2.setProgress((aqi - 300) , true);
            pb_So2.setProgressTintList(ColorStateList.valueOf(Color.parseColor(MAROON)));
        } else if (aqi > 200) {
            tv_so2_val.setTextColor(Color.parseColor(PURPLE));
            pb_So2.setMax(100);
            pb_So2.setProgress((aqi - 200) , true);
            pb_So2.setProgressTintList(ColorStateList.valueOf(Color.parseColor(PURPLE)));
        } else if (aqi > 150) {
            tv_so2_val.setTextColor(Color.parseColor(RED));
            pb_So2.setMax(50);
            pb_So2.setProgress((aqi - 150) , true);
            pb_So2.setProgressTintList(ColorStateList.valueOf(Color.parseColor(RED)));
        } else if (aqi > 100) {
            tv_so2_val.setTextColor(Color.parseColor(ORANGE));
            pb_So2.setMax(50);
            pb_So2.setProgress((aqi - 100) , true);
            pb_So2.setProgressTintList(ColorStateList.valueOf(Color.parseColor(ORANGE)));
        } else if (aqi > 50) {
            tv_so2_val.setTextColor(Color.parseColor(YELLOW));
            pb_So2.setMax(50);
            pb_So2.setProgress((aqi - 50) , true);
            pb_So2.setProgressTintList(ColorStateList.valueOf(Color.parseColor(YELLOW)));
        } else if (aqi >= 0) {
            tv_so2_val.setTextColor(Color.parseColor(GREEN));
            pb_So2.setProgress(aqi);
            pb_So2.setProgressTintList(ColorStateList.valueOf(Color.parseColor(GREEN)));
        } else {
            tv_so2_val.setTextColor(Color.parseColor(GREEN));
            pb_So2.setMax(50);
            pb_So2.setProgress(aqi);
            pb_So2.setProgressTintList(ColorStateList.valueOf(Color.parseColor(GREEN)));
        }
    }

    public void setPmDisplay(int aqi) {
        TextView tv_pm_val = (TextView) findViewById(R.id.main_PM_value);
        ProgressBar pb_pm = (ProgressBar) findViewById(R.id.pb_pm);
        Drawable drawable = pb_pm.getProgressDrawable();
        tv_pm_val.setText(Integer.toString(aqi));
        if (aqi > 300) {
            tv_pm_val.setTextColor(Color.parseColor(MAROON));
            pb_pm.setMax(200);
            pb_pm.setProgress((aqi - 300), true);
            pb_pm.setProgressTintList(ColorStateList.valueOf(Color.parseColor(MAROON)));
        } else if (aqi > 200) {
            tv_pm_val.setTextColor(Color.parseColor(PURPLE));
            pb_pm.setMax(100);
            pb_pm.setProgress((aqi - 200), true);
            pb_pm.setProgressTintList(ColorStateList.valueOf(Color.parseColor(PURPLE)));
        } else if (aqi > 150) {
            tv_pm_val.setTextColor(Color.parseColor(RED));
            pb_pm.setMax(50);
            pb_pm.setProgress((aqi - 150), true);
            pb_pm.setProgressTintList(ColorStateList.valueOf(Color.parseColor(RED)));
        } else if (aqi > 100) {
            tv_pm_val.setTextColor(Color.parseColor(ORANGE));
            pb_pm.setMax(50);
            pb_pm.setProgress((aqi - 100), true);
            pb_pm.setProgressTintList(ColorStateList.valueOf(Color.parseColor(ORANGE)));
        } else if (aqi > 50) {
            tv_pm_val.setTextColor(Color.parseColor(YELLOW));
            pb_pm.setMax(50);
            pb_pm.setProgress((aqi - 50), true);
            pb_pm.setProgressTintList(ColorStateList.valueOf(Color.parseColor(YELLOW)));
        } else if (aqi >= 0) {
            tv_pm_val.setTextColor(Color.parseColor(GREEN));
            pb_pm.setProgress(aqi);
            pb_pm.setProgressTintList(ColorStateList.valueOf(Color.parseColor(GREEN)));
        } else {
            tv_pm_val.setTextColor(Color.parseColor(GREEN));
            pb_pm.setMax(50);
            pb_pm.setProgress(aqi);
            pb_pm.setProgressTintList(ColorStateList.valueOf(Color.parseColor(GREEN)));
        }
    }
    public void setSensorWarmupWarning(int warningStatus)
    {
        ImageView warning = (ImageView) findViewById(R.id.icon_swu);
        if (warningStatus == 1)
            warning.setEnabled(false);
        else
            warning.setEnabled(true);
    }

    public void setAveragerWarmupWarning(boolean warningStatus)
    {
        ImageView warning = (ImageView) findViewById(R.id.icon_awu);
        if (warningStatus)
            warning.setEnabled(true);
        else
            warning.setEnabled(false);
    }
    public void setDrawer() {
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
