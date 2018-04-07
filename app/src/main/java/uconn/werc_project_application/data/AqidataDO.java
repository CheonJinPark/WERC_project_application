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
    private Double _sensorLpoPm;
    private Double _sensorLpoPml;
    private Double _sensorPpmCo;
    private Double _sensorPpmNo2;
    private Double _sensorPpmO3;
    private Double _sensorPpmSo2;
    private Double _sensorUgm3Pm;
    private Double _sensorUgm3Pml;
    private Double _sensorVoltCo;
    private Double _sensorVoltNo2;
    private Double _sensorVoltO3;
    private Double _sensorVoltSo2;
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
    @DynamoDBAttribute(attributeName = "sensor_lpo_pm")
    public Double getSensorLpoPm() {
        return _sensorLpoPm;
    }

    public void setSensorLpoPm(final Double _sensorLpoPm) {
        this._sensorLpoPm = _sensorLpoPm;
    }
    @DynamoDBAttribute(attributeName = "sensor_lpo_pml")
    public Double getSensorLpoPml() {
        return _sensorLpoPml;
    }

    public void setSensorLpoPml(final Double _sensorLpoPml) {
        this._sensorLpoPml = _sensorLpoPml;
    }
    @DynamoDBAttribute(attributeName = "sensor_ppm_co")
    public Double getSensorPpmCo() {
        return _sensorPpmCo;
    }

    public void setSensorPpmCo(final Double _sensorPpmCo) {
        this._sensorPpmCo = _sensorPpmCo;
    }
    @DynamoDBAttribute(attributeName = "sensor_ppm_no2")
    public Double getSensorPpmNo2() {
        return _sensorPpmNo2;
    }

    public void setSensorPpmNo2(final Double _sensorPpmNo2) {
        this._sensorPpmNo2 = _sensorPpmNo2;
    }
    @DynamoDBAttribute(attributeName = "sensor_ppm_o3")
    public Double getSensorPpmO3() {
        return _sensorPpmO3;
    }

    public void setSensorPpmO3(final Double _sensorPpmO3) {
        this._sensorPpmO3 = _sensorPpmO3;
    }
    @DynamoDBAttribute(attributeName = "sensor_ppm_so2")
    public Double getSensorPpmSo2() {
        return _sensorPpmSo2;
    }

    public void setSensorPpmSo2(final Double _sensorPpmSo2) {
        this._sensorPpmSo2 = _sensorPpmSo2;
    }
    @DynamoDBAttribute(attributeName = "sensor_ugm3_pm")
    public Double getSensorUgm3Pm() {
        return _sensorUgm3Pm;
    }

    public void setSensorUgm3Pm(final Double _sensorUgm3Pm) {
        this._sensorUgm3Pm = _sensorUgm3Pm;
    }
    @DynamoDBAttribute(attributeName = "sensor_ugm3_pml")
    public Double getSensorUgm3Pml() {
        return _sensorUgm3Pml;
    }

    public void setSensorUgm3Pml(final Double _sensorUgm3Pml) {
        this._sensorUgm3Pml = _sensorUgm3Pml;
    }
    @DynamoDBAttribute(attributeName = "sensor_volt_co")
    public Double getSensorVoltCo() {
        return _sensorVoltCo;
    }

    public void setSensorVoltCo(final Double _sensorVoltCo) {
        this._sensorVoltCo = _sensorVoltCo;
    }
    @DynamoDBAttribute(attributeName = "sensor_volt_no2")
    public Double getSensorVoltNo2() {
        return _sensorVoltNo2;
    }

    public void setSensorVoltNo2(final Double _sensorVoltNo2) {
        this._sensorVoltNo2 = _sensorVoltNo2;
    }
    @DynamoDBAttribute(attributeName = "sensor_volt_o3")
    public Double getSensorVoltO3() {
        return _sensorVoltO3;
    }

    public void setSensorVoltO3(final Double _sensorVoltO3) {
        this._sensorVoltO3 = _sensorVoltO3;
    }
    @DynamoDBAttribute(attributeName = "sensor_volt_so2")
    public Double getSensorVoltSo2() {
        return _sensorVoltSo2;
    }

    public void setSensorVoltSo2(final Double _sensorVoltSo2) {
        this._sensorVoltSo2 = _sensorVoltSo2;
    }
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }

}
