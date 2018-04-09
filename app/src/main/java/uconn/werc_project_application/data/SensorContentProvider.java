package uconn.werc_project_application.data;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.hardware.Sensor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import uconn.werc_project_application.AWSProvider;


/**
 * The Content Provider for the internal Sensordata database
 */
public class SensorContentProvider extends ContentProvider {
    /**
     * Creates a UriMatcher for matching the path elements for this content provider
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /**
     * The code for the UriMatch matching all Sensordata
     */
    private static final int ALL_ITEMS = 10;

    /**
     * The code for the UriMatch matching a single datapacket
     */
    private static final int ONE_ITEM = 20;

    /*
     * Initialize the UriMatcher with the URIs that this content provider handles
     */
    static {
        sUriMatcher.addURI(
                SensorContentContract.AUTHORITY,
                SensorContentContract.Sensordata.DIR_BASEPATH,
                ALL_ITEMS);
        sUriMatcher.addURI(
                SensorContentContract.AUTHORITY,
                SensorContentContract.Sensordata.ITEM_BASEPATH,
                ONE_ITEM);
    }

    /**
     * Part of the Content Provider interface.  The system calls onCreate() when it starts up
     * the provider.  You should only perform fast-running initialization tasks in this method.
     * Defer database creation and data loading until the provider actually receives a request
     * for the data.  This runs on the UI thread.
     *
     * @return true if the provider was successfully loaded; false otherwise
     */
    @Override
    public boolean onCreate() {
        return true;
    }


    private AqidataDO toAqidataDO(ContentValues values) {
        final AqidataDO datapacket = new AqidataDO();
        datapacket.setUserId(AWSProvider.getInstance().getIdentityManager().getCachedUserID());
        datapacket.setProjectId(values.getAsString(AqiContentContract.Aqidata.PROJECTID));
        datapacket.setDeviceId(values.getAsString(AqiContentContract.Aqidata.DEVICEID));
        datapacket.setTime(values.getAsDouble(AqiContentContract.Aqidata.TIME));
        datapacket.setGpsLat(values.getAsDouble(AqiContentContract.Aqidata.GPSLAT));
        datapacket.setGpsLong(values.getAsDouble(AqiContentContract.Aqidata.GPSLONG));
        datapacket.setPacketId(values.getAsString(AqiContentContract.Aqidata.PACKETID));
        datapacket.setSensorAqiCo(values.getAsDouble(AqiContentContract.Aqidata.SENSORAQICO));
        datapacket.setSensorAqiNo2(values.getAsDouble(AqiContentContract.Aqidata.SENSORAQINO2));
        datapacket.setSensorAqiO3(values.getAsDouble(AqiContentContract.Aqidata.SENSORAQIO3));
        datapacket.setSensorAqiSo2(values.getAsDouble(AqiContentContract.Aqidata.SENSORAQISO2));
        datapacket.setSensorAqiPm(values.getAsDouble(AqiContentContract.Aqidata.SENSORAQIPM));
        datapacket.setSensorAqiPml(values.getAsDouble(AqiContentContract.Aqidata.SENSORAQIPML));
        datapacket.setSensorPpmCo(values.getAsDouble(AqiContentContract.Aqidata.SENSORPPMCO));
        datapacket.setSensorPpmNo2(values.getAsDouble(AqiContentContract.Aqidata.SENSORPPMNO2));
        datapacket.setSensorPpmO3(values.getAsDouble(AqiContentContract.Aqidata.SENSORPPMO3));
        datapacket.setSensorPpmSo2(values.getAsDouble(AqiContentContract.Aqidata.SENSORPPMSO2));
        datapacket.setSensorUgm3Pm(values.getAsDouble(AqiContentContract.Aqidata.SENSORUGM3PM));
        datapacket.setSensorUgm3Pml(values.getAsDouble(AqiContentContract.Aqidata.SENSORUGM3PML));
        datapacket.setSensorVoltCo(values.getAsDouble(AqiContentContract.Aqidata.SENSORVOLTCO));
        datapacket.setSensorVoltNo2(values.getAsDouble(AqiContentContract.Aqidata.SENSORVOLTNO2));
        datapacket.setSensorVoltO3(values.getAsDouble(AqiContentContract.Aqidata.SENSORVOLTO3));
        datapacket.setSensorVoltSo2(values.getAsDouble(AqiContentContract.Aqidata.SENSORVOLTSO2));
        datapacket.setSensorLpoPm(values.getAsDouble(AqiContentContract.Aqidata.SENSORLPOPM));
        datapacket.setSensorLpoPml(values.getAsDouble(AqiContentContract.Aqidata.SENSORLPOPML));
        datapacket.setAqiSrc(values.getAsString(AqiContentContract.Aqidata.AQISRC));
        datapacket.setAqiVal(values.getAsDouble(AqiContentContract.Aqidata.AQIVAL));





        return datapacket;
    }

