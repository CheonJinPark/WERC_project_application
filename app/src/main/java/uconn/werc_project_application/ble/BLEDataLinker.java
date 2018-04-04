package uconn.werc_project_application.ble;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.AsyncQueryHandler;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import uconn.werc_project_application.MainActivity;
import uconn.werc_project_application.R;
import uconn.werc_project_application.data.SensorContentContract;

import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_WRITE;
import static android.bluetooth.BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT;
import static android.content.ContentValues.TAG;

/**
 * Created by Bill Brown on 3/25/2018.
 */

public class BLEDataLinker {

    private ArrayList<BluetoothGattCharacteristic> mWriteCharacteristicList = new ArrayList<BluetoothGattCharacteristic>();
    private ArrayList<BluetoothGattCharacteristic> mNotifyCharacteristicList = new ArrayList<BluetoothGattCharacteristic>();
    private ArrayList<BluetoothGattCharacteristic> mReadCharacteristicList = new ArrayList<BluetoothGattCharacteristic>();
    private static String mData;
    private static View mView;
    private static TextView mTv_char;
    private static TextView mTv_rx;
    private static BLEDataLinker instance = null;

    private ArrayList<BluetoothGattService> services_ArrayList;
    private HashMap<String, BluetoothGattCharacteristic> characteristics_HashMap;
    private HashMap<String, ArrayList<BluetoothGattCharacteristic>> characteristics_HashMapList;

    private Intent mBTLE_Service_Intent;
    private Service_BLE_GATT mBTLE_Service;
    private boolean mBTLE_Service_Bound;
    private BroadcastReceiver_BLE_GATT mGattUpdateReceiver;
    private static Context context;
    private static final int INSERT_TOKEN = 1003;
    private ContentResolver contentResolver;
    private static MainActivity activity;
    private String name;
    private String address;


    public static BLEDataLinker getInstance() { return instance; }

