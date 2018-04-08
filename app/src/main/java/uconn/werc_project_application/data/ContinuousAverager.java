package uconn.werc_project_application.data;

import android.content.ContentValues;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Bill on 4/7/2018.
 */

public class ContinuousAverager {
    private static final String CAG = "ContinuousAverager";

    // Metadata
    private int packetCount = 0;
    private Double vRef = 1.665;

    // Average Voltages
    private Double av_v_co = 0.0;
    private Double av_v_no2 = 0.0;
    private Double av_v_o3 = 0.0;
    private Double av_v_so2 = 0.0;
    private Double av_l_pm = 0.0;
    private Double av_l_pml = 0.0;

    // Raw Sensor Data
    private LinkedList<Double> voltage_co = new LinkedList<Double>();
    private LinkedList<Double> voltage_no2 = new LinkedList<>();
    private LinkedList<Double> voltage_o3 = new LinkedList<>();
    private LinkedList<Double> voltage_so2 = new LinkedList<>();
    private LinkedList<Double> lpo_pm = new LinkedList<>();
    private LinkedList<Double> lpo_pml = new LinkedList<>();

    // AQI Sensor Data
    private LinkedList<Double> aqi_co = new LinkedList<>();
    private LinkedList<Double> aqi_no2 = new LinkedList<>();
    private LinkedList<Double> aqi_o3 = new LinkedList<>();
    private LinkedList<Double> aqi_so2 = new LinkedList<>();
    private LinkedList<Double> aqi_pm = new LinkedList<>();
    private LinkedList<Double> aqi_pml = new LinkedList<>();

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

