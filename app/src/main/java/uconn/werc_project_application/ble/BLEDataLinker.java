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

import uconn.werc_project_application.R;

import static android.content.ContentValues.TAG;

/**
 * Created by Bill Brown on 3/25/2018.
 */

public class BLEDataLinker {

    private static BluetoothGattCharacteristic mWriteCharacteristic;
    private static BluetoothGattCharacteristic mNotifyCharacteristic;
    private static String mData;
    private static View mView;
    private static TextView mTv_char;
    private static TextView mTv_rx;
    private static BLEDataLinker instance = null;
    private static Service_BLE_GATT service;

    private ListAdapter_BLE_Services expandableListAdapter;
    private ExpandableListView expandableListView;

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
    }

    public void connectBLE() {
        if (context != null && address != null) {
            mGattUpdateReceiver = new BroadcastReceiver_BLE_GATT(this);
            mBTLE_Service_Intent = new Intent(context, Service_BLE_GATT.class);
            context.bindService(mBTLE_Service_Intent, mBTLE_ServiceConnection, Context.BIND_AUTO_CREATE);
            context.startService(mBTLE_Service_Intent);
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

    public int writeToBLE(String msg)
    {
        if (mWriteCharacteristic != null && service != null) {
                mWriteCharacteristic.setValue(msg);
                service.writeCharacteristic(mWriteCharacteristic);
                return 1;
            }
        else
            return 0;

    }

    public void setService(Service_BLE_GATT service)
    {
        this.service = service;
    }
    public Context getContext() { return context; }
    public void setAddress(String address) { this.address = address; }
}