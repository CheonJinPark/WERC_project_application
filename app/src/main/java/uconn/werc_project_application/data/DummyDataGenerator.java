package uconn.werc_project_application.data;

import android.content.ContentValues;
import android.content.Context;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Bill on 2/25/2018.
 *
 * Singleton for spoof data generation. This is a debugging tool that will not see implementation
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
            else if (field.equals(SensorContentContract.Sensordata.DEVICEID))
                values.put(field, UUID.randomUUID().toString());
            else
                values.put(field, (min + (max - min) * r.nextDouble()));
        }

        return values;
    }

}
