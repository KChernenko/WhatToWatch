package me.bitfrom.whattowatch.core.storage;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import me.bitfrom.whattowatch.BuildConfig;
import me.bitfrom.whattowatch.utils.DefaultConfig;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = DefaultConfig.EMULATED_SDK)
public class PreferencesHelperTest {

    @Before
    public void setUp() {

    }
}