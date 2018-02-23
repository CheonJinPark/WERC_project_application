package uconn.werc_project_application.data;

/**
 * Created by Bill on 2/23/2018.
 */

/**
 * The authority of the notes content provider - this must match the authority
 * specified in the AndroidManifest.xml provider section
 */

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

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
    public static final String TABLE_NAME = "gpsdata";

    /**
     * The internal ID
     */
    public static final String _USERID = "userId";

    /**
     * The noteId field
     */
    public static final String TIMEEPOCH = "time_epoch";

    /**
     * The title field
     */
    public static final String DEVICEID = "device_id";

    /**
     * The content field
     */
    public static final String GPSLAT = "gps_lat";

    /**
     * The created field
     */
    public static final String GPSLONG = "gps_long";

    /**
     * The updated field
     */
    public static final String UPDATED = "updated";

    /**
     * The directory base-path
     */
    public static final String DIR_BASEPATH = "notes";

    /**
     * The items base-path
     */
    public static final String ITEM_BASEPATH = "notes/*";

    /**
     * The SQLite database command to create the table
     */
    public static final String CREATE_SQLITE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + _ID + " INTEGER PRIMARY KEY, "
                    + NOTEID + " TEXT UNIQUE NOT NULL, "
                    + TITLE + " TEXT NOT NULL DEFAULT '', "
                    + CONTENT + " TEXT NOT NULL DEFAULT '', "
                    + CREATED + " BIGINT NOT NULL DEFAULT 0, "
                    + UPDATED + " BIGINT NOT NULL DEFAULT 0)";

    /**
     * The content URI for this table
     */
    public static final Uri CONTENT_URI =
            Uri.withAppendedPath(NotesContentContract.CONTENT_URI, TABLE_NAME);

    /**
     * The mime type of a directory of items
     */
    public static final String CONTENT_DIR_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.com.amazonaws.mobile.samples.notes";

    /**
     * The mime type of a single item
     */
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.com.amazonaws.mobile.samples.notes";

    /**
     * A projection of all columns in the items table
     */
    public static final String[] PROJECTION_ALL = {
            _ID,
            NOTEID,
            TITLE,
            CONTENT,
            CREATED,
            UPDATED
    };

    /**
     * The default sort order (SQLite syntax)
     */
    public static final String SORT_ORDER_DEFAULT = CREATED + " ASC";

    /**
     * Build a URI for the provided note
     * @param noteId the ID of the provided note
     * @return the URI of the provided note
     */
    public static Uri uriBuilder(String noteId) {
        Uri item = new Uri.Builder()
                .scheme("content")
                .authority(NotesContentContract.AUTHORITY)
                .appendPath(NotesContentContract.Notes.DIR_BASEPATH)
                .appendPath(noteId)
                .build();
        return item;
    }
}
}
