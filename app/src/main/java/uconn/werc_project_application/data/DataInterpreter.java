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

/**
 * Created by Bill Brown on 3/27/2018.
 */

public class DataInterpreter {
    private String delimiter = ",";
    private String[] prefix_identifiers = {"NM=","CO=", "O3=", "NO=", "SO=", "PM=", "P2=", "TM="};

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

        loadDefaultValues();
    }

    private void loadDefaultValues()
    {
        loadDefaultSCodes();
        loadDefaultTGains();
        loadDefaultOffsets();
        loadDefaultVRef();
        loadAQIHashMaps();
    }

    private void loadDefaultSCodes()
    {
        sensitivitycode_hashmap = new HashMap<String, Double>();
        sensitivitycode_hashmap.clear();

        // Default values. Should be changed.
        sensitivitycode_hashmap.put(SensorContentContract.Sensordata.SENSORCO, 10.0);
        sensitivitycode_hashmap.put(SensorContentContract.Sensordata.SENSORNO2, 10.0);
        sensitivitycode_hashmap.put(SensorContentContract.Sensordata.SENSORO3, 10.0);
        sensitivitycode_hashmap.put(SensorContentContract.Sensordata.SENSORSO2, 10.0);
    }

    private void loadDefaultTGains()
    {
        tiagain_hashmap = new HashMap<String, Double>();
        tiagain_hashmap.clear();

        // Default values
        tiagain_hashmap.put(SensorContentContract.Sensordata.SENSORCO, 100.0);
        tiagain_hashmap.put(SensorContentContract.Sensordata.SENSORNO2, 499.0);
        tiagain_hashmap.put(SensorContentContract.Sensordata.SENSORO3, 499.0);
        tiagain_hashmap.put(SensorContentContract.Sensordata.SENSORSO2, 100.0);
    }

    private void loadDefaultOffsets()
    {
        offset_hashmap = new HashMap<String, Double>();
        offset_hashmap.clear();

        // Default values
        offset_hashmap.put(SensorContentContract.Sensordata.SENSORCO, 10.0);
        offset_hashmap.put(SensorContentContract.Sensordata.SENSORNO2, 10.0);
        offset_hashmap.put(SensorContentContract.Sensordata.SENSORO3, 10.0);
        offset_hashmap.put(SensorContentContract.Sensordata.SENSORSO2, 10.0);

    }

    private void loadDefaultVRef()
    {
        vRef = 1.65;
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

    public ContentValues interpretBLEData(String input) {
        String[] values = parseValues(input);
        ContentValues cv = new ContentValues();
        for (String val : values) {
            if (val.contains(prefix_identifiers[0])) {
                cv.put(SensorContentContract.Sensordata.DEVICEID, Double.parseDouble(val.substring(0, val.length() - 3)));
            } else if (val.contains(prefix_identifiers[1])) {
                cv.put(SensorContentContract.Sensordata.SENSORRAWCO, Double.parseDouble(val.substring(0, val.length() - 3)));
            } else if (val.contains(prefix_identifiers[2])) {
                cv.put(SensorContentContract.Sensordata.SENSORRAWO3, Double.parseDouble(val.substring(0, val.length() - 3)));
            } else if (val.contains(prefix_identifiers[3])) {
                cv.put(SensorContentContract.Sensordata.SENSORRAWNO2, Double.parseDouble(val.substring(0, val.length() - 3)));
            } else if (val.contains(prefix_identifiers[4])) {
                cv.put(SensorContentContract.Sensordata.SENSORRAWSO2, Double.parseDouble(val.substring(0, val.length() - 3)));
            } else if (val.contains(prefix_identifiers[5])) {
                cv.put(SensorContentContract.Sensordata.SENSORRAWPM, Double.parseDouble(val.substring(0, val.length() - 3)));
            } else if (val.contains(prefix_identifiers[6])) {
                cv.put(SensorContentContract.Sensordata.SENSORRAWTEMP, Double.parseDouble(val.substring(0, val.length() - 3)));
            }
        }

        Random r = new Random();
        // Add Meta Fields
        // Set Time
        cv.put(SensorContentContract.Sensordata.TIME, new Date().getTime());
        // Set Random Packet ID
        cv.put(SensorContentContract.Sensordata.PACKETID, UUID.randomUUID().toString());
        // Set GPS Data
        cv.put(SensorContentContract.Sensordata.GPSLAT, gpsLat);
        cv.put(SensorContentContract.Sensordata.GPSLONG, gpsLong);

        /*
        CALCULATIONS: Voltage to Parts Per Million (PPM) for CO, NO2, O3, SO2
        Equations:
        Concentration:              Cx = (1/M)(Vgas - Vgas0)
        Sensor Calibration Factor:  M = Sensitivity Code * TIA Gain * 10^-9 * 10^3
        Voltage in Clean-Air        Vgas0 = Vref + Voffset

        Sensitivity Codes are hard-coded but can be reconfigured in Settings
         */

        // Convert CO
        Double co_ppm = (1.0 / (sensitivitycode_hashmap.get(SensorContentContract.Sensordata.SENSORCO) *
                tiagain_hashmap.get(SensorContentContract.Sensordata.SENSORCO) * Math.pow(10.0, -6.0)))
                * (Double.parseDouble(cv.get(SensorContentContract.Sensordata.SENSORCO).toString()) - (vRef + offset_hashmap.get(SensorContentContract.Sensordata.SENSORCO)));
        cv.put(SensorContentContract.Sensordata.SENSORCO, co_ppm);
        Log.d("Data Calculation", "CO PPM: " + Double.toString(co_ppm));

        // Convert NO2
        Double no2_ppm = (1.0 / (sensitivitycode_hashmap.get(SensorContentContract.Sensordata.SENSORNO2) *
                tiagain_hashmap.get(SensorContentContract.Sensordata.SENSORNO2) * Math.pow(10.0, -6.0)))
                * (Double.parseDouble(cv.get(SensorContentContract.Sensordata.SENSORNO2).toString()) - (vRef + offset_hashmap.get(SensorContentContract.Sensordata.SENSORNO2)));
        cv.put(SensorContentContract.Sensordata.SENSORNO2, no2_ppm);
        Log.d("Data Calculation", "NO2 PPM: " + Double.toString(no2_ppm));

        // Convert O3
        Double o3_ppm = (1.0 / (sensitivitycode_hashmap.get(SensorContentContract.Sensordata.SENSORO3) *
                tiagain_hashmap.get(SensorContentContract.Sensordata.SENSORO3) * Math.pow(10.0, -6.0)))
                * (Double.parseDouble(cv.get(SensorContentContract.Sensordata.SENSORO3).toString()) - (vRef + offset_hashmap.get(SensorContentContract.Sensordata.SENSORO3)));
        cv.put(SensorContentContract.Sensordata.SENSORO3, o3_ppm);
        Log.d("Data Calculation", "O3 PPM: " + Double.toString(o3_ppm));

        // Convert SO2
        Double so2_ppm = (1.0 / (sensitivitycode_hashmap.get(SensorContentContract.Sensordata.SENSORSO2) *
                tiagain_hashmap.get(SensorContentContract.Sensordata.SENSORSO2) * Math.pow(10.0, -6.0)))
                * (Double.parseDouble(cv.get(SensorContentContract.Sensordata.SENSORSO2).toString()) - (vRef + offset_hashmap.get(SensorContentContract.Sensordata.SENSORSO2)));
        cv.put(SensorContentContract.Sensordata.SENSORSO2, so2_ppm);
        Log.d("Data Calculation", "SO2 PPM: " + Double.toString(so2_ppm));

        /*
        CALCULATIONS: Voltage to Micrograms per Cubic Meter ug/m3 for Particulate Matter
         */
        Double pm_ug = (3.0 * r.nextDouble());

        Double pml_ug = (3.0 * r.nextDouble());

        /*
        CALCULATIONS: Parts-Per-Million to Air Quality Index (AQI)
        Equations:
         */

        // Calculate AQI for CO
        Double co_aqi;
        if (co_ppm > 40.5)
            co_aqi = aqihm_co.get(40.5) * (co_ppm - 40.5) + 401.0;
        else if (co_ppm > 30.5)
            co_aqi = aqihm_co.get(30.5) * (co_ppm - 30.5) + 301.0;
        else if (co_ppm > 15.5)
            co_aqi = aqihm_co.get(15.5) * (co_ppm - 15.5) + 201.0;
        else if (co_ppm > 12.5)
            co_aqi = aqihm_co.get(12.5) * (co_ppm - 12.5) + 151.0;
        else if (co_ppm > 9.5)
            co_aqi = aqihm_co.get(9.5) * (co_ppm - 9.5) + 101.0;
        else if (co_ppm > 4.5)
            co_aqi = aqihm_co.get(4.5) * (co_ppm - 4.5) + 51.0;
        else if (co_ppm >= 0.0)
            co_aqi = aqihm_co.get(0.0) * (co_ppm);
        else
            co_aqi = 99.9; // error, shouldnt' happen.
        cv.put(SensorContentContract.Sensordata.SENSORAQICO, co_aqi);
        Log.d("Data Calculation", "CO AQI: " + Double.toString(co_aqi));

        // Calculate AQI for NO2
        Double no2_aqi;
        if (no2_ppm > 1.65)
            no2_aqi = aqihm_no2.get(1.65) * (no2_ppm - 1.65) + 401.0;
        else if (no2_ppm > 1.25)
            no2_aqi = aqihm_no2.get(1.25) * (no2_ppm - 1.25) + 301.0;
        else if (no2_ppm > 0.65)
            no2_aqi = aqihm_no2.get(0.65) * (no2_ppm - 0.65) + 201.0;
        else if (no2_ppm > 0.361)
            no2_aqi = aqihm_no2.get(0.361) * (no2_ppm - 0.361) + 151.0;
        else if (no2_ppm > 0.101)
            no2_aqi = aqihm_no2.get(0.101) * (no2_ppm - 0.101) + 101.0;
        else if (no2_ppm > 0.054)
            no2_aqi = aqihm_no2.get(0.054) * (no2_ppm - 0.054) + 51.0;
        else if (no2_ppm >= 0.0)
            no2_aqi = aqihm_no2.get(0.0) * (no2_ppm);
        else
            no2_aqi = 99.9; // should never happen.
        cv.put(SensorContentContract.Sensordata.SENSORAQINO2, no2_aqi);
        Log.d("Data Calculation", "NO2 AQI: " + Double.toString(no2_aqi));

        // Calculate AQI for O3
        Double o3_aqi;
        if (o3_ppm > 0.505)
            o3_aqi = aqihm_o3.get(0.505) * (o3_ppm - 0.505) + 401.0;
        else if (o3_ppm > 0.405)
            o3_aqi = aqihm_o3.get(0.405) * (o3_ppm - 0.405) + 301.0;
        else if (o3_ppm > 0.201)
            o3_aqi = aqihm_o3.get(0.201) * (o3_ppm - 0.201) + 201.0;
        else if (o3_ppm > 0.106)
            o3_aqi = aqihm_o3.get(0.106) * (o3_ppm - 0.106) + 201.0;
        else if (o3_ppm > 0.086)
            o3_aqi = aqihm_o3.get(0.086) * (o3_ppm - 0.086) + 151.0;
        else if (o3_ppm > 0.071)
            o3_aqi = aqihm_o3.get(0.071) * (o3_ppm - 0.071) + 101.0;
        else if (o3_ppm > 0.055)
            o3_aqi = aqihm_o3.get(0.055) * (o3_ppm - 0.055) + 51.0;
        else if (o3_ppm >= 0.0)
            o3_aqi = aqihm_o3.get(0.0) * (o3_ppm);
        else
            o3_aqi = 99.9; // should never happen
        cv.put(SensorContentContract.Sensordata.SENSORAQIO3, o3_aqi);
        Log.d("Data Calculation", "O3 AQI: " + Double.toString(o3_aqi));

        // Calculate AQI for SO2
        Double so2_aqi;
        if (so2_ppm > 0.805)
            so2_aqi = aqihm_so2.get(0.805) * (so2_ppm - 0.805) + 401.0;
        else if (so2_ppm > 0.605)
            so2_aqi = aqihm_so2.get(0.605) * (so2_ppm - 0.605) + 301.0;
        else if (so2_ppm > 0.305)
            so2_aqi = aqihm_so2.get(0.305) * (so2_ppm - 0.305) + 201.0;
        else if (so2_ppm > 0.186)
            so2_aqi = aqihm_so2.get(0.186) * (so2_ppm - 0.186) + 151.0;
        else if (so2_ppm > 0.076)
            so2_aqi = aqihm_so2.get(0.076) * (so2_ppm - 0.076) + 101.0;
        else if (so2_ppm > 0.036)
            so2_aqi = aqihm_so2.get(0.036) * (so2_ppm - 0.036) + 51.0;
        else if (so2_ppm >= 0.0)
            so2_aqi = aqihm_so2.get(0.0) * (so2_ppm);
        else
            so2_aqi = 99.9;
        cv.put(SensorContentContract.Sensordata.SENSORAQISO2, so2_aqi);
        Log.d("Data Calculation", "SO2 AQI: " + Double.toString(so2_aqi));

        // Calculate AQI for PM
        Double pm_aqi;
        if (pm_ug > 350.5)
            pm_aqi = aqihm_pm.get(350.5) * (pm_ug - 350.5) + 401.0;
        else if (pm_ug > 250.5)
            pm_aqi = aqihm_pm.get(250.5) * (pm_ug - 250.5) + 301.0;
        else if (pm_ug > 150.5)
            pm_aqi = aqihm_pm.get(150.5) * (pm_ug - 150.5) + 201.0;
        else if (pm_ug > 55.5)
            pm_aqi = aqihm_pm.get(55.5) * (pm_ug - 55.5) + 151.0;
        else if (pm_ug > 35.5)
            pm_aqi = aqihm_pm.get(35.5) * (pm_ug - 35.5) + 101.0;
        else if (pm_ug > 12.1)
            pm_aqi = aqihm_pm.get(12.1) * (pm_ug - 12.1) + 51.0;
        else if (pm_ug >= 0)
            pm_aqi = aqihm_pm.get(0.0) * (pm_ug);
        else
            pm_aqi = 99.9; // shouldn't happen.
        cv.put(SensorContentContract.Sensordata.SENSORAQIPM, pm_aqi);
        Log.d("Data Calculation", "PM AQI: " + Double.toString(pm_aqi));

        // Calculate AQI for PML
        Double pml_aqi;
        if (pml_ug > 505.0)
            pml_aqi = aqihm_pml.get(505.0) * (pml_ug - 505.0) + 401.0;
        else if (pml_ug > 425.0)
            pml_aqi = aqihm_pml.get(425.0) * (pml_ug - 425.0) + 301.0;
        else if (pml_ug > 355.0)
            pml_aqi = aqihm_pml.get(355.0) * (pml_ug - 355.0) + 201.0;
        else if (pml_ug > 255.0)
            pml_aqi = aqihm_pml.get(255.0) * (pml_ug - 255.0) + 151.0;
        else if (pml_ug > 155.0)
            pml_aqi = aqihm_pml.get(155.0) * (pml_ug - 155.0) + 101.0;
        else if (pml_ug > 55.0)
            pml_aqi = aqihm_pml.get(55.0) * (pml_ug - 55.0) + 51.0;
        else if (pml_ug >= 0.0)
            pml_aqi = aqihm_pml.get(0.0) * (pml_ug);
        else
            pml_aqi = 99.9; // uh yeah.
        cv.put(SensorContentContract.Sensordata.SENSORAQIPML, pml_aqi);
        Log.d("Data Calculation", "PML AQI: " + Double.toString(pml_aqi));

        // Determine Largest AQI Value and Source
        if (co_aqi >= no2_aqi && co_aqi >= o3_aqi && co_aqi >= so2_aqi && co_aqi >= pm_aqi && co_aqi >= pml_aqi) {
            cv.put(SensorContentContract.Sensordata.AQISRC, "Carbon Monoxide");
            cv.put(SensorContentContract.Sensordata.AQIVAL, co_aqi);
        } else if (no2_aqi >= co_aqi && no2_aqi >= o3_aqi && no2_aqi >= so2_aqi && no2_aqi >= pm_aqi && no2_aqi >= pml_aqi) {
            cv.put(SensorContentContract.Sensordata.AQISRC, "Nitrogen Dioxide");
            cv.put(SensorContentContract.Sensordata.AQIVAL, no2_aqi);
        } else if (o3_aqi >= co_aqi && o3_aqi >= no2_aqi && o3_aqi >= so2_aqi && o3_aqi >= pm_aqi && o3_aqi >= pml_aqi) {
            cv.put(SensorContentContract.Sensordata.AQISRC, "Ozone");
            cv.put(SensorContentContract.Sensordata.AQIVAL, o3_aqi);
        } else if (so2_aqi >= co_aqi && so2_aqi >= o3_aqi && so2_aqi >= no2_aqi && so2_aqi >= pm_aqi && so2_aqi >= pml_aqi) {
            cv.put(SensorContentContract.Sensordata.AQISRC, "Sulfur Dioxide");
            cv.put(SensorContentContract.Sensordata.AQIVAL, so2_aqi);
        } else if (pm_aqi >= co_aqi && pm_aqi >= o3_aqi && pm_aqi >= no2_aqi && pm_aqi >= so2_aqi && pm_aqi >= pml_aqi) {
            cv.put(SensorContentContract.Sensordata.AQISRC, "Small Particulate Matter");
            cv.put(SensorContentContract.Sensordata.AQIVAL, pm_aqi);
        } else if (pml_aqi >= co_aqi && pml_aqi >= o3_aqi && pml_aqi >= so2_aqi && pml_aqi >= pm_aqi && pml_aqi >= no2_aqi ) {
            cv.put(SensorContentContract.Sensordata.AQISRC, "Large Particulate Matter");
            cv.put(SensorContentContract.Sensordata.AQIVAL, pml_aqi);
        } else {
            cv.put(SensorContentContract.Sensordata.AQISRC, "Unknown. How did you find this");
            cv.put(SensorContentContract.Sensordata.AQIVAL, co_aqi);
        }

        return cv;
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



}