    private Object[] fromAqidataDO(AqidataDO datapacket) {
        String[] fields = AqiContentContract.Aqidata.PROJECTION_ALL;
        Object[] r = new Object[fields.length];
        for (int i = 0 ; i < fields.length ; i++) {
            if (fields[i].equals(AqiContentContract.Aqidata.DEVICEID)) {
                r[i] = datapacket.getDeviceId();
            } else if (fields[i].equals(AqiContentContract.Aqidata._ID)) {
                r[i] = datapacket.getUserId();
            } else if (fields[i].equals(AqiContentContract.Aqidata.PROJECTID)) {
                r[i] = datapacket.getProjectId();
            } else if (fields[i].equals(AqiContentContract.Aqidata.TIME)) {
                r[i] = datapacket.getTime();
            } else if (fields[i].equals(AqiContentContract.Aqidata.PACKETID)) {
                r[i] = datapacket.getPacketId();
            } else if (fields[i].equals(AqiContentContract.Aqidata.GPSLAT)) {
                r[i] = datapacket.getGpsLat();
            } else if (fields[i].equals(AqiContentContract.Aqidata.GPSLONG)) {
                r[i] = datapacket.getGpsLong();
            } else if (fields[i].equals(AqiContentContract.Aqidata.SENSORAQICO)) {
                r[i] = datapacket.getSensorAqiCo();
            } else if (fields[i].equals(AqiContentContract.Aqidata.SENSORAQINO2)) {
                r[i] = datapacket.getSensorAqiNo2();
            } else if (fields[i].equals(AqiContentContract.Aqidata.SENSORAQIO3)) {
                r[i] = datapacket.getSensorAqiO3();
            } else if (fields[i].equals(AqiContentContract.Aqidata.SENSORAQISO2)) {
                r[i] = datapacket.getSensorAqiSo2();
            } else if (fields[i].equals(AqiContentContract.Aqidata.SENSORAQIPM)) {
                r[i] = datapacket.getSensorAqiPm();
            } else if (fields[i].equals(AqiContentContract.Aqidata.SENSORAQIPML)) {
                r[i] = datapacket.getSensorAqiPml();
            } else if (fields[i].equals(AqiContentContract.Aqidata.AQIVAL)) {
                r[i] = datapacket.getAqiVal();
            } else if (fields[i].equals(AqiContentContract.Aqidata.AQISRC)) {
                r[i] = datapacket.getAqiSrc();
            } else {
                r[i] = new Integer(0);
            }
        }
        return r;
    }


    private SensordataDO toSensordataDO(ContentValues values) {
        final SensordataDO datapacket = new SensordataDO();
        datapacket.setUserId(AWSProvider.getInstance().getIdentityManager().getCachedUserID());
        datapacket.setProjectId(values.getAsString(SensorContentContract.Sensordata.PROJECTID));
        datapacket.setDeviceId(values.getAsString(SensorContentContract.Sensordata.DEVICEID));
        datapacket.setTime(values.getAsDouble(SensorContentContract.Sensordata.TIME));
        datapacket.setGpsLat(values.getAsDouble(SensorContentContract.Sensordata.GPSLAT));
        datapacket.setGpsLong(values.getAsDouble(SensorContentContract.Sensordata.GPSLONG));
        datapacket.setSensorRawCo(values.getAsDouble(SensorContentContract.Sensordata.SENSORRAWCO));
        datapacket.setSensorRawNo2(values.getAsDouble(SensorContentContract.Sensordata.SENSORRAWNO2));
        datapacket.setSensorRawO3(values.getAsDouble(SensorContentContract.Sensordata.SENSORRAWO3));
        datapacket.setSensorRawPm(values.getAsDouble(SensorContentContract.Sensordata.SENSORRAWPM));
        datapacket.setSensorRawSo2(values.getAsDouble(SensorContentContract.Sensordata.SENSORRAWSO2));
        datapacket.setSensorRawPml(values.getAsDouble(SensorContentContract.Sensordata.SENSORRAWPML));

        return datapacket;
    }

