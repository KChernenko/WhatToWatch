package me.bitfrom.whattowatch.core.storage;

import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import me.bitfrom.whattowatch.BuildConfig;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import me.bitfrom.whattowatch.utils.DefaultConfig;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = DefaultConfig.EMULATED_SDK)
public class PreferencesHelperTest {

    private PreferencesHelper preferencesHelper;

    @Before
    public void setUp() {
        preferencesHelper = new PreferencesHelper(RuntimeEnvironment.application.
                getApplicationContext());
    }

    @Test
    public void getSharedPreferencesShouldReturnSharedPreferencesInstance() {
        assertThat(preferencesHelper.getSharedPreferences())
                .isInstanceOf(SharedPreferences.class);
    }

    @Test
    public void checkIfFirstLaunchedShouldReturnCorrectValue() {
        assertThat(preferencesHelper.checkIfFirstLaunched())
                .isEqualTo(true);

        preferencesHelper.markFirstLaunched();

        assertThat(preferencesHelper.checkIfFirstLaunched())
                .isEqualTo(false);
    }

    @Test
    public void getPreferredNumberOfFilmsShouldReturnSevenByDefault() {
        assertThat(preferencesHelper.getPreferredNumberOfFilms())
                .isEqualTo(7);
    }

    @Test
    public void getUpdateIntervalShouldReturnFiveDaysByDefault() {
        assertThat(preferencesHelper.getUpdateInterval())
                .isEqualTo(ConstantsManager.FIVE_DAYS);
    }
}