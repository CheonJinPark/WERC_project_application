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
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;

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

    private SenordataDO toSenordataDO(ContentValues values) {
        final SenordataDO datapacket = new SenordataDO();
        datapacket.setUserId(AWSProvider.getInstance().getIdentityManager().getCachedUserID());
        datapacket.setDeviceId(values.getAsString(SensorContentContract.Sensordata.DEVICEID));
        datapacket.setTime(values.getAsDouble(SensorContentContract.Sensordata.TIME));
        datapacket.setGpsLat(values.getAsDouble(SensorContentContract.Sensordata.GPSLAT));
        datapacket.setGpsLong(values.getAsDouble(SensorContentContract.Sensordata.GPSLONG));
        datapacket.setPacketId(UUID.randomUUID().toString());
        datapacket.setSensorCo(values.getAsDouble(SensorContentContract.Sensordata.SENSORCO));
        datapacket.setSensorNo2(values.getAsDouble(SensorContentContract.Sensordata.SENSORNO2));
        datapacket.setSensorO3(values.getAsDouble(SensorContentContract.Sensordata.SENSORO3));
        datapacket.setSensorPm(values.getAsDouble(SensorContentContract.Sensordata.SENSORPM));
        datapacket.setSensorSo2(values.getAsDouble(SensorContentContract.Sensordata.SENSORSO2));
        datapacket.setSensorTemp(values.getAsDouble(SensorContentContract.Sensordata.SENSORTEMP));
        datapacket.setSensorRawCo(values.getAsDouble(SensorContentContract.Sensordata.SENSORRAWCO));
        datapacket.setSensorRawNo2(values.getAsDouble(SensorContentContract.Sensordata.SENSORRAWNO2));
        datapacket.setSensorRawO3(values.getAsDouble(SensorContentContract.Sensordata.SENSORRAWO3));
        datapacket.setSensorRawPm(values.getAsDouble(SensorContentContract.Sensordata.SENSORRAWPM));
        datapacket.setSensorRawSo2(values.getAsDouble(SensorContentContract.Sensordata.SENSORRAWSO2));
        datapacket.setSensorRawTemp(values.getAsDouble(SensorContentContract.Sensordata.SENSORRAWTEMP));



        return datapacket;
    }

    private Object[] fromSenordataDO(SenordataDO datapacket) {
        String[] fields = SensorContentContract.Sensordata.PROJECTION_ALL;
        Object[] r = new Object[fields.length];
        for (int i = 0 ; i < fields.length ; i++) {
            if (fields[i].equals(SensorContentContract.Sensordata.DEVICEID)) {
                r[i] = datapacket.getDeviceId();
            } else if (fields[i].equals(SensorContentContract.Sensordata._ID)) {
                r[i] = datapacket.getUserId();
            } else if (fields[i].equals(SensorContentContract.Sensordata.TIME)) {
                r[i] = datapacket.getTime();
            } else if (fields[i].equals(SensorContentContract.Sensordata.GPSLAT)) {
                r[i] = datapacket.getGpsLat();
            } else if (fields[i].equals(SensorContentContract.Sensordata.GPSLONG)) {
                r[i] = datapacket.getGpsLong();
            } else if (fields[i].equals(SensorContentContract.Sensordata.SENSORCO)) {
                r[i] = datapacket.getSensorCo();
            } else if (fields[i].equals(SensorContentContract.Sensordata.SENSORNO2)) {
                r[i] = datapacket.getSensorNo2();
            } else if (fields[i].equals(SensorContentContract.Sensordata.SENSORO3)) {
                r[i] = datapacket.getSensorO3();
            } else if (fields[i].equals(SensorContentContract.Sensordata.SENSORPM)) {
                r[i] = datapacket.getSensorPm();
            } else if (fields[i].equals(SensorContentContract.Sensordata.SENSORSO2)) {
                r[i] = datapacket.getSensorSo2();
            } else if (fields[i].equals(SensorContentContract.Sensordata.SENSORTEMP)) {
                r[i] = datapacket.getSensorTemp();
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
            } else if (fields[i].equals(SensorContentContract.Sensordata.SENSORRAWTEMP)) {
                r[i] = datapacket.getSensorRawTemp();
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
        MatrixCursor cursor = new MatrixCursor(SensorContentContract.Sensordata.PROJECTION_ALL);
        String userId = AWSProvider.getInstance().getIdentityManager().getCachedUserID();

        switch (uriType) {
            case ALL_ITEMS:
                // In this (simplified) version of a content provider, we only allow searching
                // for all records that the user owns.  The first step to this is establishing
                // a template record that has the partition key pre-populated.
                SenordataDO template = new SenordataDO();
                template.setUserId(userId);
                // Now create a query expression that is based on the template record.
                DynamoDBQueryExpression<SenordataDO> queryExpression;
                queryExpression = new DynamoDBQueryExpression<SenordataDO>()
                        .withHashKeyValues(template);
                // Finally, do the query with that query expression.
                List<SenordataDO> result = dbMapper.query(SenordataDO.class, queryExpression);
                Iterator<SenordataDO> iterator = result.iterator();
                while (iterator.hasNext()) {
                    final SenordataDO note = iterator.next();
                    Object[] columnValues = fromSenordataDO(note);
                    cursor.addRow(columnValues);
                }

                break;
            case ONE_ITEM:
                // In this (simplified) version of a content provider, we only allow searching
                // for the specific record that was requested
                final SenordataDO note = dbMapper.load(SenordataDO.class, userId, uri.getLastPathSegment());
                if (note != null) {
                    Object[] columnValues = fromSenordataDO(note);
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
        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case ALL_ITEMS:
                DynamoDBMapper dbMapper = AWSProvider.getInstance().getDynamoDBMapper();
                final SenordataDO newdata = toSenordataDO(values);
                dbMapper.save(newdata);
                Uri item = new Uri.Builder()
                        .appendPath(SensorContentContract.CONTENT_URI.toString())
                        .appendPath(newdata.getPacketId())
                        .build();
//                notifyAllListeners(item);
                return item;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);
        int rows;

        switch (uriType) {
            case ONE_ITEM:
                DynamoDBMapper dbMapper = AWSProvider.getInstance().getDynamoDBMapper();
                final SenordataDO sensordata = new SenordataDO();
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
                final SenordataDO updatedData = toSenordataDO(values);
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
