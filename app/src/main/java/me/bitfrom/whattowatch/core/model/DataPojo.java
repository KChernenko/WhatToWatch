package me.bitfrom.whattowatch.core.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;

@JsonObject
public class DataPojo {

    @JsonField
    private List<MoviePojo> movies = new ArrayList<>();

    public List<MoviePojo> getMovies() {
        return movies;
    }

    public void setMovies(List<MoviePojo> movies) {
        this.movies = movies;
    }
}