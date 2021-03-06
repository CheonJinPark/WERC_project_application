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

@DynamoDBTable(tableName = "sensorem-mobilehub-1103649196-senordata")

public class SenordataDO {
    private String _userId;
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
    private Double _sensorCo;
    private Double _sensorNo2;
    private Double _sensorO3;
    private Double _sensorPm;
    private Double _sensorPml;
    private Double _sensorRawCo;
    private Double _sensorRawNo2;
    private Double _sensorRawO3;
    private Double _sensorRawPm;
    private Double _sensorRawPml;
    private Double _sensorRawSo2;
    private Double _sensorRawTemp;
    private Double _sensorSo2;
    private Double _sensorTemp;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
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
    @DynamoDBAttribute(attributeName = "sensor_co")
    public Double getSensorCo() {
        return _sensorCo;
    }

    public void setSensorCo(final Double _sensorCo) {
        this._sensorCo = _sensorCo;
    }
    @DynamoDBAttribute(attributeName = "sensor_no2")
    public Double getSensorNo2() {
        return _sensorNo2;
    }

    public void setSensorNo2(final Double _sensorNo2) {
        this._sensorNo2 = _sensorNo2;
    }
    @DynamoDBAttribute(attributeName = "sensor_o3")
    public Double getSensorO3() {
        return _sensorO3;
    }

    public void setSensorO3(final Double _sensorO3) {
        this._sensorO3 = _sensorO3;
    }
    @DynamoDBAttribute(attributeName = "sensor_pm")
    public Double getSensorPm() {
        return _sensorPm;
    }

    public void setSensorPm(final Double _sensorPm) {
        this._sensorPm = _sensorPm;
    }
    @DynamoDBAttribute(attributeName = "sensor_pml")
    public Double getSensorPml() {
        return _sensorPml;
    }

    public void setSensorPml(final Double _sensorPml) {
        this._sensorPml = _sensorPml;
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
    @DynamoDBAttribute(attributeName = "sensor_raw_temp")
    public Double getSensorRawTemp() {
        return _sensorRawTemp;
    }

    public void setSensorRawTemp(final Double _sensorRawTemp) {
        this._sensorRawTemp = _sensorRawTemp;
    }
    @DynamoDBAttribute(attributeName = "sensor_so2")
    public Double getSensorSo2() {
        return _sensorSo2;
    }

    public void setSensorSo2(final Double _sensorSo2) {
        this._sensorSo2 = _sensorSo2;
    }
    @DynamoDBAttribute(attributeName = "sensor_temp")
    public Double getSensorTemp() {
        return _sensorTemp;
    }

    public void setSensorTemp(final Double _sensorTemp) {
        this._sensorTemp = _sensorTemp;
    }

}
