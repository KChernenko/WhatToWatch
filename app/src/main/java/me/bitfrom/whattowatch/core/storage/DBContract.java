package me.bitfrom.whattowatch.core.storage;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

import me.bitfrom.whattowatch.core.model.Film;
import me.bitfrom.whattowatch.core.model.MoviePojo;
import me.bitfrom.whattowatch.utils.ConstantsManager;

public class DBContract {
    public DBContract() {}

    public abstract static class FilmsTable {
        public static final String TABLE_NAME = "films_table";

        public static final String COLUMN_IMDB_ID = "imdb_id";
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
        public static final String COLUMN_CATEGORY = "category";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_IMDB_ID + " TEXT NOT NULL PRIMARY KEY, " +
                        COLUMN_URL_IMDB + " TEXT NOT NULL, " +
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
                        COLUMN_FAVORITE + " INTEGER, " +
                        COLUMN_CATEGORY + " INTEGER" + " );";

        public static ContentValues toContentValues(MoviePojo movie, int category) {
            ContentValues values = new ContentValues(13);
            StringBuilder directors = new StringBuilder();
            StringBuilder writers = new StringBuilder();

            values.put(COLUMN_IMDB_ID, movie.getIdIMDB());
            for (int i = 0; i < movie.getDirectors().size(); i++) {
                if (i == movie.getDirectors().size() - 1) {
                    directors.append(movie.getDirectors().get(i).getName()).append("");
                    break;
                }
                directors.append(movie.getDirectors().get(i).getName()).append(", ");
            }
            values.put(COLUMN_DIRECTORS, directors.toString());

            for (int i = 0; i < movie.getWriters().size(); i++) {
                if (i == movie.getWriters().size() - 1) {
                    writers.append(movie.getWriters().get(i).getName()).append("");
                    break;
                }
                writers.append(movie.getWriters().get(i).getName()).append(", ");
            }
            values.put(COLUMN_WRITERS, writers.toString());

            values.put(COLUMN_COUNTRIES, convertListItems(movie.getCountries()));
            values.put(COLUMN_GENRES, convertListItems(movie.getGenres()));
            values.put(COLUMN_RUNTIME, convertListItems(movie.getRuntime()));
            values.put(COLUMN_URL_IMDB, movie.getUrlIMDB());
            values.put(COLUMN_URL_POSTER, movie.getUrlPoster());
            values.put(COLUMN_PLOT, movie.getPlot());
            if (movie.getRating().equals("")) {
                values.put(COLUMN_RATING, "N/A");
            } else {
                values.put(COLUMN_RATING, movie.getRating());
            }
            values.put(COLUMN_TITLE, movie.getTitle());
            values.put(COLUMN_YEAR, movie.getYear());
            values.put(COLUMN_FAVORITE, ConstantsManager.NOT_FAVORITE);
            values.put(COLUMN_CATEGORY, category);

            return values;
        }

        public static Film parseCursor(Cursor cursor) {
            Film film = new Film();

            film.idIMDB = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMDB_ID));
            film.urlPoster = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL_POSTER));
            film.urlIMDB = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL_IMDB));
            film.countries = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRIES));
            film.directors = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIRECTORS));
            film.genres = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENRES));
            film.plot = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLOT));
            film.rating = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RATING));
            film.runtime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RUNTIME));
            film.title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
            film.writers = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WRITERS));
            film.year = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_YEAR));
            film.favorite = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FAVORITE));


            return film;
        }

        private static String convertListItems(List<String> movieItem) {
            StringBuilder result = new StringBuilder();

            for (int i = 0; i < movieItem.size(); i++) {
                if (i == movieItem.size() - 1) {
                    result.append(movieItem.get(i)).append("");
                    break;
                }
                result.append(movieItem.get(i)).append("/");
            }

            return result.toString();
        }
    }
}
