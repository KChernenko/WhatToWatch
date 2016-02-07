package me.bitfrom.whattowatch.data.storage;


import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

import me.bitfrom.whattowatch.data.model.FilmModel;
import me.bitfrom.whattowatch.data.model.Movie;

public class DBContract {

    public DBContract() {}

    public abstract static class FilmsTable {
        public static final String TABLE_NAME = "films_table";

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
        public static final String COLUMN_FAVORITE = "favorite";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_URL_IMDB + " TEXT NOT NULL PRIMARY KEY, " +
                        COLUMN_COUNTRIES + " TEXT NOT NULL, " +
                        COLUMN_DIRECTORS + " TEXT NOT NULL, " +
                        COLUMN_GENRES + " TEXT NOT NULL, " +
                        COLUMN_PLOT + " TEXT NOT NULL, " +
                        COLUMN_RATING + " TEXT NOT NULL, " +
                        COLUMN_RUNTIME + " TEXT NOT NULL, " +
                        COLUMN_TITLE + " TEXT NOT NULL, " +
                        COLUMN_URL_POSTER + " TEXT NOT NULL, " +
                        COLUMN_WRITERS + " TEXT NOT NULL, " +
                        COLUMN_YEAR + " TEXT NOT NULL, " +
                        COLUMN_FAVORITE + " INTEGER" + " );";


        public static ContentValues toContentValues(Movie movie) {
            ContentValues values = new ContentValues(10);
            StringBuilder directors = new StringBuilder();
            StringBuilder writers = new StringBuilder();

            for (int i = 0; i < movie.getDirectors().size(); i++) {
                directors.append(movie.getDirectors().get(i).getName());
            }
            values.put(COLUMN_DIRECTORS, directors.toString());

            for (int i = 0; i < movie.getWriters().size(); i++) {
                writers.append(movie.getWriters().get(i).getName());
            }
            values.put(COLUMN_WRITERS, writers.toString());

            values.put(COLUMN_COUNTRIES, convertListItems(movie.getCountries()));
            values.put(COLUMN_GENRES, convertListItems(movie.getGenres()));
            values.put(COLUMN_RUNTIME, convertListItems(movie.getRuntime()));
            values.put(COLUMN_URL_IMDB, movie.getUrlIMDB());
            values.put(COLUMN_PLOT, movie.getPlot());
            values.put(COLUMN_RATING, movie.getRating());
            values.put(COLUMN_TITLE, movie.getTitle());
            values.put(COLUMN_YEAR, movie.getYear());

            return values;
        }

        public static FilmModel parseCursor(Cursor cursor) {
            FilmModel filmModel = new FilmModel();

            filmModel.urlIMDB = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL_IMDB));
            filmModel.countries = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRIES));
            filmModel.directors = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIRECTORS));
            filmModel.genres = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENRES));
            filmModel.plot = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLOT));
            filmModel.rating = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RATING));
            filmModel.runtime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RUNTIME));
            filmModel.title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
            filmModel.writers = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WRITERS));
            filmModel.year = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_YEAR));

            return filmModel;
        }

        private static String convertListItems(List<String> movieItem) {
            StringBuilder result = new StringBuilder();

            for (int i = 0; i < movieItem.size(); i++) {
                result.append(movieItem.get(i));
            }

            return result.toString();
        }
    }
}
