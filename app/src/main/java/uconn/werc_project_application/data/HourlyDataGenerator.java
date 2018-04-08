package uconn.werc_project_application.data;

import android.content.ContentValues;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import uconn.werc_project_application.MainActivity;

/**
 * Created by Bill on 4/6/2018.
 */

public class HourlyDataGenerator {
    private static final String TAG = "HourlyDataGenerator";
    private static final String CAG = "DAC Calculations";
    private static final int PACKETTHRESHOLD = 160;
    private String[] deviceIds = {"sens1", "sens2"};

    private String deviceId = null;
    // Metadata
    private int packetCount = 0;

    // Raw Sensor Data
    private ArrayList<Double> voltage_co = new ArrayList<>();
    private ArrayList<Double> voltage_no2 = new ArrayList<>();
    private ArrayList<Double> voltage_o3 = new ArrayList<>();
    private ArrayList<Double> voltage_so2 = new ArrayList<>();
    private ArrayList<Double> lpo_pm = new ArrayList<>();
    private ArrayList<Double> lpo_pml = new ArrayList<>();

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


        /* Average Values
     */

    // Average Voltages
    private Double av_v_co = 0.0;
    private Double av_v_no2 = 0.0;
    private Double av_v_o3 = 0.0;
    private Double av_v_so2 = 0.0;
    private Double av_l_pm = 0.0;
    private Double av_l_pml = 0.0;


    private Double vRef;

    public HourlyDataGenerator(String deviceId, HashMap<String, Double> sensitivitycode_hashmap, HashMap<String, Double> tiagain_hashmap, HashMap<String, Double> offset_hashmap, HashMap<Double, Double> aqihm_co, HashMap<Double, Double> aqihm_no2, HashMap<Double, Double> aqihm_o3, HashMap<Double, Double> aqihm_so2, HashMap<Double, Double> aqihm_pm, HashMap<Double, Double> aqihm_pml, Double vRef) {
        this.deviceId = deviceId;
        this.sensitivitycode_hashmap = sensitivitycode_hashmap;
        this.tiagain_hashmap = tiagain_hashmap;
        this.offset_hashmap = offset_hashmap;
        this.aqihm_co = aqihm_co;
        this.aqihm_no2 = aqihm_no2;
        this.aqihm_o3 = aqihm_o3;
        this.aqihm_so2 = aqihm_so2;
        this.aqihm_pm = aqihm_pm;
        this.aqihm_pml = aqihm_pml;
        this.vRef = vRef;
    }

    public String getDeviceId() {
        return deviceId;
    }


    public void average_voltage_co(Double val)
    {
        voltage_co.add(val);
    }

    public void average_voltage_no2(Double val)
    {
        voltage_no2.add(val);
    }

    public void average_voltage_o3(Double val)
    {
        voltage_o3.add(val);
    }

    public void average_voltage_so2(Double val)
    {
        voltage_so2.add(val);
    }

    public void average_lpo_pm(Double val)
    {
        lpo_pm.add(val);
    }

    public void average_lpo_pml(Double val)
    {
        lpo_pml.add(val);
    }

    public void increment_packet_counter()
    {
        packetCount++;
    }

    public void update_averages()
    {
        Log.d(TAG, "Packets Included in Average: " + packetCount);

        // Update Average Voltages
        av_v_co = calculateAverage(voltage_co);
        Log.d(TAG, "Average Voltage CO: " + Double.toString(av_v_co));
        av_v_no2 = calculateAverage(voltage_no2);
        Log.d(TAG, "Average Voltage NO2: " + Double.toString(av_v_no2));
        av_v_o3 = calculateAverage(voltage_o3);
        Log.d(TAG, "Average Voltage O3: " + Double.toString(av_v_o3));
        av_v_so2 = calculateAverage(voltage_so2);
        Log.d(TAG, "Average Voltage SO2: " + Double.toString(av_v_so2));
        av_l_pm = calculateAverage(lpo_pm);
        Log.d(TAG, "Average LPO PM: " + Double.toString(av_l_pm));
        av_l_pml = calculateAverage(lpo_pml);
        Log.d(TAG, "Average LPO PML: " + Double.toString(av_l_pml));
    }

    public boolean overThreshold() {
        return packetCount > PACKETTHRESHOLD;
    }

