package uconn.werc_project_application.data;

/**
 * Created by Bill on 2/23/2018.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Android has a base class for dealing with SQLite databases called SQLiteOpenHelper.
 * This is a derived class.  Its main purpose is to set up the database on first access.
 */
class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "sensordata.db";
    private static final int DBVERSION = 1;

    /**
     * Create a new SQLiteOpenHelper object for this database.
     * @param context the application context
     */
    DatabaseHelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    /**
     * Called when the database needs to be created
     * @param db the database handle
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SensorContentProvider.Notes.CREATE_SQLITE_TABLE);
    }

    /**
     * Called when the database needs to be updated
     * @param db the database handle
     * @param oldVersion the old database version
     * @param newVersion the new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Don't do anything here - we don't support upgrade yet.
    }
}