    public ContinuousAverager(HashMap<String, Double> sensitivitycode_hashmap, HashMap<String, Double> tiagain_hashmap, HashMap<String, Double> offset_hashmap, HashMap<Double, Double> aqihm_co, HashMap<Double, Double> aqihm_no2, HashMap<Double, Double> aqihm_o3, HashMap<Double, Double> aqihm_so2, HashMap<Double, Double> aqihm_pm, HashMap<Double, Double> aqihm_pml) {
        this.sensitivitycode_hashmap = sensitivitycode_hashmap;
        this.tiagain_hashmap = tiagain_hashmap;
        this.offset_hashmap = offset_hashmap;
        this.aqihm_co = aqihm_co;
        this.aqihm_no2 = aqihm_no2;
        this.aqihm_o3 = aqihm_o3;
        this.aqihm_so2 = aqihm_so2;
        this.aqihm_pm = aqihm_pm;
        this.aqihm_pml = aqihm_pml;
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

    private void average_aqi_update() {
        av_v_co = ContinuousAverager.calculateAverage(voltage_co);
        Double co_ppm = (1.0 / (sensitivitycode_hashmap.get(AqiContentContract.Aqidata.SENSORAQICO) *
                tiagain_hashmap.get(AqiContentContract.Aqidata.SENSORAQICO) * Math.pow(10.0, -6.0)))
                * (av_v_co - (vRef + offset_hashmap.get(AqiContentContract.Aqidata.SENSORAQICO)));

        // Convert NO2
        av_v_no2 = ContinuousAverager.calculateAverage(voltage_no2);
        Double no2_ppm = (1.0 / (sensitivitycode_hashmap.get(AqiContentContract.Aqidata.SENSORAQINO2) *
                tiagain_hashmap.get(AqiContentContract.Aqidata.SENSORAQINO2) * Math.pow(10.0, -6.0)))
                * (av_v_no2 - (vRef + offset_hashmap.get(AqiContentContract.Aqidata.SENSORAQINO2)));

        // Convert O3
        av_v_o3 = ContinuousAverager.calculateAverage(voltage_o3);
        Double o3_ppm = (1.0 / (sensitivitycode_hashmap.get(AqiContentContract.Aqidata.SENSORAQIO3) *
                tiagain_hashmap.get(AqiContentContract.Aqidata.SENSORAQIO3) * Math.pow(10.0, -6.0)))
                * (av_v_o3 - (vRef + offset_hashmap.get(AqiContentContract.Aqidata.SENSORAQIO3)));

        // Convert SO2
        av_v_so2 = ContinuousAverager.calculateAverage(voltage_so2);
        Double so2_ppm = (1.0 / (sensitivitycode_hashmap.get(AqiContentContract.Aqidata.SENSORAQISO2) *
                tiagain_hashmap.get(AqiContentContract.Aqidata.SENSORAQISO2) * Math.pow(10.0, -6.0)))
                * (av_v_so2 - (vRef + offset_hashmap.get(AqiContentContract.Aqidata.SENSORAQISO2)));

        /*
        CALCULATIONS: Voltage to Micrograms per Cubic Meter ug/m3 for Particulate Matter
        Follows 3rd Order Polynomial Trendline Generated from "Reference Only" Graph in Datasheet
        x = Low Pulse Occupancy (LPO)
        y = ug/m^3 of PM in air

        y = -0.11*x^3 + 6.55*x^2 + 28.9147*x + 40.2479
         */
        Double pm_raw = ContinuousAverager.calculateAverage(lpo_pm);
        Double pm_ug = (-0.11 * pm_raw * pm_raw * pm_raw) + (6.55 * pm_raw * pm_raw) + (28.9147 * pm_raw) + 0.8098;

        Double pml_raw = ContinuousAverager.calculateAverage(lpo_pml);
        Double pml_ug = (-0.11 * pml_raw * pml_raw * pml_raw) + (6.55 * pml_raw * pml_raw) + (28.9147 * pml_raw);

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
            aqi_co.add(co_aqi);
        Log.d(CAG, "CO AQI: " + Double.toString(co_aqi));

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
        aqi_no2.add(no2_aqi);
        Log.d(CAG, "NO2 AQI: " + Double.toString(no2_aqi));


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
            aqi_o3.add(o3_aqi);
        Log.d(CAG, "O3 AQI: " + Double.toString(o3_aqi));

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
            aqi_so2.add(so2_aqi);
        Log.d(CAG, "SO2 AQI: " + Double.toString(so2_aqi));

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
            aqi_pm.add(pm_aqi);
        Log.d(CAG, "PM AQI: " + Double.toString(pm_aqi));

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
            aqi_pml.add(pml_aqi);
        Log.d(CAG, "PML AQI: " + Double.toString(pml_aqi));

    }



    public ContentValues sampleAverage() {
        average_aqi_update();
        ContentValues cv = new ContentValues();


        cv.put(AqiContentContract.Aqidata.SENSORAQICO, ContinuousAverager.calculateAverage(aqi_co));
        if (aqi_co.size() > 120)
            aqi_co.removeFirst();

        cv.put(AqiContentContract.Aqidata.SENSORAQINO2, ContinuousAverager.calculateAverage(aqi_no2));
        if (aqi_no2.size() > 120)
            aqi_no2.removeFirst();

        cv.put(AqiContentContract.Aqidata.SENSORAQIO3, ContinuousAverager.calculateAverage(aqi_o3));
        if (aqi_o3.size() > 120)
            aqi_o3.removeFirst();

        cv.put(AqiContentContract.Aqidata.SENSORAQISO2, ContinuousAverager.calculateAverage(aqi_so2));
        if (aqi_so2.size() > 120)
            aqi_so2.removeFirst();

        cv.put(AqiContentContract.Aqidata.SENSORAQIPM, ContinuousAverager.calculateAverage(aqi_pm));
        if (aqi_pm.size() > 120)
            aqi_pm.removeFirst();

        cv.put(AqiContentContract.Aqidata.SENSORAQIPML, ContinuousAverager.calculateAverage(aqi_pml));
        if (aqi_pml.size() > 120)
            aqi_pml.removeFirst();

        cv.put(AqiContentContract.Aqidata.GPSLAT, DataInterpreter.getInstance().getGpsLat());
        cv.put(AqiContentContract.Aqidata.GPSLONG, DataInterpreter.getInstance().getGpsLong());

        cv.put(AqiContentContract.Aqidata.TIME, new Date().getTime());

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


    public void increment_packet_counter()
    {
        packetCount++;
    }

    public static double calculateAverage(LinkedList<Double> marks) {
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
