package me.bitfrom.whattowatch.domain.weapons;

import android.content.ContentValues;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import me.bitfrom.whattowatch.WWApplication;
import me.bitfrom.whattowatch.data.FilmsContract;
import me.bitfrom.whattowatch.domain.contracts.ImageDownloadInteractor;
import me.bitfrom.whattowatch.rest.model.Film;

/**
 * Created by Constantine with love.
 */
public class SaveDataWeapon {

    public static void saveData(List<Film> films) {

        Vector<ContentValues> cVVector = new Vector<>(films.size());
        ImageDownloadInteractor imageWeapon = new ImageDownloadWeapon();

        GregorianCalendar calendar = new GregorianCalendar();
        long dateTime = calendar.getTimeInMillis();

        final int notFavorite = 0;

        for (Film film: films) {
            ContentValues movieValues = new ContentValues();

            movieValues.put(FilmsContract.FilmsEntry.COLUMN_TITLE, film.getTitle());
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_DIRECTORS, film.getDirectors().get(0).getName());
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_GENRES, film.getGenres().toString());
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_WRITERS, film.getWriters().get(0).getName());
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_COUNTRIES, film.getCountries().get(0));
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_YEAR, film.getYear());
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_RUNTIME, film.getRuntime().get(0));
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_URL_POSTER, film.getUrlPoster());
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_RATING, film.getRating());
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_PLOT, film.getPlot());
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_URL_IMDB, film.getUrlIMDB());
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_DATE, dateTime);
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_FAVORITE, notFavorite);

            imageWeapon.loadPoster(WWApplication.getAppContext(), film.getUrlPoster(), null, ImageDownloadWeapon.FLAG.LOAD);

            cVVector.add(movieValues);
        }

        //add to database
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            WWApplication.getAppContext().getContentResolver()
                    .bulkInsert(FilmsContract.FilmsEntry.CONTENT_URI, cvArray);

            // delete old data so we don't build up an endless history
            WWApplication.getAppContext().getContentResolver().delete(FilmsContract.FilmsEntry.CONTENT_URI,
                    FilmsContract.FilmsEntry.COLUMN_DATE + " < ?" + " AND "
                            + FilmsContract.FilmsEntry.COLUMN_FAVORITE + " = ?",
                    new String[] {Long.toString(dateTime), Integer.toString(notFavorite)});

            NotificationWeapon.updateNotifications(WWApplication.getAppContext());
        }
    }
}