    private Object[] fromSensordataDO(SensordataDO datapacket) {
        String[] fields = SensorContentContract.Sensordata.PROJECTION_ALL;
        Object[] r = new Object[fields.length];
        for (int i = 0 ; i < fields.length ; i++) {
            if (fields[i].equals(SensorContentContract.Sensordata.DEVICEID)) {
                r[i] = datapacket.getDeviceId();
            } else if (fields[i].equals(SensorContentContract.Sensordata._ID)) {
                r[i] = datapacket.getUserId();
            } else if (fields[i].equals(SensorContentContract.Sensordata.PROJECTID)) {
                r[i] = datapacket.getProjectId();
            } else if (fields[i].equals(SensorContentContract.Sensordata.TIME)) {
                r[i] = datapacket.getTime();
            } else if (fields[i].equals(SensorContentContract.Sensordata.PACKETID)) {
                r[i] = datapacket.getPacketId();
            } else if (fields[i].equals(SensorContentContract.Sensordata.GPSLAT)) {
                r[i] = datapacket.getGpsLat();
            } else if (fields[i].equals(SensorContentContract.Sensordata.GPSLONG)) {
                r[i] = datapacket.getGpsLong();
            } else if (fields[i].equals(SensorContentContract.Sensordata.SENSORRAWCO)) {
                r[i] = datapacket.getSensorRawCo();
            } else if (fields[i].equals(SensorContentContract.Sensordata.SENSORRAWNO2)) {
                r[i] = datapacket.getSensorRawNo2();
            } else if (fields[i].equals(SensorContentContract.Sensordata.SENSORRAWO3)) {
                r[i] = datapacket.getSensorRawO3();
            } else if (fields[i].equals(SensorContentContract.Sensordata.SENSORRAWPM)) {
                r[i] = datapacket.getSensorRawPm();
            } else if (fields[i].equals(SensorContentContract.Sensordata.SENSORRAWSO2)) {
                r[i] = datapacket.getSensorRawSo2();
            } else if (fields[i].equals(SensorContentContract.Sensordata.SENSORRAWPML)) {
                r[i] = datapacket.getSensorRawPml();
            } else {
                r[i] = new Integer(0);
            }
        }
        return r;
    }



