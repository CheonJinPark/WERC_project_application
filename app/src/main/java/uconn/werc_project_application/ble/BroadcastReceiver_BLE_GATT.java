package uconn.werc_project_application.ble;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import uconn.werc_project_application.BLEServicesActivity;

/**
 * Created by Bill Brown on 2/26/18.
 */
public class BroadcastReceiver_BLE_GATT extends BroadcastReceiver {

    private boolean mConnected = false;

    private BLEDataLinker linker;

    public BroadcastReceiver_BLE_GATT(BLEDataLinker activity) {
        this.linker = activity;
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

        if (Service_BLE_GATT.ACTION_GATT_CONNECTED.equals(action)) {
            mConnected = true;
            BLEUtilities.toast(linker.getContext(), "Connected to Device");
        }
        else if (Service_BLE_GATT.ACTION_GATT_DISCONNECTED.equals(action)) {
            mConnected = false;
            BLEUtilities.toast(linker.getContext(), "Disconnected From Device");
//            linker.finish();
        }
        else if (Service_BLE_GATT.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
//            linker.updateServices();
        }
        else if (Service_BLE_GATT.ACTION_DATA_AVAILABLE.equals(action)) {

//            String uuid = intent.getStringExtra(Service_BTLE_GATT.EXTRA_UUID);
//            String data = intent.getStringExtra(Service_BTLE_GATT.EXTRA_DATA);

//            activity.updateCharacteristic();
        }

        return;
    }
}
