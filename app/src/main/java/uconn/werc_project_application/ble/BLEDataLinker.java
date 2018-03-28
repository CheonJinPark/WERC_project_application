package uconn.werc_project_application.ble;

import android.app.Service;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uconn.werc_project_application.R;

import static android.content.ContentValues.TAG;

/**
 * Created by Bill Brown on 3/25/2018.
 */

public class BLEDataLinker {

    private BluetoothGattCharacteristic mWriteCharacteristic;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
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

            // Automatically connects to the device upon successful start-up initialization.
//            mBTLeService.connect(mBTLeDeviceAddress);

//            mBluetoothGatt = mBTLeService.getmBluetoothGatt();
//            mGattUpdateReceiver.setBluetoothGatt(mBluetoothGatt);
//            mGattUpdateReceiver.setBTLeService(mBTLeService);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBTLE_Service = null;
            mBTLE_Service_Bound = false;

//            mBluetoothGatt = null;
//            mGattUpdateReceiver.setBluetoothGatt(null);
//            mGattUpdateReceiver.setBTLeService(null);
        }
    };

    public static void initialize(Context context) {
        if (instance == null)
        {
            instance = new BLEDataLinker(context);
        }

    }

    private BLEDataLinker(Context context) {
        this.address = address;
        this.context = context;

        services_ArrayList = new ArrayList<BluetoothGattService>();
        characteristics_HashMap = new HashMap<String, BluetoothGattCharacteristic>();
        characteristics_HashMapList = new HashMap<String, ArrayList<BluetoothGattCharacteristic>>();

    }

    public void connectBLE() {
        if (context != null && address != null) {
            mGattUpdateReceiver = new BroadcastReceiver_BLE_GATT(this);
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
            this.mWriteCharacteristic = characteristic;
        } else if (BLEUtilities.hasNotifyProperty(properties) != 0)
        {
            this.mNotifyCharacteristic = characteristic;
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
                        mWriteCharacteristic = characteristic;
                        Log.d("BLEDataLinker", "Found Write characteristic");
                        Log.d("BLEDataLinker", mWriteCharacteristic.getUuid().toString());
                    } else if (BLEUtilities.hasNotifyProperty(properties) != 0)
                    {
                        mNotifyCharacteristic = characteristic;
                        Log.d("BLEDataLinker", "Found Notify characteristic");
                    }
                    newCharacteristicsList.add(characteristic);
                }

                characteristics_HashMapList.put(service.getUuid().toString(), newCharacteristicsList);
            }
        }
    }
    public int writeToBLE(String msg)
    {
        if (mWriteCharacteristic != null && mBTLE_Service != null) {
                mWriteCharacteristic.setValue(msg);
                mBTLE_Service.writeCharacteristic(mWriteCharacteristic);
                Log.d("BLEDataLinker", "How is this happening.");
                return 1;
        }
        else {
            determineCharacteristics();
            return 0;
        }

    }

    public void setService(Service_BLE_GATT service)
    {
        this.mBTLE_Service = service;
    }
    public Context getContext() { return context; }
    public void setAddress(String address) { this.address = address; }
}