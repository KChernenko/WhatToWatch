package me.bitfrom.whattowatch.domain.weapons;

import android.content.ContentValues;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import me.bitfrom.whattowatch.WWApplication;
import me.bitfrom.whattowatch.data.FilmsContract;
import me.bitfrom.whattowatch.domain.contracts.FavoriteConstants;
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

        final int filmsSize = films.size();
        for (int i = 0; i < filmsSize; i++) {
            ContentValues movieValues = new ContentValues();

            movieValues.put(FilmsContract.FilmsEntry.COLUMN_TITLE, films.get(i).getTitle());
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_DIRECTORS, films.get(i).getDirectors().get(0).getName());
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_GENRES, films.get(i).getGenres().toString());
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_WRITERS, films.get(i).getWriters().get(0).getName());
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_COUNTRIES, films.get(i).getCountries().get(0));
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_YEAR, films.get(i).getYear());
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_RUNTIME, films.get(i).getRuntime().get(0));
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_URL_POSTER, films.get(i).getUrlPoster());
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_RATING, films.get(i).getRating());
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_PLOT, films.get(i).getPlot());
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_URL_IMDB, films.get(i).getUrlIMDB());
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_DATE, dateTime);
            movieValues.put(FilmsContract.FilmsEntry.COLUMN_FAVORITE, FavoriteConstants.NOT_FAVORITE);

            imageWeapon.loadPoster(ImageDownloadWeapon.FLAG.LOAD, films.get(i).getUrlPoster(), null);

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
                    new String[] {Long.toString(dateTime), Integer.toString(FavoriteConstants.NOT_FAVORITE)});

            NotificationWeapon.updateNotifications(WWApplication.getAppContext());
        }
    }
}
