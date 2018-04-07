package uconn.werc_project_application.data;

import android.content.ContentValues;
import android.content.Context;
import android.location.LocationListener;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import uconn.werc_project_application.AWSProvider;

/**
 * Created by Bill on 2/25/2018.
 *
 * For spoof data generation. This is a debugging tool that will not see implementation
 * in the final build.
 */

public class DummyDataGenerator {
    private static String[] table_fields;

    public DummyDataGenerator(String[] fields) {
        this.table_fields = fields;

    }

    public ContentValues generate(double min, double max) {
        ContentValues values = new ContentValues();
        Random r = new Random();
        for (String field : table_fields)
        {
            if (field.equals(SensorContentContract.Sensordata.TIME))
                values.put(field, new Date().getTime());
            else if (field.equals(SensorContentContract.Sensordata.PACKETID))
                values.put(field, UUID.randomUUID().toString());
            else if (field.equals(SensorContentContract.Sensordata.PROJECTID))
                values.put(field, "werc");
            else if (field.equals(SensorContentContract.Sensordata._ID))
                values.put(field, AWSProvider.getInstance().getIdentityManager().getCachedUserID());
            else if (field.equals(SensorContentContract.Sensordata.GPSLAT)) {
                values.put(field, DataInterpreter.getInstance().getGpsLat());
            }
            else if (field.equals(SensorContentContract.Sensordata.GPSLONG))
                values.put(field, DataInterpreter.getInstance().getGpsLong());
            else
                values.put(field, (min + (max - min) * r.nextDouble()));
        }

        return values;
    }

}
