package uconn.werc_project_application.data;

import android.app.Service;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import uconn.werc_project_application.ble.BLEDataLinker;
import uconn.werc_project_application.ble.BLEUtilities;

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

    public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds


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
        timer.scheduleAtFixedRate(new createDatapointTask(), 0, NOTIFY_INTERVAL);
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
                    // My function happens in here.
                    DummyDataGenerator.initialize(SensorContentContract.Sensordata.PROJECTION_ALL);
                    ContentValues values = DummyDataGenerator.getInstance().generate(0.0, 3.0);
                    if (BLEDataLinker.getInstance() != null)
                        Toast.makeText(getApplicationContext(), Integer.toString(BLEDataLinker.getInstance().writeToBLE("test")), Toast.LENGTH_LONG).show();





                    AsyncQueryHandler queryHandler = new AsyncQueryHandler(contentResolver) {
                        @Override
                        protected void onInsertComplete(int token, Object cookie, Uri uri) {
                            super.onInsertComplete(token, cookie, uri);
                            Log.d("Scheduled Insert", "scheduled insert completed");
                        //    Toast.makeText(getApplicationContext(),"Scheduled Data Upload",Toast.LENGTH_LONG).show();

                        }
                    };
                    queryHandler.startInsert(INSERT_TOKEN, null, SensorContentContract.Sensordata.CONTENT_URI, values);
                }
            });
        }
    }
}
