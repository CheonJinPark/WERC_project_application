package uconn.werc_project_application.data;

import android.database.Cursor;

import java.util.UUID;

/**
 * Created by Bill Brown on 2/23/2018.
 *
 * So-far unused class. Efficacy is to be determined
 *
 *
 */

public class Datapoint {
    /* Member Variables */
    // Device Metadata
    private long id = -1;
    private String table;
    private String packetId;
    private String userId;
    private long time;
    private String deviceId;
    // GPS
    private long gps_lat;
    private long gps_long;
    // Sensor Data
    private long sensor_co;
    private long sensor_no2;
    private long sensor_o3;
    private long sensor_pm;
    private long sensor_so2;
    private long sensor_temp;
    // Raw Sensor Data
    private long sensor_raw_co;
    private long sensor_raw_no2;
    private long sensor_raw_o3;
    private long sensor_raw_pm;
    private long sensor_raw_so2;
    private long sensor_raw_temp;


//    public Datapoint() {
//        setPacketId(UUID.randomUUID().toString());
//    }

    public static Datapoint fromCursorQuery(Cursor c)
    {
        Datapoint dp = new Datapoint();

        dp.setPacketId(getString(c, SensorContentContract.Sensordata.PACKETID, ""));
        dp.setUserId(getString(c, SensorContentContract.Sensordata._ID, ""));
        dp.setTime(getLong(c, SensorContentContract.Sensordata.TIME, -1));
        dp.setDeviceId(getString(c, SensorContentContract.Sensordata.DEVICEID, ""));
        dp.setGps_lat(getLong(c, SensorContentContract.Sensordata.GPSLAT, -1));
        dp.setGps_long(getLong(c, SensorContentContract.Sensordata.GPSLONG, -1 ));
        dp.setSensor_co(getLong(c, SensorContentContract.Sensordata.SENSORCO, -1));
        dp.setSensor_no2(getLong(c, SensorContentContract.Sensordata.SENSORNO2, -1));
        dp.setSensor_o3(getLong(c, SensorContentContract.Sensordata.SENSORO3, -1));
        dp.setSensor_pm(getLong(c, SensorContentContract.Sensordata.SENSORPM, -1));
        dp.setSensor_so2(getLong(c, SensorContentContract.Sensordata.SENSORSO2, -1));

        return dp;
    }

    /**
     * Read a string from a key in the cursor
     *
     * @param c the cursor to read from
     * @param col the column key
     * @param defaultValue the default value if the column key does not exist in the cursor
     * @return the value of the key
     */
    private static String getString(Cursor c, String col, String defaultValue) {
        if (c.getColumnIndex(col) >= 0) {
            return c.getString(c.getColumnIndex(col));
        } else {
            return defaultValue;
        }
    }


    /**
     * Read a long value from a key in the cursor
     *
     * @param c the cursor to read from
     * @param col the column key
     * @param defaultValue the default value if the column key does not exist in the cursor
     * @return the value of the key
     */
    private static long getLong(Cursor c, String col, long defaultValue) {
        if (c.getColumnIndex(col) >= 0) {
            return c.getLong(c.getColumnIndex(col));
        } else {
            return defaultValue;
        }
    }



    public long getSensor_temp() {
        return sensor_temp;
    }

    public void setSensor_temp(long sensor_temp) {
        this.sensor_temp = sensor_temp;
    }

    public long getSensor_raw_temp() {
        return sensor_raw_temp;
    }

    public void setSensor_raw_temp(long sensor_raw_temp) {
        this.sensor_raw_temp = sensor_raw_temp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getGps_lat() {
        return gps_lat;
    }

    public void setGps_lat(long gps_lat) {
        this.gps_lat = gps_lat;
    }

    public long getGps_long() {
        return gps_long;
    }

    public void setGps_long(long gps_long) {
        this.gps_long = gps_long;
    }

    public long getSensor_co() {
        return sensor_co;
    }

    public void setSensor_co(long sensor_co) {
        this.sensor_co = sensor_co;
    }

    public long getSensor_no2() {
        return sensor_no2;
    }

    public void setSensor_no2(long sensor_no2) {
        this.sensor_no2 = sensor_no2;
    }

    public long getSensor_o3() {
        return sensor_o3;
    }

    public void setSensor_o3(long sensor_o3) {
        this.sensor_o3 = sensor_o3;
    }

    public long getSensor_pm() {
        return sensor_pm;
    }

    public void setSensor_pm(long sensor_pm) {
        this.sensor_pm = sensor_pm;
    }

    public long getSensor_so2() {
        return sensor_so2;
    }

    public void setSensor_so2(long sensor_so2) {
        this.sensor_so2 = sensor_so2;
    }

    public long getSensor_raw_co() {
        return sensor_raw_co;
    }

    public void setSensor_raw_co(long sensor_raw_co) {
        this.sensor_raw_co = sensor_raw_co;
    }

    public long getSensor_raw_no2() {
        return sensor_raw_no2;
    }

    public void setSensor_raw_no2(long sensor_raw_no2) {
        this.sensor_raw_no2 = sensor_raw_no2;
    }

    public long getSensor_raw_o3() {
        return sensor_raw_o3;
    }

    public void setSensor_raw_o3(long sensor_raw_o3) {
        this.sensor_raw_o3 = sensor_raw_o3;
    }

    public long getSensor_raw_pm() {
        return sensor_raw_pm;
    }

    public void setSensor_raw_pm(long sensor_raw_pm) {
        this.sensor_raw_pm = sensor_raw_pm;
    }

    public long getSensor_raw_so2() {
        return sensor_raw_so2;
    }

    public void setSensor_raw_so2(long sensor_raw_so2) {
        this.sensor_raw_so2 = sensor_raw_so2;
    }
}

