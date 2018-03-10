package uconn.werc_project_application;

import java.io.Serializable;

/**
 * Created by Cheonjin Park on 2/16/2018.
 */



public class GPS implements Serializable{
    private double longitude;
    private double  latitude;
    private double o3, co, so2, dust, no2;

    public double getO3() {
        return o3;
    }

    public void setO3(double o3) {
        this.o3 = o3;
    }

    public double getCo() {
        return co;
    }

    public void setCo(double co) {
        this.co = co;
    }

    public double getSo2() {
        return so2;
    }

    public void setSo2(double so2) {
        this.so2 = so2;
    }

    public double getDust() {
        return dust;
    }

    public void setDust(double dust) {
        this.dust = dust;
    }

    public double getNo2() {
        return no2;
    }

    public void setNo2(double no2) {
        this.no2 = no2;
    }

    public GPS(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
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

    public String showInfo(){
        String result = "";
        result = "O3 : "+Double.toString(o3)+System.getProperty("line.separator")+"CO : "+Double.toString(co)+System.getProperty("line.separator")+"SO2 : "+Double.toString(so2)+System.getProperty("line.separator")+"NO2 : "+Double.toString(no2)+System.getProperty("line.separator")+"Dust : "+Double.toString(dust);
        return result;
    }
}