    private ServiceConnection mBTLE_ServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {

            // We've bound to LocalService, cast the IBinder and get LocalService instance
            Service_BLE_GATT.BTLeServiceBinder binder = (Service_BLE_GATT.BTLeServiceBinder) service;
            mBTLE_Service = binder.getService();
            mBTLE_Service_Bound = true;

            if (!mBTLE_Service.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
            }

            mBTLE_Service.connect(address);

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBTLE_Service = null;
            mBTLE_Service_Bound = false;

        }
    };

    public static void initialize(MainActivity activity) {
        if (instance == null)
        {
            instance = new BLEDataLinker(activity);
        }

    }

    private BLEDataLinker(MainActivity activity) {
        this.address = address;
        this.activity = activity;
        this.context = activity.getApplicationContext();
        services_ArrayList = new ArrayList<BluetoothGattService>();
        characteristics_HashMap = new HashMap<String, BluetoothGattCharacteristic>();
        characteristics_HashMapList = new HashMap<String, ArrayList<BluetoothGattCharacteristic>>();

    }

    public void connectBLE() {
        if (context != null && address != null) {
            mGattUpdateReceiver = new BroadcastReceiver_BLE_GATT(this);
            context.registerReceiver(mGattUpdateReceiver, BLEUtilities.makeGattUpdateIntentFilter());
            mBTLE_Service_Intent = new Intent(context, Service_BLE_GATT.class);
            context.bindService(mBTLE_Service_Intent, mBTLE_ServiceConnection, Context.BIND_AUTO_CREATE);
            context.startService(mBTLE_Service_Intent);
            determineCharacteristics();
            Log.d("BLEDataLinker", "Linker Declared.");
        }
    }
    public void setData(String data)
    {
        this.mData = data;
    }

    public void setCharacteristic(BluetoothGattCharacteristic characteristic)
    {
        int properties = characteristic.getProperties();

        if (BLEUtilities.hasWriteProperty(properties) != 0)
        {
            this.mWriteCharacteristicList.add(characteristic);
        } else if (BLEUtilities.hasNotifyProperty(properties) != 0)
        {
            this.mNotifyCharacteristicList.add(characteristic);
        }

        String value = characteristic.getStringValue(0);
        if (value != null)
            this.mData = value;
        else
            this.mData = "--";
    }
    public void determineCharacteristics()
    {
        if (mBTLE_Service != null) {

            services_ArrayList.clear();
            characteristics_HashMap.clear();
            characteristics_HashMapList.clear();
            mWriteCharacteristicList.clear();
            mReadCharacteristicList.clear();
            mNotifyCharacteristicList.clear();

            List<BluetoothGattService> servicesList = mBTLE_Service.getSupportedGattServices();

            for (BluetoothGattService service : servicesList) {

                services_ArrayList.add(service);

                List<BluetoothGattCharacteristic> characteristicsList = service.getCharacteristics();
                ArrayList<BluetoothGattCharacteristic> newCharacteristicsList = new ArrayList<>();

                for (BluetoothGattCharacteristic characteristic: characteristicsList) {
                    characteristics_HashMap.put(characteristic.getUuid().toString(), characteristic);
                    int properties = characteristic.getProperties();

                    if (BLEUtilities.hasWriteProperty(properties) != 0)
                    {
                        characteristic.setWriteType(WRITE_TYPE_DEFAULT);
                        mWriteCharacteristicList.add(characteristic);
                        Log.d("BLEDataLinker", "Found Write characteristic  UUID: " + characteristic.getUuid().toString());
                        Log.d("BLEDataLinker", "Characteristic Permissions: " + characteristic.getPermissions());

                    } else if (BLEUtilities.hasNotifyProperty(properties) != 0)
                    {
                        mNotifyCharacteristicList.add(characteristic);
                        Log.d("BLEDataLinker", "Found Notify characteristic   UUID: " + characteristic.getUuid().toString());

                        // Enable Notify Characteristic
                        if (mBTLE_Service != null) {
                            mBTLE_Service.setCharacteristicNotification(characteristic, true);
                        }
                    } else if (BLEUtilities.hasReadProperty(properties) != 0)
                    {
                        mReadCharacteristicList.add(characteristic);
                        Log.d("BLEDataLinker", "Found Read characteristic   UUID: " + characteristic.getUuid().toString());
                    }
                    newCharacteristicsList.add(characteristic);
                }

                characteristics_HashMapList.put(service.getUuid().toString(), newCharacteristicsList);
            }
        }
    }

    public void publishToDB(ContentValues cvals)
    {
        AsyncQueryHandler queryHandler = new AsyncQueryHandler(contentResolver) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                super.onInsertComplete(token, cookie, uri);
                Log.d("Scheduled Insert", "scheduled insert completed");
                Toast.makeText(context,"Scheduled Data Upload",Toast.LENGTH_LONG).show();

            }
        };
        queryHandler.startInsert(INSERT_TOKEN, null, SensorContentContract.Sensordata.CONTENT_URI, cvals);
    }

    public void updateDashboard(ContentValues cvals)
    {
        TextView tv_co = (TextView) activity.findViewById(R.id.main_CO_value);
        TextView tv_no2 = (TextView) activity.findViewById(R.id.main_NO2_value);
        TextView tv_o3 = (TextView) activity.findViewById(R.id.main_O3_value);
        TextView tv_so2 = (TextView) activity.findViewById(R.id.main_SO2_value);
        TextView tv_pm = (TextView) activity.findViewById(R.id.main_PM_value);

        tv_co.setText(cvals.get(SensorContentContract.Sensordata.SENSORAQICO).toString());
        tv_no2.setText(cvals.get(SensorContentContract.Sensordata.SENSORAQINO2).toString());
        tv_o3.setText(cvals.get(SensorContentContract.Sensordata.SENSORAQIO3).toString());
        tv_so2.setText(cvals.get(SensorContentContract.Sensordata.SENSORAQISO2).toString());
        tv_pm.setText(cvals.get(SensorContentContract.Sensordata.SENSORAQIPM).toString());

    }
    public int writeToBLE(String msg)
    {
        if (!mWriteCharacteristicList.isEmpty() && mBTLE_Service != null) {

//            UUID uuid = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");
//            BluetoothGattCharacteristic wchar = new BluetoothGattCharacteristic(uuid, PROPERTY_WRITE, BluetoothGattCharacteristic.PERMISSION_WRITE);
//            wchar.setValue(msg);
            for (BluetoothGattCharacteristic wchar : mWriteCharacteristicList) {
                wchar.setValue(msg);
                mBTLE_Service.writeCharacteristic(wchar);

                Log.d("BLEDataLinker", "Message Submitted to BLE Device at UUID: " + wchar.getUuid().toString());

            }
            return 1;

        }
        else {
            determineCharacteristics();
            return 0;
        }

    }

    public void updateConnectivityStatus(boolean connection)
    {
        if (connection)
            activity.setConnection(MainActivity.BLE_CONNECT);
        else
            activity.setConnection(MainActivity.BLE_DISCONNECT);
    }

    public void close()
    {
        context.unregisterReceiver(mGattUpdateReceiver);
        context.unbindService(mBTLE_ServiceConnection);
        mBTLE_Service_Intent = null;
        mBTLE_Service.close();
    }

    public void setService(Service_BLE_GATT service)
    {
        this.mBTLE_Service = service;
    }
    public Context getContext() { return context; }
    public void setAddress(String address) { this.address = address; }
}