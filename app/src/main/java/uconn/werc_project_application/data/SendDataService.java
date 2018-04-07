package uconn.werc_project_application.data;

import android.Manifest;
import android.app.Service;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Timer;
import java.util.TimerTask;

import uconn.werc_project_application.ble.BLEDataLinker;
import uconn.werc_project_application.ble.BLEUtilities;

import static android.content.ContentValues.TAG;

/*
Bill Brown
 */
public class SendDataService extends Service {
    private final LocalBinder mBinder = new LocalBinder();
    protected Handler handler;
    protected Toast mToast;
    protected TimerTask tTask;
    protected Timer timer;
    private static final int INSERT_TOKEN = 1003;
    private ContentResolver contentResolver;

    LocationManager locationManager;
    int REQUEST_LOCATION = 2;

    public static final long INSTANT_DATA_INTERVAL = 10 * 1000; // 10 seconds

    public static final long HOUR_DATA_INTERNAL = 1000 * 60 * 60;


    public class LocalBinder extends Binder {
        public SendDataService getService() {
            return SendDataService .this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        contentResolver = getApplicationContext().getContentResolver();
        handler = new Handler();

        if (timer != null)
            timer.cancel();
        else
            timer = new Timer();
        timer.scheduleAtFixedRate(new createDatapointTask(), 0, INSTANT_DATA_INTERVAL);
        timer.scheduleAtFixedRate(new createAqiDatapointTask(), 0, HOUR_DATA_INTERNAL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class createDatapointTask extends TimerTask {
        @Override
        public void run () {
            // Use a handler. I know what this is.
            handler.post(new Runnable() {
                @Override
                public void run() {
                    // My function happens in here
                    if (BLEDataLinker.getInstance() != null) {
// Toast.makeText(getApplicationContext(), Integer.toString(BLEDataLinker.getInstance().writeToBLE("test")), Toast.LENGTH_LONG).show();
                        if (DataInterpreter.getInstance().isDataReady())
                        {
                            ContentValues cvals = DataInterpreter.getInstance().getDataPacket();
                            AsyncQueryHandler queryHandler = new AsyncQueryHandler(contentResolver) {
                                @Override
                                protected void onInsertComplete(int token, Object cookie, Uri uri) {
                                    super.onInsertComplete(token, cookie, uri);
                                    Log.d("SendDataService", "Instantaneous Data Uploaded");
                                    Toast.makeText(getApplicationContext(),"Instantaneous Data Uploaded",Toast.LENGTH_LONG).show();
                                    DataInterpreter.getInstance().clearDataPacket();

                                }
                            };
                            Log.d(TAG, "Instantaneous Data Insert Initiated");
                            queryHandler.startInsert(INSERT_TOKEN, null, SensorContentContract.Sensordata.CONTENT_URI, cvals);
                        }
                    }
                }
            });
        }
    }

    class createAqiDatapointTask extends TimerTask {
        @Override
        public void run () {
            // Use a handler. I know what this is.
            handler.post(new Runnable() {
                @Override
                public void run() {
                    // My function happens in here
                    if (BLEDataLinker.getInstance() != null) {
// Toast.makeText(getApplicationContext(), Integer.toString(BLEDataLinker.getInstance().writeToBLE("test")), Toast.LENGTH_LONG).show();
                        if (DataInterpreter.getInstance().isSampleLargeEnough())
                        {
                            ContentValues cvals = DataInterpreter.getInstance().getDACPacket();
                            AsyncQueryHandler queryHandler = new AsyncQueryHandler(contentResolver) {
                                @Override
                                protected void onInsertComplete(int token, Object cookie, Uri uri) {
                                    super.onInsertComplete(token, cookie, uri);
                                    Log.d("SendDataService", "Hourly AQI Uploaded");
                                    Toast.makeText(getApplicationContext(),"Hourly AQI Updated",Toast.LENGTH_LONG).show();
                                    DataInterpreter.getInstance().clearDataPacket();

                                }
                            };
                            Log.d(TAG, "Hourly Data Insert Initiated");
                            DataInterpreter.getInstance().resetDac();
                            queryHandler.startInsert(INSERT_TOKEN, null, AqiContentContract.Aqidata.CONTENT_URI, cvals);
                        }
                    }
                }
            });
        }
    }
}
