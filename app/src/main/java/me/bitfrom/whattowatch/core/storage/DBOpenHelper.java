package me.bitfrom.whattowatch.core.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Inject;

import me.bitfrom.whattowatch.injection.ApplicationContext;
import me.bitfrom.whattowatch.utils.ConstantsManager;

public class DBOpenHelper extends SQLiteOpenHelper {

    @Inject
    public DBOpenHelper(@ApplicationContext Context context) {
        super(context, ConstantsManager.DATABASE_NAME, null, ConstantsManager.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(DBContract.FilmsTable.CREATE);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + DBContract.FilmsTable.TABLE_NAME);
        onCreate(db);
    }
}
