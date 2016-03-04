package me.bitfrom.whattowatch.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class InTheaterPojo {

    @SerializedName("openingThisWeek")
    @Expose
    private Object openingThisWeek;
    @SerializedName("inTheatersNow")
    @Expose
    private Object inTheatersNow;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("movies")
    @Expose
    private List<MoviePojo> movies = new ArrayList<>();


    /**
     * @return
     * The openingThisWeek
     **/
    public Object getOpeningThisWeek() {
        return openingThisWeek;
    }

    /**
     * @param openingThisWeek
     * The openingThisWeek
     **/
    public void setOpeningThisWeek(Object openingThisWeek) {
        this.openingThisWeek = openingThisWeek;
    }

    /**
     * @return
     * The inTheatersNow
     **/
    public Object getInTheatersNow() {
        return inTheatersNow;
    }

    /**
     * @param inTheatersNow
     * The inTheatersNow
     **/
    public void setInTheatersNow(Object inTheatersNow) {
        this.inTheatersNow = inTheatersNow;
    }

    /**
     * @return
     * The date
     **/
    public String getDate() {
        return date;
    }

    /**
     * @param date
     * The date
     **/
    public void setDate(String date) {
        this.date = date;
    }

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
