package uconn.werc_project_application.ble;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import uconn.werc_project_application.BLEServicesActivity;
import uconn.werc_project_application.data.DataInterpreter;

/**
 * Created by Bill Brown on 2/26/18.
 */
public class BroadcastReceiver_BLE_GATT extends BroadcastReceiver {

    private boolean mConnected = false;

    private BLEServicesActivity activity;
    private BLEDataLinker linker;
    public final static String EXTRA_UUID = "uconn.werc_project_application.Service_BLE_GATT.EXTRA_UUID";
    public final static String EXTRA_DATA = "uconn.werc_project_application.Service_BLE_GATT.EXTRA_DATA";

    public BroadcastReceiver_BLE_GATT(BLEDataLinker activity) {
        this.linker = activity;
    }
    public BroadcastReceiver_BLE_GATT(BLEServicesActivity activity) {
        this.activity = activity;
    }

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device. This can be a
    // result of read or notification operations.

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        Log.d("BroadcastReceiverBLE" , "Received action: " + action);
        if (Service_BLE_GATT.ACTION_GATT_CONNECTED.equals(action)) {
            mConnected = true;
            BLEUtilities.toast(linker.getContext(), "Connected to Device");
            BLEDataLinker.getInstance().updateConnectivityStatus(true);
        }
        else if (Service_BLE_GATT.ACTION_GATT_DISCONNECTED.equals(action)) {
            mConnected = false;
            BLEUtilities.toast(linker.getContext(), "Disconnected From Device");
            BLEDataLinker.getInstance().updateConnectivityStatus(false);
//            linker.finish();
        }
        else if (Service_BLE_GATT.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
            String uuid = intent.getStringExtra(EXTRA_UUID);
            String data = intent.getStringExtra(EXTRA_DATA);
            Log.d("BroadcastReceiverBLE", "Services Discovered: Data: " + data + "  at UUID: " + uuid);
//
//            activity.updateServices();
        }
        else if (Service_BLE_GATT.ACTION_DATA_AVAILABLE.equals(action)) {
            String uuid = intent.getStringExtra(EXTRA_UUID);
            String data = intent.getStringExtra(EXTRA_DATA);
            Log.d("BroadcastReceiverBLE", "Data Available: Data: " + data + "  at UUID: " + uuid);
            ContentValues cvals = DataInterpreter.getInstance().interpretBLEData(data);
            BLEDataLinker.getInstance().updateDashboard(cvals);
            BLEDataLinker.getInstance().publishToDB(cvals);
//            activity.updateCharacteristic();
        }

        return;
    }
}
