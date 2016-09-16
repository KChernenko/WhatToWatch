package me.bitfrom.whattowatch.core.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import me.bitfrom.whattowatch.core.storage.entities.FilmEntity;
import me.bitfrom.whattowatch.injection.ApplicationContext;
import me.bitfrom.whattowatch.utils.ConstantsManager;

public class DbOpenHelper extends SQLiteOpenHelper {

    @Inject
    public DbOpenHelper(@NonNull @ApplicationContext Context context) {
        super(context, ConstantsManager.DATABASE_NAME, null, ConstantsManager.DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(FilmEntity.CREATE_TABLE);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FilmEntity.TABLE_NAME);
        onCreate(db);
    }
}