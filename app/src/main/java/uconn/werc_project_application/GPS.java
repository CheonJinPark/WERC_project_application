package uconn.werc_project_application;

import android.database.Cursor;
import android.icu.text.IDNA;

import com.Information.Information;

import java.io.Serializable;

import uconn.werc_project_application.data.AqiContentContract;

/**
 * Created by Cheonjin Park on 2/16/2018.
 */



public class GPS implements Serializable{

    private String userID;
    private double longitude;
    private double  latitude;
    private int o3;
    private int co;
    private int so2;
    private int pm;
    private int no2;
    private int aqival;
    private String aqi_type;

    public String getAqi_type() {
        return aqi_type;
    }

    public void setAqi_type(String aqi_type) {
        this.aqi_type = aqi_type;
    }

    public GPS(){

    }
    public int getAqival() {
        return aqival;
    }

    public void setAqival(int aqival) {
        this.aqival = aqival;
    }



    public int getO3() {
        return o3;
    }

    public void setO3(int o3) {
        this.o3 = o3;
    }

    public int getCo() {
        return co;
    }

    public void setCo(int co) {
        this.co = co;
    }

    public int getSo2() {
        return so2;
    }

    public void setSo2(int so2) {
        this.so2 = so2;
    }

    public int getPM() {
        return pm;
    }

    public void setPM(int pm) {
        this.pm = pm;
    }

    public int getNo2() {
        return no2;
    }

    public void setNo2(int no2) {
        this.no2 = no2;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public GPS(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public int get(String state) {
        int result = 0;
        switch (state) {
            case Information.CO:
                result = getCo();
                break;
            case Information.NO2:
                result= getNo2();
                break;
            case Information.SO2:
                result = getSo2();
                break;
            case Information.DUST:
                result = getPM();
                break;
            case Information.O3:
                result = getO3();
                break;
            case Information.AQIvalue:
                result = getAqival();
                break;
        }
        return result;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void savefromCursorQuery(Cursor cursor){
        String type = cursor.getString(cursor.getColumnIndex(AqiContentContract.Aqidata.AQISRC));
        switch (type){
            case "Carbon Monoxide":
                this.setAqi_type(Information.CO);
                break;
            case "Nitrogen Dioxide":
                this.setAqi_type(Information.NO2);
                break;
            case "Ozone":
                this.setAqi_type(Information.O3);
            case "Sulfur Dioxide":
                this.setAqi_type(Information.SO2);
                break;
            case "Small Particulate Matter":
                this.setAqi_type(Information.DUST);
                break;
            case "Large Particulate Matter":
                this.setAqi_type(Information.DUST);
                break;
            case "Unknown. How did you find this":
                this.setAqi_type(Information.DUST);
                break;
        }
        this.setUserID(cursor.getString(cursor.getColumnIndex(AqiContentContract.Aqidata._ID)));
        this.setLatitude(cursor.getDouble(cursor.getColumnIndex(AqiContentContract.Aqidata.GPSLAT)));
        this.setLongitude(cursor.getDouble(cursor.getColumnIndex((AqiContentContract.Aqidata.GPSLONG))));
        this.setAqival(cursor.getInt(cursor.getColumnIndex(AqiContentContract.Aqidata.AQIVAL)));
        this.setO3(cursor.getInt(cursor.getColumnIndex(AqiContentContract.Aqidata.SENSORAQIO3)));
        this.setCo(cursor.getInt(cursor.getColumnIndex(AqiContentContract.Aqidata.SENSORAQICO)));
        this.setSo2(cursor.getInt(cursor.getColumnIndex(AqiContentContract.Aqidata.SENSORAQISO2)));
        this.setPM(cursor.getInt(cursor.getColumnIndex(AqiContentContract.Aqidata.SENSORAQIPM)));
        this.setNo2(cursor.getInt(cursor.getColumnIndex(AqiContentContract.Aqidata.SENSORAQINO2)));


    }

}

