package me.bitfrom.whattowatch.data.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Data {

    @SerializedName("movies")
    @Expose
    private List<Movie> movies = new ArrayList<>();

    /**
     *
     * @return
     * The movies
     */
    public List<Movie> getMovies() {
        return movies;
    }

    /**
     *
     * @param movies
     * The movies
     */
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
