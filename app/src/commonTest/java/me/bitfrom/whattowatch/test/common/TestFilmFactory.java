package me.bitfrom.whattowatch.test.common;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import me.bitfrom.whattowatch.core.model.DirectorPojo;
import me.bitfrom.whattowatch.core.model.MoviePojo;
import me.bitfrom.whattowatch.core.model.WriterPojo;
import me.bitfrom.whattowatch.core.storage.entities.FilmEntity;
import me.bitfrom.whattowatch.utils.ConstantsManager;

/**
 * Factory class that makes instances of data models with random field values.
 * The aim of this class is to help setting up test fixtures.
 */
public class TestFilmFactory {

    public static MoviePojo generateFilm(String uniqueSuffix) {
        MoviePojo film = new MoviePojo();
        film.setIdIMDB(randomUuid());
        film.setUrlIMDB("http://www.imdb.com/title/tt" + uniqueSuffix);
        film.setCountries(new ArrayList<>(Arrays.asList("Country #" + uniqueSuffix, "Country #" + uniqueSuffix)));
        DirectorPojo[] directorsPojo = new DirectorPojo[2];
        directorsPojo[0] = new DirectorPojo();
        directorsPojo[0].setName("Random director");
        directorsPojo[0].setNameId(uniqueSuffix);
        directorsPojo[1] = new DirectorPojo();
        directorsPojo[1].setName("Another director");
        directorsPojo[1].setNameId(uniqueSuffix);
        film.setDirectors(Arrays.asList(directorsPojo));
        WriterPojo[] writersPojo = new WriterPojo[2];
        writersPojo[0] = new WriterPojo();
        writersPojo[0].setName("Some writer");
        writersPojo[0].setNameId(uniqueSuffix);
        writersPojo[1] = new WriterPojo();
        writersPojo[1].setName("Another writer");
        writersPojo[1].setNameId(uniqueSuffix);
        film.setWriters(Arrays.asList(writersPojo));
        film.setGenres(Arrays.asList("Genre #" + uniqueSuffix, "Genre #" + uniqueSuffix));
        film.setPlot("Some interesting and amazing and really thrilling very long plot!");
        film.setRating("9.9");
        film.setRuntime(uniqueSuffix);
        film.setTitle("Matrix #" + uniqueSuffix);
        film.setUrlPoster("http://ia.media-imdb.com/images/M/MV5BMTkxNDYxOTA4M15BMl5BanBnXkFtZTgwNTk0NzQxMTE@._V1_.jpg");
        film.setYear(uniqueSuffix);
        return film;
    }

    public static List<MoviePojo> generateFilms(int number) {
        List<MoviePojo> films = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            films.add(generateFilm(String.valueOf(i * 42)));
        }

        return films;
    }

    public static List<FilmEntity> convertToFilmEntity(List<MoviePojo> moviePojos) {
        List<FilmEntity> filmEntityList = new ArrayList<>();
        for (int i = 0; i < moviePojos.size(); i++) {
            FilmEntity filmEntity = FilmEntity.FACTORY.creator
                    .create(moviePojos.get(i).getIdIMDB(),
                            moviePojos.get(i).getUrlIMDB(),
                            TextUtils.join(", ", moviePojos.get(i).getCountries()),
                            TextUtils.join(", ", moviePojos.get(i).getDirectors()),
                            TextUtils.join(", ", moviePojos.get(i).getWriters()),
                            TextUtils.join(", ", moviePojos.get(i).getGenres()),
                            moviePojos.get(i).getPlot(),
                            moviePojos.get(i).getRating(),
                            moviePojos.get(i).getRuntime(),
                            moviePojos.get(i).getTitle(),
                            moviePojos.get(i).getUrlPoster(),
                            moviePojos.get(i).getYear(),
                            ConstantsManager.NOT_FAVORITE,
                            ConstantsManager.CATEGORY_TOP);
            filmEntityList.add(filmEntity);
        }

        return filmEntityList;
    }

    private static String randomUuid() {
        return UUID.randomUUID().toString();
    }
}