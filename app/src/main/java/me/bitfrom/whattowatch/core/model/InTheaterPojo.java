package me.bitfrom.whattowatch.core.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;

@JsonObject
public class InTheaterPojo {

    @JsonField
    private Object openingThisWeek;
    @JsonField
    private Object inTheatersNow;
    @JsonField
    private String date;
    @JsonField
    private List<MoviePojo> movies = new ArrayList<>();

    public Object getOpeningThisWeek() {
        return openingThisWeek;
    }

    public void setOpeningThisWeek(Object openingThisWeek) {
        this.openingThisWeek = openingThisWeek;
    }

    public Object getInTheatersNow() {
        return inTheatersNow;
    }

    public void setInTheatersNow(Object inTheatersNow) {
        this.inTheatersNow = inTheatersNow;
    }

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