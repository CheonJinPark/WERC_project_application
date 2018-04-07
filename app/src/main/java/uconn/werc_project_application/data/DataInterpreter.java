package uconn.werc_project_application.data;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import uconn.werc_project_application.MainActivity;
import uconn.werc_project_application.ble.BLEDataLinker;

/**
 * Created by Bill Brown on 3/27/2018.
 */

public class DataInterpreter {
    private static final String TAG = "DataInterpreter";
    private static final String PROJECTID = "werc";
    private String delimiter = ",";
    private String[] prefix_identifiers = {"NM","CO", "O3", "NO", "SO", "PM", "P2", "ID"};
    private String[] deviceIds = {"sens1", "sens2"};


    private static final String ENDOFTX = "FIN";
    private String receivedData = "";
    private boolean data_ready = false;
    private ContentValues data_packet;
    private String deviceId;

    private DataAverageContainer dac = new DataAverageContainer();

    private static DataInterpreter instance = null;
    private static MainActivity mActivity;
    private static Context mContext;

    private HashMap<String, Double> sensitivitycode_hashmap;
    private HashMap<String, Double> tiagain_hashmap;
    private HashMap<String, Double> offset_hashmap;

    // AQI Hashmaps
    private HashMap<Double, Double> aqihm_co;
    private HashMap<Double, Double> aqihm_no2;
    private HashMap<Double, Double> aqihm_o3;
    private HashMap<Double, Double> aqihm_so2;
    private HashMap<Double, Double> aqihm_pm;
    private HashMap<Double, Double> aqihm_pml;

    private Double vRef;

    private Double gpsLong = 0.0;
    private Double gpsLat = 0.0;
    public static DataInterpreter getInstance() { return instance; }

    public static void initialize(MainActivity activity)
    {
        if (instance == null)
            instance = new DataInterpreter(activity);
    }

    private DataInterpreter(MainActivity activity) {
        this.mActivity = activity;
        this.mContext = activity.getApplicationContext();
    }

    private void loadDefaultVRef()
    {
        vRef = 1.665;
    }

    private void loadAQIHashMaps()
    {
        // CO Hashmap
        aqihm_co = new HashMap<Double, Double>();
        aqihm_co.put(0.0, 11.36);
        aqihm_co.put(4.5, 10.0);
        aqihm_co.put(9.5, 16.89);
        aqihm_co.put(12.5, 16.89);
        aqihm_co.put(15.5, 6.64);
        aqihm_co.put(30.5, 10.0);
        aqihm_co.put(40.5, 10.0);

        // NO2 Hashmap
        aqihm_no2 = new HashMap<Double, Double>();
        aqihm_no2.put(0.0, 943.4);
        aqihm_no2.put(0.054, 1065.2);
        aqihm_no2.put(0.101, 189.2);
        aqihm_no2.put(0.361, 170.1);
        aqihm_no2.put(0.65, 165.3);
        aqihm_no2.put(1.25, 248.1);
        aqihm_no2.put(1.65, 248.1);

        // O3 Hashmap
        aqihm_o3 = new HashMap<Double, Double>();
        aqihm_o3.put(0.0, 925.9);
        aqihm_o3.put(0.055, 3266.7);
        aqihm_o3.put(0.071, 3500.0);
        aqihm_o3.put(0.086, 2578.9);
        aqihm_o3.put(0.106, 1053.2);
        aqihm_o3.put(0.201, 487.0);
        aqihm_o3.put(0.405, 1000.0);
        aqihm_o3.put(0.505, 1000.0);

        // SO2 Hashmap
        aqihm_so2 = new HashMap<Double, Double>();
        aqihm_so2.put(0.0, 1428.6);
        aqihm_so2.put(0.036, 1256.4);
        aqihm_so2.put(0.076, 449.5);
        aqihm_so2.put(0.186, 415.3);
        aqihm_so2.put(0.305, 331.1);
        aqihm_so2.put(0.605, 497.5);
        aqihm_so2.put(0.805, 497.5);

        // PM Hashmap
        aqihm_pm = new HashMap<Double, Double>();
        aqihm_pm.put(0.0, 4.17);
        aqihm_pm.put(12.1, 2.1);
        aqihm_pm.put(35.5, 2.46);
        aqihm_pm.put(55.5, 0.516);
        aqihm_pm.put(150.5, 1.0);
        aqihm_pm.put(250.5, 1.0);
        aqihm_pm.put(350.5, 0.662);

        // PML
        aqihm_pml = new HashMap<Double, Double>();
        aqihm_pml.put(0.0, 0.926);
        aqihm_pml.put(55.0, 0.495);
        aqihm_pml.put(155.0, 0.495);
        aqihm_pml.put(255.0, 0.495);
        aqihm_pml.put(355.0, 1.435);
        aqihm_pml.put(425.0, 1.25);
        aqihm_pml.put(505.0, 1.0);

    }



