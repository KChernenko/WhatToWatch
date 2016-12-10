package me.bitfrom.whattowatch.test.common;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import me.bitfrom.whattowatch.core.model.DirectorModel;
import me.bitfrom.whattowatch.core.model.InTheaterModel;
import me.bitfrom.whattowatch.core.model.MovieModel;
import me.bitfrom.whattowatch.core.model.WriterModel;
import me.bitfrom.whattowatch.core.storage.entities.FilmEntity;

/**
 * Factory class that makes instances of data models with random field values.
 * The aim of this class is to help setting up test fixtures.
 */
public class TestFilmFactory {

    public static MovieModel generateMovie(String uniqueSuffix) {
        MovieModel film = new MovieModel();
        film.setIdIMDB(randomUuid());
        film.setUrlIMDB("http://www.imdb.com/title/tt" + uniqueSuffix);
        film.setCountries(new ArrayList<>(Arrays.asList("Country #" + uniqueSuffix, "Country #" + uniqueSuffix)));
        DirectorModel[] directorsPojo = new DirectorModel[2];
        directorsPojo[0] = new DirectorModel();
        directorsPojo[0].setName("Random director");
        directorsPojo[0].setNameId(uniqueSuffix);
        directorsPojo[1] = new DirectorModel();
        directorsPojo[1].setName("Another director");
        directorsPojo[1].setNameId(uniqueSuffix);
        film.setDirectors(Arrays.asList(directorsPojo));
        WriterModel[] writersPojo = new WriterModel[2];
        writersPojo[0] = new WriterModel();
        writersPojo[0].setName("Some writer");
        writersPojo[0].setNameId(uniqueSuffix);
        writersPojo[1] = new WriterModel();
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

    public static List<MovieModel> generateMovies(int number) {
        List<MovieModel> films = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            films.add(generateMovie(String.valueOf(i * 42)));
        }

        return films;
    }

    public static List<FilmEntity> convertToFilmEntity(List<MovieModel> movieModels, int favoriteFlag,
                                                       int categoryFlag) {
        List<FilmEntity> filmEntityList = new ArrayList<>();
        for (int i = 0; i < movieModels.size(); i++) {
            FilmEntity filmEntity = FilmEntity.FACTORY.creator
                    .create(movieModels.get(i).getIdIMDB(),
                            movieModels.get(i).getUrlIMDB(),
                            join(", ", movieModels.get(i).getCountries()),
                            join(", ", Stream.of(movieModels.get(i).getDirectors())
                                    .map(DirectorModel::getName).collect(Collectors.toList())),
                            join(", ", Stream.of(movieModels.get(i).getWriters())
                                    .map(WriterModel::getName).collect(Collectors.toList())),
                            join(", ", movieModels.get(i).getGenres()),
                            movieModels.get(i).getPlot(),
                            movieModels.get(i).getRating(),
                            movieModels.get(i).getRuntime(),
                            movieModels.get(i).getTitle(),
                            movieModels.get(i).getUrlPoster(),
                            movieModels.get(i).getYear(),
                            favoriteFlag,
                            categoryFlag);
            filmEntityList.add(filmEntity);
        }

        return filmEntityList;
    }

    public static List<InTheaterModel> generateInTheatres(int number) {
        List<InTheaterModel> list = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            InTheaterModel inTheaterModel = new InTheaterModel();
            inTheaterModel.setOpeningThisWeek(randomUuid());
            inTheaterModel.setInTheatersNow(randomUuid());
            inTheaterModel.setDate(randomUuid());
            inTheaterModel.setMovies(generateMovies(number));
        }

        return list;
    }

    private static String randomUuid() {
        return UUID.randomUUID().toString();
    }

    public static String join(String delim, List<String> list) {

        StringBuilder sb = new StringBuilder();

        String loopDelim = "";

        for(String s : list) {

            sb.append(loopDelim);
            sb.append(s);

            loopDelim = delim;
        }

        return sb.toString();
    }
}