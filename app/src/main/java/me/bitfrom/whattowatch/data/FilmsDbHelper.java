package me.bitfrom.whattowatch.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static me.bitfrom.whattowatch.data.FilmsContract.FilmsEntry;

/**
 * Created by Constantine with love.
 */
public class FilmsDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "films.db";

    public FilmsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + FilmsEntry.TABLE_NAME + " (" +
                FilmsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                FilmsEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FilmsEntry.COLUMN_DIRECTORS + " TEXT NOT NULL, " +
                FilmsEntry.COLUMN_GENRES + " TEXT NOT NULL, " +
                FilmsEntry.COLUMN_WRITERS + " TEXT NOT NULL, " +
                FilmsEntry.COLUMN_COUNTRIES + " TEXT NOT NULL, " +
                FilmsEntry.COLUMN_YEAR + " TEXT NOT NULL, " +
                FilmsEntry.COLUMN_RUNTIME + " TEXT NOT NULL, " +
                FilmsEntry.COLUMN_URL_POSTER + " TEXT NOT NULL, " +
                FilmsEntry.COLUMN_RATING + " TEXT NOT NULL, " +
                FilmsEntry.COLUMN_PLOT + " TEXT NOT NULL, " +
                FilmsEntry.COLUMN_URL_IMDB + " TEXT NOT NULL, " +
                FilmsEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                FilmsEntry.COLUMN_FAVORITE + " INTEGER NOT NULL" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FilmsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}