package uconn.werc_project_application.data;

/**
 * Created by Bill on 2/23/2018.
 */

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Per the official Android documentation, this class defines all publically available
 * elements, like the authority, the content URIs, columns, and content types for each
 * element
 */
public class SensorContentContract {
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
    public static final class Sensordata implements BaseColumns {
        /**
         * The Table Name
         */
        public static final String TABLE_NAME = "sensordata";

        public static final String PROJECTID = "projectId";

        public static final String _ID = "userId";

        public static final String TIME = "time";

        public static final String DEVICEID = "deviceId";


        public static final String PACKETID= "packetId";


        public static final String GPSLAT = "gps_lat";


        public static final String GPSLONG = "gps_long";

        public static final String SENSORRAWCO = "sensor_raw_co";

        public static final String SENSORRAWNO2 = "sensor_raw_no2";

        public static final String SENSORRAWO3 = "sensor_raw_o3";

        public static final String SENSORRAWPM = "sensor_raw_pm";

        public static final String SENSORRAWPML = "sensor_raw_pml";

        public static final String SENSORRAWSO2 = "sensor_raw_so2";

        public static final String DIR_BASEPATH = "sensordata";


        public static final String ITEM_BASEPATH = "sensordata/*";


        /**
         * The content URI for this table
         */
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(SensorContentContract.CONTENT_URI, TABLE_NAME);

        /**
         * The mime type of a directory of items
         */
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.uconn.werc_project_application.sensordata";

        /**
         * The mime type of a single item
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.uconn.werc_project_application.sensordata";

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
                SENSORRAWCO,
                SENSORRAWNO2,
                SENSORRAWO3,
                SENSORRAWPM,
                SENSORRAWPML,
                SENSORRAWSO2
        };

        /**
         * The default sort order (SQLite syntax)
         //         */
        public static final String SORT_ORDER_DEFAULT = TIME + " ASC";

        /**
         * Build a URI for the provided note
         * @param packetId the ID of the provided note
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
