package me.bitfrom.whattowatch.domain.weapons;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import de.greenrobot.event.EventBus;
import me.bitfrom.whattowatch.R;
import me.bitfrom.whattowatch.data.MoviesContract;
import me.bitfrom.whattowatch.domain.contracts.ImageDownloadInteractor;
import me.bitfrom.whattowatch.domain.contracts.SaveDataInteractor;
import me.bitfrom.whattowatch.domain.weapons.network.ImageDownloadWeapon;
import me.bitfrom.whattowatch.domain.weapons.network.LoadDataWeapon;
import me.bitfrom.whattowatch.rest.model.Movie;
import me.bitfrom.whattowatch.utils.ServerMessageEvent;
import me.bitfrom.whattowatch.utils.Utility;

/**
 * Created by Constantine with love.
 */
public class SaveDataWeapon implements SaveDataInteractor {

    private static final String LOG_TAG = SaveDataWeapon.class.getSimpleName();

    private static List<Movie> moviesList = new ArrayList<>();

    private ImageDownloadInteractor imageWeapon;

    public SaveDataWeapon() {
        imageWeapon = new ImageDownloadWeapon();
    }

    public void saveData(Context context) {
        int numberOfMovies = Utility.getPreferredNumbersOfMovies(context);

        if (moviesList.isEmpty()) {

            moviesList = LoadDataWeapon.loadMovies(context);

            if (moviesList.size() == 0) {
                String message = context.getString(R.string.server_isnt_available);
                EventBus.getDefault().post(new ServerMessageEvent(message));
            } else if (moviesList.size() == numberOfMovies) {
                Vector<ContentValues> cVVector = new Vector<>(numberOfMovies);

                GregorianCalendar calendar = new GregorianCalendar();
                long dateTime = calendar.getTimeInMillis();

                for (Movie movie: moviesList) {
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

                    imageWeapon.loadPoster(context, movie.getUrlPoster(), null, ImageDownloadWeapon.FLAG.LOAD);

                    cVVector.add(movieValues);
                }

                //add to database
                if (cVVector.size() > 0) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    int inserted = context.getContentResolver()
                            .bulkInsert(MoviesContract.MoviesEntry.CONTENT_URI, cvArray);
                    Log.d(LOG_TAG, "Inserted: " + inserted);

                    // delete old data so we don't build up an endless history
                    int deleted = context.getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI,
                            MoviesContract.MoviesEntry.COLUMN_DATE + " < ?",
                            new String[] {Long.toString(dateTime)});
                    Log.d(LOG_TAG, "Deleted: " + deleted);

                    NotificationWeapon.updateNotifications(context);
                }
            }
        }
    }
}
