package me.bitfrom.whattowatch.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import static me.bitfrom.whattowatch.data.MoviesContract.*;

/**
 * Created by Constantine with love.
 */
public class MoviesProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDbHelper mOpenHelper;

    static final int MOVIES = 100;
    static final int MOVIE_ID = 101;

    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        // 2) Use the addURI function to match each of the types.
        matcher.addURI(authority, PATH_MOVIES, MOVIES);
        matcher.addURI(authority, PATH_MOVIES + "/#", MOVIE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MoviesEntry.TABLE_NAME);
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
            {
                queryBuilder.appendWhere(MoviesEntry._ID + "=" + uri.getLastPathSegment());
                break;
            }
            case MOVIES:
            {
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        retCursor = queryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return MoviesEntry.CONTENT_TYPE;
            case MOVIE_ID:
                return MoviesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES: {
                normalizeDate(values);

                long _id = db.insert(MoviesEntry.TABLE_NAME, null, values);

                if (_id > 0)
                    returnUri = MoviesEntry.buildMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);

                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //Start by getting a writable database
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        //Use the uriMatcher to match the MOVIES URI's we are going to handle.
        final int match = sUriMatcher.match(uri);

        int rowsDeleted;
        //A null value deletes all rows.  In my implementation of this, I only notified
        // the uri listeners (using the content resolver) if the rowsDeleted != 0 or the selection
        // is null.
        switch (match) {
            case MOVIES:
                rowsDeleted = db.delete(MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(MoviesEntry.TABLE_NAME,
                            MoviesEntry._ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = db.delete(MoviesEntry.TABLE_NAME,
                            MoviesEntry._ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //This is a lot like the delete function.  We return the number of rows impacted
        // by the update.
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIES:
                normalizeDate(values);
                rowsUpdated = db.update(MoviesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MOVIE_ID:
                normalizeDate(values);
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(MoviesEntry.TABLE_NAME,
                            values,
                            MoviesEntry._ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = db.update(MoviesEntry.TABLE_NAME,
                            values,
                            MoviesEntry._ID + "=" + id
                            + " and "
                            + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value: values) {
                        normalizeDate(value);
                        long _id = db.insert(MoviesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

    private void normalizeDate(ContentValues values) {
        // normalize the date value
        if (values.containsKey(MoviesEntry.COLUMN_DATE)) {
            long dateValue = values.getAsLong(MoviesEntry.COLUMN_DATE);
            values.put(MoviesEntry.COLUMN_DATE, MoviesContract.normalizeDate(dateValue));
        }
    }
}
