package me.bitfrom.whattowatch.data.storage;

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
import rx.functions.Func1;

@Singleton
public class DBHelper {

    private final BriteDatabase mDb;

    @Inject
    public DBHelper(DBOpenHelper dbOpenHelper) {
        mDb = SqlBrite.create().wrapDatabaseHelper(dbOpenHelper);
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
                    mDb.delete(DBContract.FilmsTable.TABLE_NAME, null);
                    for (Movie movie: newMovies) {
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
                "SELECT * FROM " + DBContract.FilmsTable.TABLE_NAME)
                .mapToList(new Func1<Cursor, FilmModel>() {
                    @Override
                    public FilmModel call(Cursor cursor) {
                        return DBContract.FilmsTable.parseCursor(cursor);
                    }
                });
    }

    public Observable<FilmModel> getTopFilmById(String imdbId) {
        return mDb.createQuery(DBContract.FilmsTable.TABLE_NAME,
                "SELECT * FROM " + DBContract.FilmsTable.TABLE_NAME + " WHERE "
        + DBContract.FilmsTable.COLUMN_IMDB_ID + " = ?", imdbId)
                .map(new Func1<SqlBrite.Query, FilmModel>() {
                    @Override
                    public FilmModel call(SqlBrite.Query query) {
                        return DBContract.FilmsTable.parseCursor(query.run());
                    }
                });
    }

    public Observable<List<FilmModel>> getFavoriteFilms() {
        return mDb.createQuery(DBContract.FilmsTable.TABLE_NAME,
                "SELECT * FROM " + DBContract.FilmsTable.TABLE_NAME + " WHERE "
                        + DBContract.FilmsTable.COLUMN_FAVORITE + " = ?",
                String.valueOf(ConstantsManager.FAVORITE))
        .mapToList(new Func1<Cursor, FilmModel>() {
            @Override
            public FilmModel call(Cursor cursor) {
                return DBContract.FilmsTable.parseCursor(cursor);
            }
        });
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
