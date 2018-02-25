package uconn.werc_project_application.data;

import android.content.ContentValues;
import android.content.Context;

import java.util.Random;

/**
 * Created by Bill on 2/25/2018.
 */

public class DummyDataGenerator {
    private static DummyDataGenerator instance = null;
    private static String[] table_fields;

    public static DummyDataGenerator getInstance() {
        return instance;
    }

    public static void initialize(String[] fields) {
        if (instance == null) {
            instance = new DummyDataGenerator(fields);
        }
    }
    private DummyDataGenerator(String[] fields) {
        this.table_fields = fields;

    }

    public ContentValues generate(double min, double max) {
        ContentValues values = new ContentValues();
        Random r = new Random();
        for (String field : table_fields)
        {
            double randomVal = (min + (max - min) * r.nextDouble());
            values.put(field, randomVal);
        }

        return values;
    }

}
