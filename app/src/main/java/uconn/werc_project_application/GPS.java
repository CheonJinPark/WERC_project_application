package uconn.werc_project_application;

import java.io.Serializable;

/**
 * Created by Cheonjin Park on 2/16/2018.
 */



public class GPS implements Serializable{
    private double longitude;
    private double  latitude;

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
}
