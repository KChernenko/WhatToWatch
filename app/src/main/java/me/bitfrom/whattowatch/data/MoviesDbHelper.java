package me.bitfrom.whattowatch.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static me.bitfrom.whattowatch.data.MoviesContract.*;

/**
 * Created by Constantine with love.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DABASE_NAME = "movies.db";

    public MoviesDbHelper(Context context) {
        super(context, DABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +
                MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_DIRECTORS + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_GENRES + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_WRITERS + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_COUNTRIES + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_YEAR + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_RUNTIME + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_URL_POSTER + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_RATING + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_PLOT + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_URL_IMDB + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_DATE + " TEXT NOT NULL" +
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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}