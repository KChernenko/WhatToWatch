package me.bitfrom.whattowatch.utils;

import java.util.Random;

/**
 * Created by Constantine with love.
 */
public class FilmIdGenerator {

    private static volatile FilmIdGenerator mFilmGenerator;

    private FilmIdGenerator() {

    }

    public static FilmIdGenerator getGenerator() {
        FilmIdGenerator localGenerator = mFilmGenerator;
        if (localGenerator == null) {
            synchronized (FilmIdGenerator.class) {
                localGenerator = mFilmGenerator;
                if (localGenerator == null) {
                    mFilmGenerator = localGenerator = new FilmIdGenerator();
                }
            }
        }

        return localGenerator;
    }

    /**
     * Generates an id of film in diapason between 1 and 250
     * @return random number
     * **/
    public int getRandomFilmID() {

        final int min = 1;
        final int max = 249;

        Random random = new Random();

        int randomID = random.nextInt((max - min) + 1) + min;


        return randomID;
    }
}
