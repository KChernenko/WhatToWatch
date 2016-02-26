package me.bitfrom.whattowatch.data.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.bitfrom.whattowatch.data.model.FilmModel;
import me.bitfrom.whattowatch.data.model.Movie;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@Singleton
public class DBHelper {

    private final BriteDatabase mDb;

    @Inject
    public DBHelper(DBOpenHelper dbOpenHelper) {
        mDb = SqlBrite.create().wrapDatabaseHelper(dbOpenHelper, Schedulers.io());
    }

    public BriteDatabase getBriteDb() {
        return mDb;
    }

    public Observable<Movie> setFilms(final Collection<Movie> newMovies) {
        return Observable.create(new Observable.OnSubscribe<Movie>() {
            @Override
            public void call(Subscriber<? super Movie> subscriber) {
                if (subscriber.isUnsubscribed()) return;

                BriteDatabase.Transaction transaction = mDb.newTransaction();

                try {
                    mDb.delete(DBContract.FilmsTable.TABLE_NAME,
                            DBContract.FilmsTable.COLUMN_FAVORITE + " = ?",
                            String.valueOf(ConstantsManager.NOT_FAVORITE));
                    for (Movie movie : newMovies) {
                        long result = mDb.insert(DBContract.FilmsTable.TABLE_NAME,
                                DBContract.FilmsTable.toContentValues(movie),
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (result >= 0) subscriber.onNext(movie);
                        else Log.e(ConstantsManager.DB_LOG_TAG, "Failed to insert data: " + result);
                    }
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<List<FilmModel>> getFilms() {
        return mDb.createQuery(DBContract.FilmsTable.TABLE_NAME,
                "SELECT * FROM " + DBContract.FilmsTable.TABLE_NAME +
                " WHERE " + DBContract.FilmsTable.COLUMN_FAVORITE + " = ?",
                String.valueOf(ConstantsManager.NOT_FAVORITE))
                .mapToList(DBContract.FilmsTable::parseCursor);
    }

    public Observable<FilmModel> getTopFilmById(String filmId) {
        return mDb.createQuery(DBContract.FilmsTable.TABLE_NAME,
                "SELECT * FROM " + DBContract.FilmsTable.TABLE_NAME + " WHERE "
        + DBContract.FilmsTable.COLUMN_IMDB_ID + " = ?", filmId)
                .map(query -> {
                    Cursor cursor = query.run();
                    cursor.moveToFirst();
                    FilmModel result = DBContract.FilmsTable.parseCursor(cursor);
                    cursor.close();
                    return result;
                });
    }

    public Observable<List<FilmModel>> getFavoriteFilms() {
        return mDb.createQuery(DBContract.FilmsTable.TABLE_NAME,
                "SELECT * FROM " + DBContract.FilmsTable.TABLE_NAME + " WHERE "
                        + DBContract.FilmsTable.COLUMN_FAVORITE + " = ?",
                String.valueOf(ConstantsManager.FAVORITE))
                .mapToList(DBContract.FilmsTable::parseCursor);
    }

    public void updateFavorite(String filmId, int favorite) {
        ContentValues values = new ContentValues(1);
        BriteDatabase.Transaction transaction = mDb.newTransaction();
        int result;
        switch (favorite) {
            case ConstantsManager.FAVORITE:
                values.put(DBContract.FilmsTable.COLUMN_FAVORITE, ConstantsManager.FAVORITE);
                try {
                    result = mDb.update(DBContract.FilmsTable.TABLE_NAME,
                            values, SQLiteDatabase.CONFLICT_REPLACE,
                            DBContract.FilmsTable.COLUMN_IMDB_ID + " = ?",
                            filmId);
                    Timber.d("Added to favorites: " + result);
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                    values.clear();
                }
                break;
            case ConstantsManager.NOT_FAVORITE:
                values.put(DBContract.FilmsTable.COLUMN_FAVORITE, ConstantsManager.NOT_FAVORITE);
                try {
                    result = mDb.update(DBContract.FilmsTable.TABLE_NAME,
                            values, SQLiteDatabase.CONFLICT_REPLACE,
                            DBContract.FilmsTable.COLUMN_IMDB_ID + " = ?",
                            filmId);
                    Timber.d("Removed from favorites: " + result);
                    transaction.markSuccessful();
                } finally {
                    transaction.end();
                    values.clear();
                }
                break;
        }
    }

    public Observable<Void> clearTables() {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                if (subscriber.isUnsubscribed()) return;

                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    Cursor cursor = mDb.query("SELECT name FROM sqlite_master WHERE type='table'");
                    while (cursor.moveToNext()) {
                        mDb.delete(cursor.getString(cursor.getColumnIndex("name")), null);
                    }
                    cursor.close();
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }
}
