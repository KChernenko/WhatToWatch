package me.bitfrom.whattowatch.core.model.comingsoon;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;

import me.bitfrom.whattowatch.core.model.MovieModel;

@JsonObject
public class ComingSoon {

    @JsonField(name = "date")
    private String date;
    @JsonField(name = "movies")
    private List<MovieModel> movies = new ArrayList<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<MovieModel> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieModel> movies) {
        this.movies = movies;
    }

}