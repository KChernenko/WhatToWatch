package me.bitfrom.whattowatch.core.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DataPojo {

    @SerializedName("movies")
    private List<MoviePojo> movies = new ArrayList<>();


    /**
     * @return
     * The movies
     **/
    public List<MoviePojo> getMovies() {
        return movies;
    }


    /**
     * @param movies
     * The movies
     **/
    public void setMovies(List<MoviePojo> movies) {
        this.movies = movies;
    }
}