    public Double getGpsLong() {
        return gpsLong;
    }

    public Double getGpsLat() {
        return gpsLat;
    }

    public void interpretBLEData(String input) {
        String[] values = parseValues(input);
        Log.d(TAG, "Data: " + input);
        ContentValues cv = new ContentValues();
        cv.put(SensorContentContract.Sensordata.PROJECTID, PROJECTID);
        Log.d(TAG, "Project Id: " + PROJECTID);

        for (String val : values) {
            if (val.contains(prefix_identifiers[0])) {
                cv.put(SensorContentContract.Sensordata.DEVICEID, val.substring(0, val.length() - 2));
                Log.d(TAG, "Device Id: " + val.substring(0, val.length() - 2));
                deviceId = val.substring(0, val.length() - 2);
            } else if (val.contains(prefix_identifiers[1])) {
                Log.d(TAG, "Raw CO: " + val.substring(0, val.length() - 2));
                dac.average_voltage_co(Double.parseDouble(val.substring(0, val.length() - 2)));
                cv.put(SensorContentContract.Sensordata.SENSORRAWCO, Double.parseDouble(val.substring(0, val.length() - 2)));
            } else if (val.contains(prefix_identifiers[2])) {
                Log.d(TAG, "Raw O3: " + val.substring(0, val.length() - 2));
                dac.average_voltage_o3(Double.parseDouble(val.substring(0, val.length() - 2)));
                cv.put(SensorContentContract.Sensordata.SENSORRAWO3, Double.parseDouble(val.substring(0, val.length() - 2)));
            } else if (val.contains(prefix_identifiers[3])) {
                Log.d(TAG, "Raw NO2: " + val.substring(0, val.length() - 2));
                dac.average_voltage_no2(Double.parseDouble(val.substring(0, val.length() - 2)));
                cv.put(SensorContentContract.Sensordata.SENSORRAWNO2, Double.parseDouble(val.substring(0, val.length() - 2)));
            } else if (val.contains(prefix_identifiers[4])) {
                Log.d(TAG, "Raw SO2: " + val.substring(0, val.length() - 2));
                dac.average_voltage_so2(Double.parseDouble(val.substring(0, val.length() - 2)));
                cv.put(SensorContentContract.Sensordata.SENSORRAWSO2, Double.parseDouble(val.substring(0, val.length() - 2)));
            } else if (val.contains(prefix_identifiers[5])) {
                Log.d(TAG, "Raw PM: " + val.substring(0, val.length() - 2));
                dac.average_lpo_pm(Double.parseDouble(val.substring(0, val.length() - 2)));
                cv.put(SensorContentContract.Sensordata.SENSORRAWPM, Double.parseDouble(val.substring(0, val.length() - 2)));
            } else if (val.contains(prefix_identifiers[6])) {
                Log.d(TAG, "Raw PML: " + val.substring(0, val.length() - 2));
                dac.average_lpo_pml(Double.parseDouble(val.substring(0, val.length() - 2)));
                cv.put(SensorContentContract.Sensordata.SENSORRAWPML, Double.parseDouble(val.substring(0, val.length() - 2)));
            }
        }

        // Add Meta Fields
        // Set Time
        cv.put(SensorContentContract.Sensordata.TIME, new Date().getTime());
        // Set Random Packet ID
        cv.put(SensorContentContract.Sensordata.PACKETID, UUID.randomUUID().toString());
        // Set GPS Data
        cv.put(SensorContentContract.Sensordata.GPSLAT, gpsLat);
        cv.put(SensorContentContract.Sensordata.GPSLONG, gpsLong);
        Log.d(TAG, "GPS Latitude: " + gpsLat);
        Log.d(TAG, "GPS Longitude: " + gpsLong);

        dac.increment_packet_counter();
        dac.update_averages();
        data_packet = cv;
        data_ready = true;

        Log.d(TAG, "Data Ready Status: " + data_ready);
    }
    public void receiveData(String input)
    {
        if (input.contains("sens")) {
            receivedData = "";
        }
        receivedData = receivedData + input;
        if (input.contains(ENDOFTX) && receivedData.contains("sens")) {
            interpretBLEData(receivedData);
            receivedData = "";
        }

    }

