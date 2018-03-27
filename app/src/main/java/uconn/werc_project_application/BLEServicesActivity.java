package uconn.werc_project_application;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uconn.werc_project_application.ble.BLEUtilities;
import uconn.werc_project_application.ble.BroadcastReceiver_BLE_GATT;
import uconn.werc_project_application.ble.Dialog_BLE_Characteristic;
import uconn.werc_project_application.ble.ListAdapter_BLE_Services;
import uconn.werc_project_application.ble.Service_BLE_GATT;

public class BLEServicesActivity extends AppCompatActivity implements ExpandableListView.OnChildClickListener {
    private final static String TAG = BLEServicesActivity.class.getSimpleName();


    public static final String EXTRA_NAME = "uconn.werc_project_application.BLEScanActivity.NAME";
    public static final String EXTRA_ADDRESS = "uconn.werc_project_application.BLEScanActivity.ADDRESS";

    private ListAdapter_BLE_Services expandableListAdapter;
    private ExpandableListView expandableListView;

    private ArrayList<BluetoothGattService> services_ArrayList;
    private HashMap<String, BluetoothGattCharacteristic> characteristics_HashMap;
    private HashMap<String, ArrayList<BluetoothGattCharacteristic>> characteristics_HashMapList;

    private Intent mBTLE_Service_Intent;
    private Service_BLE_GATT mBTLE_Service;
    private boolean mBTLE_Service_Bound;
    private BroadcastReceiver_BLE_GATT mGattUpdateReceiver;

    private String name;
    private String address;

    private ServiceConnection mBTLE_ServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {

            // We've bound to LocalService, cast the IBinder and get LocalService instance
            Service_BLE_GATT.BTLeServiceBinder binder = (Service_BLE_GATT.BTLeServiceBinder) service;
            mBTLE_Service = binder.getService();
            mBTLE_Service_Bound = true;

            if (!mBTLE_Service.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btle_services);

        Intent intent = getIntent();
        name = intent.getStringExtra(BLEServicesActivity.EXTRA_NAME);
        address = intent.getStringExtra(BLEServicesActivity.EXTRA_ADDRESS);

        services_ArrayList = new ArrayList<>();
        characteristics_HashMap = new HashMap<>();
        characteristics_HashMapList = new HashMap<>();

        expandableListAdapter = new ListAdapter_BLE_Services(
                this, services_ArrayList, characteristics_HashMapList);

        expandableListView = (ExpandableListView) findViewById(R.id.lv_expandable);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnChildClickListener(this);

        ((TextView) findViewById(R.id.tv_name)).setText(name + " Services");
        ((TextView) findViewById(R.id.tv_address)).setText(address);
    }

    @Override
    protected void onStart() {
        super.onStart();

//        mGattUpdateReceiver = new BroadcastReceiver_BLE_GATT(this);
//        registerReceiver(mGattUpdateReceiver, BLEUtilities.makeGattUpdateIntentFilter());
//
//        mBTLE_Service_Intent = new Intent(this, Service_BLE_GATT.class);
//        bindService(mBTLE_Service_Intent, mBTLE_ServiceConnection, Context.BIND_AUTO_CREATE);
//        startService(mBTLE_Service_Intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(mGattUpdateReceiver);
        unbindService(mBTLE_ServiceConnection);
        mBTLE_Service_Intent = null;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        BluetoothGattCharacteristic characteristic = characteristics_HashMapList.get(
                services_ArrayList.get(groupPosition).getUuid().toString())
                .get(childPosition);

        if (BLEUtilities.hasWriteProperty(characteristic.getProperties()) != 0) {
            String uuid = characteristic.getUuid().toString();

            Dialog_BLE_Characteristic dialog_btle_characteristic = new Dialog_BLE_Characteristic();

            dialog_btle_characteristic.setTitle(uuid);
            dialog_btle_characteristic.setService(mBTLE_Service);
            dialog_btle_characteristic.setCharacteristic(characteristic);

            dialog_btle_characteristic.show(getFragmentManager(), "Dialog_BTLE_Characteristic");
            Log.d("BLE Connection", uuid);


        } else if (BLEUtilities.hasReadProperty(characteristic.getProperties()) != 0) {
            if (mBTLE_Service != null) {
                mBTLE_Service.readCharacteristic(characteristic);
            }
        } else if (BLEUtilities.hasNotifyProperty(characteristic.getProperties()) != 0) {
            if (mBTLE_Service != null) {
                mBTLE_Service.setCharacteristicNotification(characteristic, true);
            }
        }

        return false;
    }

    public void updateServices() {

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
                    newCharacteristicsList.add(characteristic);
                }

                characteristics_HashMapList.put(service.getUuid().toString(), newCharacteristicsList);
            }

            if (servicesList != null && servicesList.size() > 0) {
                expandableListAdapter.notifyDataSetChanged();
            }
        }
    }

    public void updateCharacteristic() {
        expandableListAdapter.notifyDataSetChanged();
    }
}
