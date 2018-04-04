//package uconn.werc_project_application.data;
//
//import android.Manifest;
//import android.app.Service;
//import android.content.AsyncQueryHandler;
//import android.content.ContentResolver;
//import android.content.ContentValues;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationManager;
//import android.net.Uri;
//import android.os.Binder;
//import android.os.Handler;
//import android.os.IBinder;
//import android.support.v4.app.ActivityCompat;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnSuccessListener;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//import uconn.werc_project_application.ble.BLEDataLinker;
//import uconn.werc_project_application.ble.BLEUtilities;
//
///*
//Bill Brown
// */
//public class SendDataService extends Service {
//    private final LocalBinder mBinder = new LocalBinder();
//    protected Handler handler;
//    protected Toast mToast;
//    protected TimerTask tTask;
//    protected Timer timer;
//    private static final int INSERT_TOKEN = 1003;
//    private ContentResolver contentResolver;
//    private FusedLocationProviderClient mFusedLocationClient;
//
//    LocationManager locationManager;
//    int REQUEST_LOCATION = 2;
//
//    public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds
//
//
//    public class LocalBinder extends Binder {
//        public SendDataService getService() {
//            return SendDataService .this;
//        }
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return mBinder;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        contentResolver = getApplicationContext().getContentResolver();
//        initialize_Gps();
//        handler = new Handler();
//
//        if (timer != null)
//            timer.cancel();
//        else
//            timer = new Timer();
//        timer.scheduleAtFixedRate(new createDatapointTask(), 0, NOTIFY_INTERVAL);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    class createDatapointTask extends TimerTask {
//        @Override
//        public void run () {
//            // Use a handler. I know what this is.
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    // My function happens in here.
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                            != PackageManager.PERMISSION_GRANTED) {
//                        // Check Permissions Now
//
//                        // Check Permissions Now
//                        ActivityCompat.requestPermissions(this,
//                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                REQUEST_LOCATION);
//                        Log.d("JIN","Request Permission" );
//                    }
//                    else {
//                        // permission has been granted, continue as usual
//                        Log.d("JIN","Permission os granted" );
//
//                    }
//                    mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            // Got last known location. In some rare situations this can be null.
//                            if (location != null) {
//                                if (DataInterpreter.getInstance() != null) {
//                                    DataInterpreter.getInstance().setGpsLong(location.getLongitude());
//                                    DataInterpreter.getInstance().setGpsLat(location.getLatitude());
//                                    Log.d("SendDataService", "GPS Location Updated: Longitude: " + Double.toString(location.getLongitude()) + " Latitude: " + Double.toString(location.getLatitude()))
//                                }
//                            } else {
//                                Toast.makeText(getApplicationContext(), "No location", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//                    if (BLEDataLinker.getInstance() != null) {
//                        Toast.makeText(getApplicationContext(), Integer.toString(BLEDataLinker.getInstance().writeToBLE("test")), Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
//        }
//    }
//
//    public void initialize_Gps() {
//
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//
//    }
//}