    public boolean isDataReady()
    {
        return data_ready;
    }

    public ContentValues getDataPacket()
    {
        data_ready = false;
        return data_packet;
    }

    public void clearDataPacket()
    {
        data_packet.clear();
    }
    public String[] parseValues(String input)
    {
        return input.split(delimiter);
    }

    public void setGpsLat(Double lat)
    {
        this.gpsLat = lat;
    }

    public void setGpsLong(Double longe)
    {
        this.gpsLong = longe;
    }

    public boolean isSampleLargeEnough() {
        return dac.overThreshold();
    }

    public ContentValues getDACPacket() {
        ContentValues cv = dac.getContentValues();
        cv.put(AqiContentContract.Aqidata.DEVICEID, deviceId);
        cv.put(AqiContentContract.Aqidata.GPSLAT, gpsLat);
        cv.put(AqiContentContract.Aqidata.GPSLONG, gpsLong);
        cv.put(AqiContentContract.Aqidata.PROJECTID, PROJECTID);

        cv.put(AqiContentContract.Aqidata.PACKETID, UUID.randomUUID().toString());

        Double co_aqi = cv.getAsDouble(AqiContentContract.Aqidata.SENSORAQICO);
        Double no2_aqi = cv.getAsDouble(AqiContentContract.Aqidata.SENSORAQINO2);
        Double o3_aqi = cv.getAsDouble(AqiContentContract.Aqidata.SENSORAQIO3);
        Double so2_aqi = cv.getAsDouble(AqiContentContract.Aqidata.SENSORAQISO2);
        Double pm_aqi = cv.getAsDouble(AqiContentContract.Aqidata.SENSORAQIPM);
        Double pml_aqi = cv.getAsDouble(AqiContentContract.Aqidata.SENSORAQIPML);
        // Determine Largest AQI Value and Source
        if (co_aqi >= no2_aqi && co_aqi >= o3_aqi && co_aqi >= so2_aqi && co_aqi >= pm_aqi && co_aqi >= pml_aqi) {
            cv.put(AqiContentContract.Aqidata.AQISRC, "Carbon Monoxide");
            cv.put(AqiContentContract.Aqidata.AQIVAL, co_aqi);
        } else if (no2_aqi >= co_aqi && no2_aqi >= o3_aqi && no2_aqi >= so2_aqi && no2_aqi >= pm_aqi && no2_aqi >= pml_aqi) {
            cv.put(AqiContentContract.Aqidata.AQISRC, "Nitrogen Dioxide");
            cv.put(AqiContentContract.Aqidata.AQIVAL, no2_aqi);
        } else if (o3_aqi >= co_aqi && o3_aqi >= no2_aqi && o3_aqi >= so2_aqi && o3_aqi >= pm_aqi && o3_aqi >= pml_aqi) {
            cv.put(AqiContentContract.Aqidata.AQISRC, "Ozone");
            cv.put(AqiContentContract.Aqidata.AQIVAL, o3_aqi);
        } else if (so2_aqi >= co_aqi && so2_aqi >= o3_aqi && so2_aqi >= no2_aqi && so2_aqi >= pm_aqi && so2_aqi >= pml_aqi) {
            cv.put(AqiContentContract.Aqidata.AQISRC, "Sulfur Dioxide");
            cv.put(AqiContentContract.Aqidata.AQIVAL, so2_aqi);
        } else if (pm_aqi >= co_aqi && pm_aqi >= o3_aqi && pm_aqi >= no2_aqi && pm_aqi >= so2_aqi && pm_aqi >= pml_aqi) {
            cv.put(AqiContentContract.Aqidata.AQISRC, "Small Particulate Matter");
            cv.put(AqiContentContract.Aqidata.AQIVAL, pm_aqi);
        } else if (pml_aqi >= co_aqi && pml_aqi >= o3_aqi && pml_aqi >= so2_aqi && pml_aqi >= pm_aqi && pml_aqi >= no2_aqi ) {
            cv.put(AqiContentContract.Aqidata.AQISRC, "Large Particulate Matter");
            cv.put(AqiContentContract.Aqidata.AQIVAL, pml_aqi);
        } else {
            cv.put(AqiContentContract.Aqidata.AQISRC, "Unknown. How did you find this");
            cv.put(AqiContentContract.Aqidata.AQIVAL, co_aqi);
        }

        return cv;
    }

    public void resetDac()
    {
        dac.reset();
    }

}
