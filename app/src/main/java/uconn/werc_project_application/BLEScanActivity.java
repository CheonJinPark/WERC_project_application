package uconn.werc_project_application;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsClient;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsEvent;

import uconn.werc_project_application.ble.BLEDataLinker;
import uconn.werc_project_application.ble.BLEDevice;
import uconn.werc_project_application.ble.BLEScanner;
import uconn.werc_project_application.ble.BLEUtilities;
import uconn.werc_project_application.ble.BroadcastReceiver_BLE_GATT;
import uconn.werc_project_application.ble.BroadcastReceiver_BTState;
import uconn.werc_project_application.ble.ListAdapter_BLEDevices;
import uconn.werc_project_application.ble.ListAdapter_BLE_Services;
import uconn.werc_project_application.ble.Service_BLE_GATT;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Bill on 2/6/2018.
 */

public class BLEScanActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private final static String TAG = BLEScanActivity.class.getSimpleName();

    public static final int REQUEST_ENABLE_BT = 1;
    public static final int BTLE_SERVICES = 2;

    private HashMap<String, BLEDevice> mBTDevicesHashMap;
    private ArrayList<BLEDevice> mBTDevicesArrayList;
    private ListAdapter_BLEDevices adapter;
    private ListView listView;

    private Button btn_Scan;

    private BroadcastReceiver_BTState mBTStateUpdateReceiver;
    private BLEScanner mBTLeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blescan);

        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            BLEUtilities.toast(getApplicationContext(), "BLE not supported");
            finish();
        }

        mBTStateUpdateReceiver = new BroadcastReceiver_BTState(getApplicationContext());
        mBTLeScanner = new BLEScanner(this, 120000, -75);

        mBTDevicesHashMap = new HashMap<>();
        mBTDevicesArrayList = new ArrayList<>();

        adapter = new ListAdapter_BLEDevices(this, R.layout.btle_device_list_item, mBTDevicesArrayList);

        listView = new ListView(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        btn_Scan = (Button) findViewById(R.id.btn_scan);
        ((ScrollView) findViewById(R.id.scrollView)).addView(listView);
        findViewById(R.id.btn_scan).setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();

        registerReceiver(mBTStateUpdateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onResume() {
        super.onResume();

//        registerReceiver(mBTStateUpdateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onPause() {
        super.onPause();

//        unregisterReceiver(mBTStateUpdateReceiver);
        stopScan();
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(mBTStateUpdateReceiver);
        stopScan();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
//                Utils.toast(getApplicationContext(), "Thank you for turning on Bluetooth");
            }
            else if (resultCode == RESULT_CANCELED) {
                BLEUtilities.toast(getApplicationContext(), "Please turn on Bluetooth");
            }
        }
        else if (requestCode == BTLE_SERVICES) {
            // Do something
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Context context = view.getContext();

//        Log.d("debug", "OnClick Listener triggered");
//         BLEUtilities.toast(context, "List Item clicked");

        // do something with the text views and start the next activity.

        stopScan();

        String name = mBTDevicesArrayList.get(position).getName();
        String address = mBTDevicesArrayList.get(position).getAddress();

        BLEDataLinker.getInstance().setAddress(address);
        BLEDataLinker.getInstance().connectBLE();


//        Intent intent = new Intent(this, BLEServicesActivity.class);
//        intent.putExtra(BLEServicesActivity.EXTRA_NAME, name);
//        intent.putExtra(BLEServicesActivity.EXTRA_ADDRESS, address);
//        startActivityForResult(intent, BTLE_SERVICES);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_scan:
                BLEUtilities.toast(getApplicationContext(), "Scan Button Pressed");

                if (!mBTLeScanner.isScanning()) {
                    startScan();

                    // Send Custom Event to Amazon Pinpoint
                    final AnalyticsClient mgr = AWSProvider.getInstance()
                            .getPinpointManager()
                            .getAnalyticsClient();
                    final AnalyticsEvent evt = mgr.createEvent("BLEScan")
                            .withAttribute("dummyattr", "val");
                    mgr.recordEvent(evt);
                    mgr.submitEvents();
                }
                else {
                    stopScan();
                }

                break;
            default:
                break;
        }

    }

    public void addDevice(BluetoothDevice device, int rssi) {

        String address = device.getAddress();
        if (!mBTDevicesHashMap.containsKey(address)) {
            BLEDevice btleDevice = new BLEDevice(device);
            btleDevice.setRSSI(rssi);

            mBTDevicesHashMap.put(address, btleDevice);
            mBTDevicesArrayList.add(btleDevice);
        }
        else {
            mBTDevicesHashMap.get(address).setRSSI(rssi);
        }

        adapter.notifyDataSetChanged();
    }

    public void startScan(){
        btn_Scan.setText("Scanning...");

        mBTDevicesArrayList.clear();
        mBTDevicesHashMap.clear();

        mBTLeScanner.start();
    }

    public void stopScan() {
        btn_Scan.setText("Scan Again");

        mBTLeScanner.stop();
    }
}
