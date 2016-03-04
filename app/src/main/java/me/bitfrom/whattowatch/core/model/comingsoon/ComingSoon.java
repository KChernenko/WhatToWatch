package me.bitfrom.whattowatch.core.model.comingsoon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import me.bitfrom.whattowatch.core.model.MoviePojo;

public class ComingSoon {

    @Expose
    private String date;
    @SerializedName("movies")
    @Expose
    private List<MoviePojo> movies = new ArrayList<>();

    /**
     *
     * @return
     * The date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The date
     */
    public void setDate(String date) {
        this.date = date;
    }

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
