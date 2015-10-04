package me.bitfrom.whattowatch.domain.weapons;

import android.content.ContentValues;
import android.util.Log;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import me.bitfrom.whattowatch.WWApplication;
import me.bitfrom.whattowatch.data.MoviesContract;
import me.bitfrom.whattowatch.domain.contracts.ImageDownloadInteractor;
import me.bitfrom.whattowatch.rest.model.Movie;

/**
 * Created by Constantine with love.
 */
public class SaveDataWeapon {

    public static void saveData(List<Movie> movies) {

        Vector<ContentValues> cVVector = new Vector<>(movies.size());
        ImageDownloadInteractor imageWeapon = new ImageDownloadWeapon();

        GregorianCalendar calendar = new GregorianCalendar();
        long dateTime = calendar.getTimeInMillis();

        for (Movie movie: movies) {
            ContentValues movieValues = new ContentValues();

            movieValues.put(MoviesContract.MoviesEntry.COLUMN_TITLE, movie.getTitle());
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_DIRECTORS, movie.getDirectors().get(0).getName());
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_GENRES, movie.getGenres().toString());
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_WRITERS, movie.getWriters().get(0).getName());
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_COUNTRIES, movie.getCountries().get(0));
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_YEAR, movie.getYear());
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_RUNTIME, movie.getRuntime().get(0));
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_URL_POSTER, movie.getUrlPoster());
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_RATING, movie.getRating());
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_PLOT, movie.getPlot());
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_URL_IMDB, movie.getUrlIMDB());
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_DATE, dateTime);

            imageWeapon.loadPoster(WWApplication.getAppContext(), movie.getUrlPoster(), null, ImageDownloadWeapon.FLAG.LOAD);

            cVVector.add(movieValues);
        }

        //add to database
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            WWApplication.getAppContext().getContentResolver()
                    .bulkInsert(MoviesContract.MoviesEntry.CONTENT_URI, cvArray);

            // delete old data so we don't build up an endless history
            WWApplication.getAppContext().getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI,
                    MoviesContract.MoviesEntry.COLUMN_DATE + " < ?",
                    new String[] {Long.toString(dateTime)});

            NotificationWeapon.updateNotifications(WWApplication.getAppContext());
        }
    }
}
