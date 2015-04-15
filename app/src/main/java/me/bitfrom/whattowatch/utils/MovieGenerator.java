package me.bitfrom.whattowatch.utils;

import java.util.Random;

/**
 * Created by Constantine with love.
 */
public class MovieGenerator {

    private static volatile MovieGenerator mMovieGenerator;

    private MovieGenerator() {

    }

    public static MovieGenerator getGenerator() {
        MovieGenerator localGenerator = mMovieGenerator;
        if (localGenerator == null) {
            synchronized (MovieGenerator.class) {
                localGenerator = mMovieGenerator;
                if (localGenerator == null) {
                    mMovieGenerator = localGenerator = new MovieGenerator();
                }
            }
        }

        return localGenerator;
    }

    public int getRandomMovieID() {

        final int min = 1;
        final int max = 249;

        Random random = new Random();

        int randomID = random.nextInt((max - min) + 1) + min;


        return randomID;
    }
}
