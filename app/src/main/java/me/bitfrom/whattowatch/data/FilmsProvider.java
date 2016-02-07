package me.bitfrom.whattowatch.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import static me.bitfrom.whattowatch.data.FilmsContract.CONTENT_AUTHORITY;
import static me.bitfrom.whattowatch.data.FilmsContract.FilmsEntry;
import static me.bitfrom.whattowatch.data.FilmsContract.PATH_FILMS;

/**
 * Will be removed
 */
public class FilmsProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FilmsDbHelper mOpenHelper;

    static final int FILMS = 100;
    static final int FILMS_ID = 101;

    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        // 2) Use the addURI function to match each of the types.
        matcher.addURI(authority, PATH_FILMS, FILMS);
        matcher.addURI(authority, PATH_FILMS + "/#", FILMS_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new FilmsDbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(FilmsEntry.TABLE_NAME);
        switch (sUriMatcher.match(uri)) {
            case FILMS_ID:
            {
                queryBuilder.appendWhere(FilmsEntry._ID + "=" + uri.getLastPathSegment());
                break;
            }
            case FILMS:
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
    public String getType(@NonNull Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FILMS:
                return FilmsEntry.CONTENT_TYPE;
            case FILMS_ID:
                return FilmsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FILMS: {
                long _id = db.insert(FilmsEntry.TABLE_NAME, null, values);

                if (_id > 0)
                    returnUri = FilmsEntry.buildFilmsUri(_id);
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
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        //Start by getting a writable database
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        //Use the uriMatcher to match the FILMS URI's we are going to handle.
        final int match = sUriMatcher.match(uri);

        int rowsDeleted;
        //A null value deletes all rows.  In my implementation of this, I only notified
        // the uri listeners (using the content resolver) if the rowsDeleted != 0 or the selection
        // is null.
        switch (match) {
            case FILMS:
                rowsDeleted = db.delete(FilmsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FILMS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(FilmsEntry.TABLE_NAME,
                            FilmsEntry._ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = db.delete(FilmsEntry.TABLE_NAME,
                            FilmsEntry._ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //This is a lot like the delete function.  We return the number of rows impacted
        // by the update.
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case FILMS:
                rowsUpdated = db.update(FilmsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case FILMS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(FilmsEntry.TABLE_NAME,
                            values,
                            FilmsEntry._ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = db.update(FilmsEntry.TABLE_NAME,
                            values,
                            FilmsEntry._ID + "=" + id
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
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FILMS:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value: values) {
                        long _id = db.insert(FilmsEntry.TABLE_NAME, null, value);
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

}
