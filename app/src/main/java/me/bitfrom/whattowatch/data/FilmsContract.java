package me.bitfrom.whattowatch.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Constantine with love.
 */
public class FilmsContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "me.bitfrom.whattowatch";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.

    public static final String PATH_FILMS = "films";


    /* Inner class that defines the table contents of the movies table */
    public static final class FilmsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FILMS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FILMS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FILMS;

        public static final String TABLE_NAME = "films";

        public static final String COLUMN_COUNTRIES = "countries";

        public static final String COLUMN_DIRECTORS = "directors";

        public static final String COLUMN_GENRES = "genres";

        public static final String COLUMN_PLOT = "plot";

        public static final String COLUMN_RATING = "rating";

        public static final String COLUMN_RUNTIME = "runtime";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_URL_IMDB = "url_imdb";

        public static final String COLUMN_URL_POSTER = "url_poster";

        public static final String COLUMN_WRITERS = "writers";

        public static final String COLUMN_YEAR = "year";

        public static final String COLUMN_DATE = "date";


        public static Uri buildFilmsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
