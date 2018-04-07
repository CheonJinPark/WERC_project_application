package uconn.werc_project_application.data;

import android.content.ContentValues;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Bill on 4/6/2018.
 */

public class DataAverageContainer {
    private static final String TAG = "DataAverageContainer";
    private static final int PACKETTHRESHOLD = 80;
    // Metadata
    private int packetCount = 0;
    // Sensor Data
    private ArrayList<Double> ppm_co = new ArrayList<>();
    private ArrayList<Double> ppm_no2 = new ArrayList<>();
    private ArrayList<Double> ppm_o3 = new ArrayList<>();
    private ArrayList<Double> ppm_so2 = new ArrayList<>();
    private ArrayList<Double> ugm3_pm = new ArrayList<>();
    private ArrayList<Double> ugm3_pml = new ArrayList<>();
    // Raw Sensor Data
    private ArrayList<Double> voltage_co = new ArrayList<>();
    private ArrayList<Double> voltage_no2 = new ArrayList<>();
    private ArrayList<Double> voltage_o3 = new ArrayList<>();
    private ArrayList<Double> voltage_so2 = new ArrayList<>();
    private ArrayList<Double> lpo_pm = new ArrayList<>();
    private ArrayList<Double> lpo_pml = new ArrayList<>();
    // Converted AQI Values
    private ArrayList<Double> aqi_co = new ArrayList<>();
    private ArrayList<Double> aqi_no2 = new ArrayList<>();
    private ArrayList<Double> aqi_o3 = new ArrayList<>();
    private ArrayList<Double> aqi_so2 = new ArrayList<>();
    private ArrayList<Double> aqi_pm = new ArrayList<>();
    private ArrayList<Double> aqi_pml = new ArrayList<>();

    /* Average Values
     */

    // Average Voltages
    Double av_v_co = 0.0;
    Double av_v_no2 = 0.0;
    Double av_v_o3 = 0.0;
    Double av_v_so2 = 0.0;
    Double av_l_pm = 0.0;
    Double av_l_pml = 0.0;
    // Average PPMs
    Double av_p_co = 0.0;
    Double av_p_no2 = 0.0;
    Double av_p_o3 = 0.0;
    Double av_p_so2 = 0.0;
    Double av_u_pm = 0.0;
    Double av_u_pml = 0.0;
    // Average AQI
    Double av_a_co = 0.0;
    Double av_a_no2 = 0.0;
    Double av_a_o3 = 0.0;
    Double av_a_so2 = 0.0;
    Double av_a_pm = 0.0;
    Double av_a_pml = 0.0;




    public DataAverageContainer() {

    }

    public void average_ppm_co(Double val)
    {
        ppm_co.add(val);
    }

    public void average_ppm_no2(Double val)
    {
        ppm_no2.add(val);
    }

    public void average_ppm_o3(Double val)
    {
        ppm_o3.add(val);
    }
    public void average_ppm_so2(Double val)
    {
        ppm_so2.add(val);
    }
    public void average_ugm3_pm(Double val)
    {
        ugm3_pm.add(val);
    }
    public void average_ugm3_pml(Double val)
    {
        ugm3_pml.add(val);
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

    public void average_aqi_co(Double val)
    {
        aqi_co.add(val);
    }

    public void average_aqi_no2(Double val)
    {
        aqi_no2.add(val);
    }

    public void average_aqi_o3(Double val)
    {
        aqi_o3.add(val);
    }

    public void average_aqi_so2(Double val)
    {
        aqi_so2.add(val);
    }

    public void average_aqi_pm(Double val)
    {
        aqi_pm.add(val);
    }

    public void average_aqi_pml(Double val)
    {
        aqi_pml.add(val);
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
        // Update Average PPM
        av_p_co = calculateAverage(ppm_co);
        Log.d(TAG, "Average PPM CO: " + Double.toString(av_p_co));
        av_p_no2 = calculateAverage(ppm_no2);
        Log.d(TAG, "Average PPM NO2: " + Double.toString(av_p_no2));
        av_p_o3 = calculateAverage(ppm_o3);
        Log.d(TAG, "Average PPM O3: " + Double.toString(av_p_o3));
        av_p_so2 = calculateAverage(ppm_so2);
        Log.d(TAG, "Average PPM SO2: " + Double.toString(av_p_so2));
        av_u_pm = calculateAverage(ugm3_pm);
        Log.d(TAG, "Average UGM3 PM: " + Double.toString(av_u_pm));
        av_u_pml = calculateAverage(ugm3_pml);
        Log.d(TAG, "Average UGM3 PML: " + Double.toString(av_u_pml));
        // Update Average AQI
        av_a_co = calculateAverage(aqi_co);
        Log.d(TAG, "Average AQI CO: " + Double.toString(av_a_co));
        av_a_no2 = calculateAverage(aqi_no2);
        Log.d(TAG, "Average AQI NO2: " + Double.toString(av_a_no2));
        av_a_o3 = calculateAverage(aqi_o3);
        Log.d(TAG, "Average AQI O3: " + Double.toString(av_a_o3));
        av_a_so2 = calculateAverage(aqi_so2);
        Log.d(TAG, "Average AQI SO2: " + Double.toString(av_a_so2));
        av_a_pm = calculateAverage(aqi_pm);
        Log.d(TAG, "Average AQI PM: " + Double.toString(av_a_pm));
        av_a_pml = calculateAverage(aqi_pml);
        Log.d(TAG, "Average AQI PML: " + Double.toString(av_a_pm));
    }

    public boolean overThreshold() {
        return packetCount > PACKETTHRESHOLD;
    }

    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(AqiContentContract.Aqidata.SENSORAQICO, av_a_co);
        cv.put(AqiContentContract.Aqidata.SENSORAQINO2, av_a_no2);
        cv.put(AqiContentContract.Aqidata.SENSORAQIO3, av_a_o3);
        cv.put(AqiContentContract.Aqidata.SENSORAQISO2, av_a_so2);
        cv.put(AqiContentContract.Aqidata.SENSORAQIPM, av_a_pm);
        cv.put(AqiContentContract.Aqidata.SENSORAQIPML, av_a_pml);

        return cv;
    }

    public void reset() {
        ppm_co.clear();
        ppm_no2.clear();
        ppm_o3.clear();
        ppm_so2.clear();
        ugm3_pm.clear();
        ugm3_pml.clear();


        voltage_co.clear();
        voltage_no2.clear();
        voltage_o3.clear();
        voltage_so2.clear();
        lpo_pm.clear();
        lpo_pml.clear();

        aqi_co.clear();
        aqi_no2.clear();
        aqi_o3.clear();
        aqi_pm.clear();
        aqi_pml.clear();
        aqi_so2.clear();

        // Average Voltages
        av_v_co = 0.0;
        av_v_no2 = 0.0;
        av_v_o3 = 0.0;
        av_v_so2 = 0.0;
        av_l_pm = 0.0;
        av_l_pml = 0.0;
        // Average PPMs
        av_p_co = 0.0;
        av_p_no2 = 0.0;
        av_p_o3 = 0.0;
        av_p_so2 = 0.0;
        av_u_pm = 0.0;
        av_u_pml = 0.0;
        // Average AQI
        av_a_co = 0.0;
        av_a_no2 = 0.0;
        av_a_o3 = 0.0;
        av_a_so2 = 0.0;
        av_a_pm = 0.0;
        av_a_pml = 0.0;

        packetCount = 0;
    }

    private double calculateAverage(ArrayList<Double> marks) {
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
