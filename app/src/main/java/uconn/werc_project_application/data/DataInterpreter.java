package uconn.werc_project_application.data;

import android.content.ContentValues;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Bill Brown on 3/27/2018.
 */

public class DataInterpreter {
    private String delimiter = ",";
    private String[] prefix_identifiers = {"NM=","CO=", "O3=", "NO=", "SO=", "PM=", "TM="};

    public DataInterpreter() {
    }

    public ContentValues interpretBLEData(String input) {
        String[] values = parseValues(input);
        ContentValues cv = new ContentValues();
        for (String val : values) {
            if (val.contains(prefix_identifiers[0])) {
                cv.put(SensorContentContract.Sensordata.DEVICEID, val.substring(0, val.length() - 3));
            } else if (val.contains(prefix_identifiers[1])) {
                cv.put(SensorContentContract.Sensordata.SENSORRAWCO, val.substring(0, val.length() - 3));
            } else if (val.contains(prefix_identifiers[2])) {
                cv.put(SensorContentContract.Sensordata.SENSORRAWO3, val.substring(0, val.length() - 3));
            } else if (val.contains(prefix_identifiers[3])) {
                cv.put(SensorContentContract.Sensordata.SENSORRAWNO2, val.substring(0, val.length() - 3));
            } else if (val.contains(prefix_identifiers[4])) {
                cv.put(SensorContentContract.Sensordata.SENSORRAWSO2, val.substring(0, val.length() - 3));
            } else if (val.contains(prefix_identifiers[5])) {
                cv.put(SensorContentContract.Sensordata.SENSORRAWPM, val.substring(0, val.length() - 3));
            } else if (val.contains(prefix_identifiers[6])) {
                cv.put(SensorContentContract.Sensordata.SENSORRAWTEMP, val.substring(0, val.length() - 3));
            }
        }

        Random r = new Random();
        // Add Meta Fields
        // Set Time
        cv.put(SensorContentContract.Sensordata.TIME, new Date().getTime());
        // Set Random Packet ID
        cv.put(SensorContentContract.Sensordata.PACKETID, UUID.randomUUID().toString());
        // Set GPS Data
        cv.put(SensorContentContract.Sensordata.GPSLAT, (0.0 + (3.0 - 0.0) * r.nextDouble()));
        cv.put(SensorContentContract.Sensordata.GPSLONG, (0.0 + (3.0 - 0.0) * r.nextDouble()));

        // Set Calculated Fields
        cv.put(SensorContentContract.Sensordata.SENSORCO, (0.0 + (3.0 - 0.0) * r.nextDouble()));
        cv.put(SensorContentContract.Sensordata.SENSORO3, (0.0 + (3.0 - 0.0) * r.nextDouble()));
        cv.put(SensorContentContract.Sensordata.SENSORNO2, (0.0 + (3.0 - 0.0) * r.nextDouble()));
        cv.put(SensorContentContract.Sensordata.SENSORSO2, (0.0 + (3.0 - 0.0) * r.nextDouble()));
        cv.put(SensorContentContract.Sensordata.SENSORPM, (0.0 + (3.0 - 0.0) * r.nextDouble()));


        return cv;
    }

    public String[] parseValues(String input)
    {
        return input.split(delimiter);
    }



}
