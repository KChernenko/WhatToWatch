package me.bitfrom.whattowatch.core.storage;

import android.content.Context;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import me.bitfrom.whattowatch.BuildConfig;
import me.bitfrom.whattowatch.core.image.ImageDownloader;
import me.bitfrom.whattowatch.core.model.MoviePojo;
import me.bitfrom.whattowatch.core.storage.entities.FilmEntity;
import me.bitfrom.whattowatch.test.common.TestFilmFactory;
import me.bitfrom.whattowatch.utils.ConstantsManager;
import me.bitfrom.whattowatch.utils.DefaultConfig;
import me.bitfrom.whattowatch.utils.RxSchedulersOverrideRule;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = DefaultConfig.EMULATED_SDK)
public class DbHelperTest {

    @Mock
    ImageDownloader imageDownloader;

    private DbHelper dbHelper;

    @Rule
    public final RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Context context = RuntimeEnvironment.application.getApplicationContext();

        dbHelper = new DbHelper(new DbOpenHelper(context), imageDownloader);
    }

    @Test
    public void insertFilmsEmitsPassedValues() {
        List<MoviePojo> movies = TestFilmFactory.generateMovies(2);
        TestSubscriber<List<MoviePojo>> result = new TestSubscriber<>();

        dbHelper.insertFilms(movies, ConstantsManager.CATEGORY_TOP).subscribe(result);

        //Insertion completes without errors
        result.assertNoErrors();
        //Contains passed values
        result.assertValue(movies);
    }

    @Test
    public void selectFilmsByCategoryIdReturnsInsertedFilmsWithCorrectCategoryId() {
        List<MoviePojo> movies = TestFilmFactory.generateMovies(2);
        List<FilmEntity> films = TestFilmFactory.convertToFilmEntity(movies, ConstantsManager.NOT_FAVORITE);

        TestSubscriber<List<FilmEntity>> result = new TestSubscriber<>();

        dbHelper.insertFilms(movies, ConstantsManager.CATEGORY_TOP).subscribe();
        dbHelper.selectFilmsByCategoryId(ConstantsManager.CATEGORY_TOP).subscribe(result);

        result.assertNoErrors();
        // We need to be sure that selection result contains
        // exactly the same elements with same values which were inserted
        result.assertValue(films);
    }

    @Test
    public void selectFilmByIdReturnsCorrectFilm() {
        List<MoviePojo> movies = TestFilmFactory.generateMovies(2);
        TestSubscriber<FilmEntity> result = new TestSubscriber<>();

        dbHelper.insertFilms(movies, ConstantsManager.CATEGORY_TOP).subscribe();
        dbHelper.selectFilmById(movies.get(0).getIdIMDB()).subscribe(result);

        result.assertNoErrors();
        //Returned film has requested id
        assertThat(result.getOnNextEvents().get(0).imdbId()).isEqualTo(movies.get(0).getIdIMDB());
    }

    @Test
    public void selectFavoriteFilmsReturnsFilmsThatAreInFavorite() {
        List<MoviePojo> movies = TestFilmFactory.generateMovies(3);
        List<FilmEntity> films = TestFilmFactory.convertToFilmEntity(movies, ConstantsManager.FAVORITE);
        TestSubscriber<List<FilmEntity>> result = new TestSubscriber<>();

        dbHelper.insertFilms(movies, ConstantsManager.CATEGORY_TOP).subscribe();
        for (int i = 0; i < movies.size(); i++) {
            dbHelper.updateFavorite(movies.get(i).getIdIMDB(), ConstantsManager.FAVORITE);
        }
        dbHelper.selectFavoriteFilms().subscribe(result);

        result.assertNoErrors();
        result.assertValue(films);
    }

    @Test
    public void updateFavoriteShouldUpdateDatabaseEntries() {
        int amountOfFilms = 5;
        int amountOfFavoriteFilms = 2;
        List<MoviePojo> movies = TestFilmFactory.generateMovies(amountOfFilms);
        TestSubscriber<List<FilmEntity>> favoriteSelectionResult = new TestSubscriber<>();

        dbHelper.insertFilms(movies, ConstantsManager.CATEGORY_TOP).subscribe();
        //Making 2 films favorite
        for (int i = 0; i < amountOfFavoriteFilms; i++) {
            dbHelper.updateFavorite(movies.get(i).getIdIMDB(), ConstantsManager.FAVORITE);
        }
        //Selecting favorite films
        dbHelper.selectFavoriteFilms().subscribe(favoriteSelectionResult);

        favoriteSelectionResult.assertNoErrors();
        //Checking that amount of favorite films equals to expected value
        assertThat(favoriteSelectionResult.getOnNextEvents().get(0).size())
                .isEqualTo(amountOfFavoriteFilms);
        //Checking that each item in the selection result has property inFavorite = 1
        for (int i = 0; i < amountOfFavoriteFilms; i++) {
            assertThat(favoriteSelectionResult.getOnNextEvents().get(0).get(i).inFavorite())
                    .isEqualTo(ConstantsManager.FAVORITE);
        }
        //Making those favorite films not favorite
        for (int i = 0; i < amountOfFavoriteFilms; i++) {
            dbHelper.updateFavorite(movies.get(i).getIdIMDB(), ConstantsManager.NOT_FAVORITE);
        }

        TestSubscriber<List<FilmEntity>> favoriteSelectionResultIsEmpty = new TestSubscriber<>();
        //Selecting all favorite films again
        dbHelper.selectFavoriteFilms().subscribe(favoriteSelectionResultIsEmpty);

        favoriteSelectionResultIsEmpty.assertNoErrors();
        //Checking that there are no favorite films entries in the result
        assertThat(favoriteSelectionResultIsEmpty.getOnNextEvents().get(0).size()).isEqualTo(0);
    }
}