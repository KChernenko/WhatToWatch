package me.bitfrom.whattowatch.core.model.comingsoon;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;

import me.bitfrom.whattowatch.core.model.MoviePojo;

@JsonObject
public class ComingSoon {

    @JsonField
    private String date;
    @JsonField
    private List<MoviePojo> movies = new ArrayList<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<MoviePojo> getMovies() {
        return movies;
    }

    public void setMovies(List<MoviePojo> movies) {
        this.movies = movies;
    }

}