    /**
     * Query for a (number of) records.
     *
     * @param uri The URI to query
     * @param projection The fields to return
     * @param selection The WHERE clause
     * @param selectionArgs Any arguments to the WHERE clause
     * @param sortOrder the sort order for the returned records
     * @return a Cursor that can iterate over the results
     */
    @Nullable
    @Override
    public Cursor query(
            @NonNull Uri uri,
            @Nullable String[] projection,
            @Nullable String selection,
            @Nullable String[] selectionArgs,
            @Nullable String sortOrder) {
        int uriType = sUriMatcher.match(uri);

        DynamoDBMapper dbMapper = AWSProvider.getInstance().getDynamoDBMapper();
        MatrixCursor cursor = new MatrixCursor(AqiContentContract.Aqidata.PROJECTION_ALL);
        String userId = AWSProvider.getInstance().getIdentityManager().getCachedUserID();

        String projectId = "werc";
        switch (uriType) {
            case ALL_ITEMS:
                // In this (simplified) version of a content provider, we only allow searching
                // for all records that the user owns.  The first step to this is establishing
                // a template record that has the partition key pre-populated.
                AqidataDO template = new AqidataDO();
                template.setProjectId(projectId);
                // Now create a query expression that is based on the template record.
                DynamoDBQueryExpression<AqidataDO> queryExpression;
                queryExpression = new DynamoDBQueryExpression<AqidataDO>()
                        .withHashKeyValues(template);
                // Finally, do the query with that query expression.
                List<AqidataDO> result = dbMapper.query(AqidataDO.class, queryExpression);
                Log.d("SensorContentProvider", "Number of entries returned: " + Integer.toString(result.size()));
                Iterator<AqidataDO> iterator = result.iterator();
                while (iterator.hasNext()) {
                    final AqidataDO note = iterator.next();
                    Log.d("SensorContentProvider", "PacketId: " + note.getPacketId());
                    Object[] columnValues = fromAqidataDO(note);
                    cursor.addRow(columnValues);
                }

                break;
            case ONE_ITEM:
                // In this (simplified) version of a content provider, we only allow searching
                // for the specific record that was requested
                final SensordataDO note = dbMapper.load(SensordataDO.class, userId, uri.getLastPathSegment());
                if (note != null) {
                    Object[] columnValues = fromSensordataDO(note);
                    cursor.addRow(columnValues);
                }
                break;
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }
    /**
     * The content provider must return the content type for its supported URIs.  The supported
     * URIs are defined in the UriMatcher and the types are stored in the SensorContentContract.
     *
     * @param uri the URI for typing
     * @return the type of the URI
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case ALL_ITEMS:
                return SensorContentContract.Sensordata.CONTENT_DIR_TYPE;
            case ONE_ITEM:
                return SensorContentContract.Sensordata.CONTENT_ITEM_TYPE;
            default:
                return null;
        }
    }

    /**
     * Insert a new record into the database.
     *
     * @param uri the base URI to insert at (must be a directory-based URI)
     * @param values the values to be inserted
     * @return the URI of the inserted item
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        DynamoDBMapper dbMapper = AWSProvider.getInstance().getDynamoDBMapper();
        Log.d("SensorContentProvider", "Uri: " + uri.getPath());
        if (uri.getPath().contains("aqi"))
        {
            final AqidataDO newdata = toAqidataDO(values);
            dbMapper.save(newdata);
            Uri item = new Uri.Builder()
                    .appendPath(SensorContentContract.CONTENT_URI.toString())
                    .appendPath(newdata.getPacketId())
                    .build();
            return item;
        } else {
            final SensordataDO newdata = toSensordataDO(values);
            dbMapper.save(newdata);
            Uri item = new Uri.Builder()
                    .appendPath(SensorContentContract.CONTENT_URI.toString())
                    .appendPath(newdata.getPacketId())
                    .build();
            return item;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);
        int rows;

        switch (uriType) {
            case ONE_ITEM:
                DynamoDBMapper dbMapper = AWSProvider.getInstance().getDynamoDBMapper();
                final SensordataDO sensordata = new SensordataDO();
                sensordata.setPacketId(uri.getLastPathSegment());
                sensordata.setUserId(AWSProvider.getInstance().getIdentityManager().getCachedUserID());
                dbMapper.delete(sensordata);
                rows = 1;
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        if (rows > 0) {
            notifyAllListeners(uri);
        }
        return rows;
    }
    /**
     * Part of the ContentProvider implementation.  Updates the record (based on the record URI)
     * with the specified ContentValues
     *
     * @param uri The URI of the record(s)
     * @param values The new values for the record(s)
     * @param selection If the URI is a directory, the WHERE clause
     * @param selectionArgs Arguments for the WHERE clause
     * @return the number of rows updated
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);
        int rows;

        switch (uriType) {
            case ONE_ITEM:
                DynamoDBMapper dbMapper = AWSProvider.getInstance().getDynamoDBMapper();
                final SensordataDO updatedData = toSensordataDO(values);
                dbMapper.save(updatedData);
                rows = 1;
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        if (rows > 0) {
            notifyAllListeners(uri);
        }
        return rows;
    }
    /**
     * Notify all listeners that the specified URI has changed
     * @param uri the URI that changed
     */
    private void notifyAllListeners(Uri uri) {
        ContentResolver resolver = getContext().getContentResolver();
        if (resolver != null) {
            resolver.notifyChange(uri, null);
        }
    }

    private String getOneItemClause(String id) {
        return String.format("%s = \"%s\"", SensorContentContract.Sensordata._ID, id);
    }
}
