package uconn.werc_project_application.data;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "sensorem-mobilehub-1103649196-sensordata")

public class SensordataDO {
    private String _projectId;
    private Double _time;
    private String _aqiSrc;
    private Double _aqiVal;
    private String _deviceId;
    private Double _gpsLat;
    private Double _gpsLong;
    private String _packetId;
    private Double _sensorRawCo;
    private Double _sensorRawNo2;
    private Double _sensorRawO3;
    private Double _sensorRawPm;
    private Double _sensorRawPml;
    private Double _sensorRawSo2;
    private String _userId;

    @DynamoDBHashKey(attributeName = "projectId")
    @DynamoDBAttribute(attributeName = "projectId")
    public String getProjectId() {
        return _projectId;
    }

    public void setProjectId(final String _projectId) {
        this._projectId = _projectId;
    }
    @DynamoDBRangeKey(attributeName = "time")
    @DynamoDBAttribute(attributeName = "time")
    public Double getTime() {
        return _time;
    }

    public void setTime(final Double _time) {
        this._time = _time;
    }
    @DynamoDBAttribute(attributeName = "aqi_src")
    public String getAqiSrc() {
        return _aqiSrc;
    }

    public void setAqiSrc(final String _aqiSrc) {
        this._aqiSrc = _aqiSrc;
    }
    @DynamoDBAttribute(attributeName = "aqi_val")
    public Double getAqiVal() {
        return _aqiVal;
    }

    public void setAqiVal(final Double _aqiVal) {
        this._aqiVal = _aqiVal;
    }
    @DynamoDBAttribute(attributeName = "deviceId")
    public String getDeviceId() {
        return _deviceId;
    }

    public void setDeviceId(final String _deviceId) {
        this._deviceId = _deviceId;
    }
    @DynamoDBAttribute(attributeName = "gps_lat")
    public Double getGpsLat() {
        return _gpsLat;
    }

    public void setGpsLat(final Double _gpsLat) {
        this._gpsLat = _gpsLat;
    }
    @DynamoDBAttribute(attributeName = "gps_long")
    public Double getGpsLong() {
        return _gpsLong;
    }

    public void setGpsLong(final Double _gpsLong) {
        this._gpsLong = _gpsLong;
    }
    @DynamoDBAttribute(attributeName = "packetId")
    public String getPacketId() {
        return _packetId;
    }

    public void setPacketId(final String _packetId) {
        this._packetId = _packetId;
    }
    @DynamoDBAttribute(attributeName = "sensor_raw_co")
    public Double getSensorRawCo() {
        return _sensorRawCo;
    }

    public void setSensorRawCo(final Double _sensorRawCo) {
        this._sensorRawCo = _sensorRawCo;
    }
    @DynamoDBAttribute(attributeName = "sensor_raw_no2")
    public Double getSensorRawNo2() {
        return _sensorRawNo2;
    }

    public void setSensorRawNo2(final Double _sensorRawNo2) {
        this._sensorRawNo2 = _sensorRawNo2;
    }
    @DynamoDBAttribute(attributeName = "sensor_raw_o3")
    public Double getSensorRawO3() {
        return _sensorRawO3;
    }

    public void setSensorRawO3(final Double _sensorRawO3) {
        this._sensorRawO3 = _sensorRawO3;
    }
    @DynamoDBAttribute(attributeName = "sensor_raw_pm")
    public Double getSensorRawPm() {
        return _sensorRawPm;
    }

    public void setSensorRawPm(final Double _sensorRawPm) {
        this._sensorRawPm = _sensorRawPm;
    }
    @DynamoDBAttribute(attributeName = "sensor_raw_pml")
    public Double getSensorRawPml() {
        return _sensorRawPml;
    }

    public void setSensorRawPml(final Double _sensorRawPml) {
        this._sensorRawPml = _sensorRawPml;
    }
    @DynamoDBAttribute(attributeName = "sensor_raw_so2")
    public Double getSensorRawSo2() {
        return _sensorRawSo2;
    }

    public void setSensorRawSo2(final Double _sensorRawSo2) {
        this._sensorRawSo2 = _sensorRawSo2;
    }
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }

}
