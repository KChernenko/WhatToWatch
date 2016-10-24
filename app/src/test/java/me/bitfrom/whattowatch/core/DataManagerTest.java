package me.bitfrom.whattowatch.core;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import me.bitfrom.whattowatch.core.rest.FilmsAPI;
import me.bitfrom.whattowatch.core.storage.DbHelper;
import me.bitfrom.whattowatch.core.storage.PreferencesHelper;
import me.bitfrom.whattowatch.utils.RxSchedulersOverrideRule;

import static org.assertj.core.api.Assertions.assertThat;

public class DataManagerTest {

    @Mock
    FilmsAPI filmsAPI;
    @Mock
    DbHelper dbHelper;
    @Mock
    PreferencesHelper preferencesHelper;

    private DataManager dataManager;

    @Rule
    public final RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        dataManager = new DataManager(filmsAPI, dbHelper, preferencesHelper);
    }

    @Test
    public void getPreferencesHelperShouldReturnCorrectInstance() {
        assertThat(dataManager.getPreferencesHelper())
                .isInstanceOf(PreferencesHelper.class);
    }
}