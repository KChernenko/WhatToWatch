package me.bitfrom.whattowatch.data.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class DataPojo {

    @SerializedName("movies")
    @Expose
    private List<MoviePojo> movies = new ArrayList<>();

    /**
     *
     * @return
     * The movies
     */
    public List<MoviePojo> getMovies() {
        return movies;
    }

    /**
     *
     * @param movies
     * The movies
     */
    public void setMovies(List<MoviePojo> movies) {
        this.movies = movies;
    }
}
