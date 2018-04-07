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

@DynamoDBTable(tableName = "sensorem-mobilehub-1103649196-aqidata")

public class AqidataDO {
    private String _projectId;
    private Double _time;
    private String _aqiSrc;
    private Double _aqiVal;
    private String _deviceId;
    private Double _gpsLat;
    private Double _gpsLong;
    private String _packetId;
    private Double _sensorAqiCo;
    private Double _sensorAqiNo2;
    private Double _sensorAqiO3;
    private Double _sensorAqiPm;
    private Double _sensorAqiPml;
    private Double _sensorAqiSo2;
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
    @DynamoDBAttribute(attributeName = "sensor_aqi_co")
    public Double getSensorAqiCo() {
        return _sensorAqiCo;
    }

    public void setSensorAqiCo(final Double _sensorAqiCo) {
        this._sensorAqiCo = _sensorAqiCo;
    }
    @DynamoDBAttribute(attributeName = "sensor_aqi_no2")
    public Double getSensorAqiNo2() {
        return _sensorAqiNo2;
    }

    public void setSensorAqiNo2(final Double _sensorAqiNo2) {
        this._sensorAqiNo2 = _sensorAqiNo2;
    }
    @DynamoDBAttribute(attributeName = "sensor_aqi_o3")
    public Double getSensorAqiO3() {
        return _sensorAqiO3;
    }

    public void setSensorAqiO3(final Double _sensorAqiO3) {
        this._sensorAqiO3 = _sensorAqiO3;
    }
    @DynamoDBAttribute(attributeName = "sensor_aqi_pm")
    public Double getSensorAqiPm() {
        return _sensorAqiPm;
    }

    public void setSensorAqiPm(final Double _sensorAqiPm) {
        this._sensorAqiPm = _sensorAqiPm;
    }
    @DynamoDBAttribute(attributeName = "sensor_aqi_pml")
    public Double getSensorAqiPml() {
        return _sensorAqiPml;
    }

    public void setSensorAqiPml(final Double _sensorAqiPml) {
        this._sensorAqiPml = _sensorAqiPml;
    }
    @DynamoDBAttribute(attributeName = "sensor_aqi_so2")
    public Double getSensorAqiSo2() {
        return _sensorAqiSo2;
    }

    public void setSensorAqiSo2(final Double _sensorAqiSo2) {
        this._sensorAqiSo2 = _sensorAqiSo2;
    }
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }

}