    public ContentValues getContentValues() {
        if (deviceId == null) {


        }
        ContentValues cv = new ContentValues();
        cv.put(AqiContentContract.Aqidata.TIME, new Date().getTime());

         /*
        CALCULATIONS: Voltage to Parts Per Million (PPM) for CO, NO2, O3, SO2
        Equations:
        Concentration:              Cx = (1/M)(Vgas - Vgas0)
        Sensor Calibration Factor:  M = Sensitivity Code * TIA Gain * 10^-9 * 10^3
        Voltage in Clean-Air        Vgas0 = Vref + Voffset

        Sensitivity Codes are hard-coded but can be reconfigured in Settings
         */

        // Convert CO
        Double co_ppm = (1.0 / (sensitivitycode_hashmap.get(AqiContentContract.Aqidata.SENSORAQICO) *
                tiagain_hashmap.get(AqiContentContract.Aqidata.SENSORAQICO) * Math.pow(10.0, -6.0)))
                * (av_v_co - (vRef + offset_hashmap.get(AqiContentContract.Aqidata.SENSORAQICO)));
        cv.put(AqiContentContract.Aqidata.SENSORPPMCO, co_ppm);
        Log.d(CAG, "CO PPM: " + Double.toString(co_ppm));

        // Convert NO2
        Double no2_ppm = (1.0 / (sensitivitycode_hashmap.get(AqiContentContract.Aqidata.SENSORAQINO2) *
                tiagain_hashmap.get(AqiContentContract.Aqidata.SENSORAQINO2) * Math.pow(10.0, -6.0)))
                * (av_v_no2 - (vRef + offset_hashmap.get(AqiContentContract.Aqidata.SENSORAQINO2)));
        cv.put(AqiContentContract.Aqidata.SENSORPPMNO2, no2_ppm);
        Log.d(CAG, "NO2 PPM: " + Double.toString(no2_ppm));

        // Convert O3
        Double o3_ppm = (1.0 / (sensitivitycode_hashmap.get(AqiContentContract.Aqidata.SENSORAQIO3) *
                tiagain_hashmap.get(AqiContentContract.Aqidata.SENSORAQIO3) * Math.pow(10.0, -6.0)))
                * (av_v_o3 - (vRef + offset_hashmap.get(AqiContentContract.Aqidata.SENSORAQIO3)));
        cv.put(AqiContentContract.Aqidata.SENSORPPMO3, o3_ppm);
        Log.d(CAG, "O3 PPM: " + Double.toString(o3_ppm));

        // Convert SO2
        Double so2_ppm = (1.0 / (sensitivitycode_hashmap.get(AqiContentContract.Aqidata.SENSORAQISO2) *
                tiagain_hashmap.get(AqiContentContract.Aqidata.SENSORAQISO2) * Math.pow(10.0, -6.0)))
                * (av_v_so2 - (vRef + offset_hashmap.get(AqiContentContract.Aqidata.SENSORAQISO2)));
        cv.put(AqiContentContract.Aqidata.SENSORPPMSO2, so2_ppm);
        Log.d(CAG, "SO2 PPM: " + Double.toString(so2_ppm));

        /*
        CALCULATIONS: Voltage to Micrograms per Cubic Meter ug/m3 for Particulate Matter
        Follows 3rd Order Polynomial Trendline Generated from "Reference Only" Graph in Datasheet
        x = Low Pulse Occupancy (LPO)
        y = ug/m^3 of PM in air

        y = -0.11*x^3 + 6.55*x^2 + 28.9147*x + 40.2479
         */
        Double pm_raw = av_l_pm;
        Double pm_ug = (-0.11 * pm_raw * pm_raw * pm_raw) + (6.55 * pm_raw * pm_raw) + (28.9147 * pm_raw) + 0.8098;
        cv.put(AqiContentContract.Aqidata.SENSORLPOPM, pm_ug);
        Log.d("Data Calcuation", "PM (ug/m3): " + Double.toString(pm_ug));

        Double pml_raw = av_l_pml;
        Double pml_ug = (-0.11 * pml_raw * pml_raw * pml_raw) + (6.55 * pml_raw * pml_raw) + (28.9147 * pml_raw);
        cv.put(AqiContentContract.Aqidata.SENSORLPOPML, pml_ug);
        Log.d("Data Calcuation", "PML (ug/m3): " + Double.toString(pml_ug));

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
            co_aqi = -1.0; // error, shouldnt' happen.
        if (co_aqi > 0)
            cv.put(AqiContentContract.Aqidata.SENSORAQICO, co_aqi);
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
            no2_aqi = 2.0; // should never happen.
        cv.put(AqiContentContract.Aqidata.SENSORAQINO2, no2_aqi);
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
            o3_aqi = -1.0; // should never happen
        if (o3_aqi > 0)
            cv.put(AqiContentContract.Aqidata.SENSORAQIO3, o3_aqi);
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
            so2_aqi = -1.0;
        if (so2_aqi > 0)
            cv.put(AqiContentContract.Aqidata.SENSORAQISO2, so2_aqi);
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
            pm_aqi = -1.0; // shouldn't happen.
        if (pm_aqi > 0)
            cv.put(AqiContentContract.Aqidata.SENSORAQIPM, pm_aqi);
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
            pml_aqi = -1.0; // uh yeah.
        if (pml_aqi > 0)
            cv.put(AqiContentContract.Aqidata.SENSORAQIPML, pml_aqi);
        Log.d("Data Calculation", "PML AQI: " + Double.toString(pml_aqi));


        return cv;
    }

    public void reset() {

        voltage_co.clear();
        voltage_no2.clear();
        voltage_o3.clear();
        voltage_so2.clear();
        lpo_pm.clear();
        lpo_pml.clear();

        // Average Voltages
        av_v_co = 0.0;
        av_v_no2 = 0.0;
        av_v_o3 = 0.0;
        av_v_so2 = 0.0;
        av_l_pm = 0.0;
        av_l_pml = 0.0;

        packetCount = 0;
    }

    public static double calculateAverage(ArrayList<Double> marks) {
        Double sum = 0.0;
        if(!marks.isEmpty()) {
            for (Double mark : marks) {
                sum += mark;
            }
            return sum / marks.size();
        }
        return sum;
    }

}
