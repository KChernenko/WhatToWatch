package me.bitfrom.whattowatch.core.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import me.bitfrom.whattowatch.BuildConfig;
import me.bitfrom.whattowatch.utils.DefaultConfig;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = DefaultConfig.EMULATED_SDK)
public class DbOpenHelperTest {

    private Context context;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application.getApplicationContext();
    }

    @Test
    public void createDatabase() {
        DbOpenHelper dbOpenHelper = new DbOpenHelper(context);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        assertTrue(db.isOpen());
        db.close();
    }

    @Test
    public void upgradeDatabase() {
        DbOpenHelper dbOpenHelper = new DbOpenHelper(context);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int oldVersion = db.getVersion();
        db.setVersion(oldVersion + 1);
        assertNotEquals(oldVersion, db.getVersion());
        db.close();
    }

    @Test
    public void dropDatabase() {
        DbOpenHelper dbOpenHelper = new DbOpenHelper(context);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.close();
        assertTrue(context.deleteDatabase(db.getPath()));
    }
}