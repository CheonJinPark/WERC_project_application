package uconn.werc_project_application.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Bill on 4/6/2018.
 */

public class AqiContentContract {
    /**
     * The authority of the notes content provider - this must match the authority
     * specified in the AndroidManifest.xml provider section
     */
    public static final String AUTHORITY = "uconn.werc_project_application.provider";

    /**
     * The content URI for the top-level notes authority
     */
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    /**
     * Constants for the Notes table
     */
    public static final class Aqidata implements BaseColumns {
        /**
         * The Table Name
         */
        public static final String TABLE_NAME = "aqidata";

        public static final String PROJECTID = "projectId";

        public static final String _ID = "userId";

        public static final String TIME = "time";

        public static final String DEVICEID = "deviceId";


        public static final String PACKETID= "packetId";


        public static final String GPSLAT = "gps_lat";


        public static final String GPSLONG = "gps_long";

        public static final String AQIVAL = "aqi_val";

        public static final String AQISRC = "aqi_src";

        public static final String SENSORAQICO = "sensor_aqi_co";

        public static final String SENSORAQINO2 = "sensor_aqi_no2";

        public static final String SENSORAQIO3 = "sensor_aqi_o3";

        public static final String SENSORAQISO2 = "sensor_aqi_so2";

        public static final String SENSORAQIPM = "sensor_aqi_pm";

        public static final String SENSORAQIPML = "sensor_aqi_pml";

        public static final String SENSORPPMCO = "sensor_ppm_co";

        public static final String SENSORPPMNO2 = "sensor_ppm_no2";

        public static final String SENSORPPMO3 = "sensor_ppm_o3";

        public static final String SENSORPPMSO2 = "sensor_ppm_so2";

        public static final String SENSORUGM3PM = "sensor_ugm3_pm";

        public static final String SENSORUGM3PML = "sensor_ugm3_pml";

        public static final String SENSORVOLTCO = "sensor_volt_co";

        public static final String SENSORVOLTNO2 = "sensor_volt_no2";

        public static final String SENSORVOLTO3 = "sensor_volt_o3";

        public static final String SENSORVOLTSO2 = "sensor_volt_so2";

        public static final String SENSORLPOPM = "sensor_lpo_pm";

        public static final String SENSORLPOPML = "sensor_lpo_pml";

        public static final String DIR_BASEPATH = "aqidata";


        public static final String ITEM_BASEPATH = "aqidata/*";


        /**
         * The content URI for this table
         */
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(SensorContentContract.CONTENT_URI, TABLE_NAME);

        /**
         * The mime type of a directory of items
         */
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.uconn.werc_project_application.aqidata";

        /**
         * The mime type of a single item
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.uconn.werc_project_application.aqidata";

        /**
         * A projection of all columns in the items table
         */
        public static final String[] PROJECTION_ALL = {
                _ID,
                PROJECTID,
                TIME,
                DEVICEID,
                PACKETID,
                GPSLAT,
                GPSLONG,
                AQIVAL,
                AQISRC,
                SENSORAQICO,
                SENSORAQINO2,
                SENSORAQIO3,
                SENSORAQISO2,
                SENSORAQIPM,
                SENSORAQIPML,
                SENSORPPMCO,
                SENSORPPMNO2,
                SENSORPPMO3,
                SENSORPPMSO2,
                SENSORUGM3PM,
                SENSORUGM3PML,
                SENSORVOLTCO,
                SENSORVOLTNO2,
                SENSORVOLTO3,
                SENSORVOLTSO2,
                SENSORLPOPM,
                SENSORLPOPML
        };

        /**
         * The default sort order (SQLite syntax)
         //         */
        public static final String SORT_ORDER_DEFAULT = TIME + " ASC";

        /**
         * Build a URI for the provided note
         * @param packetId the ID of the provided packet
         * @return the URI of the provided datapoint.
         */
        public static Uri uriBuilder(String packetId) {
            Uri item = new Uri.Builder()
                    .scheme("content")
                    .authority(SensorContentContract.AUTHORITY)
                    .appendPath(SensorContentContract.Sensordata.DIR_BASEPATH)
                    .appendPath(packetId)
                    .build();
            return item;
        }

        public static Uri uriBuilder() {
            Uri item = new Uri.Builder()
                    .scheme("content")
                    .authority(SensorContentContract.AUTHORITY)
                    .appendPath(SensorContentContract.Sensordata.DIR_BASEPATH)
                    .build();
            return item;
        }
    }
}
