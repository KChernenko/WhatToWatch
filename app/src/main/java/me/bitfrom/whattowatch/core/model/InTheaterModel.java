package me.bitfrom.whattowatch.core.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;

@JsonObject
public class InTheaterModel {

    @JsonField(name = "openingThisWeek")
    private String openingThisWeek;
    @JsonField(name = "inTheatersNow")
    private String inTheatersNow;
    @JsonField(name = "date")
    private String date;
    @JsonField(name = "movies")
    private List<MovieModel> movies = new ArrayList<>();

    public String getOpeningThisWeek() {
        return openingThisWeek;
    }

    public void setOpeningThisWeek(String openingThisWeek) {
        this.openingThisWeek = openingThisWeek;
    }

    public String getInTheatersNow() {
        return inTheatersNow;
    }

    public void setInTheatersNow(String inTheatersNow) {
        this.inTheatersNow = inTheatersNow;
    }

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