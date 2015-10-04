package me.bitfrom.whattowatch.rest.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Constantine with love.
 */
public class Film implements Parcelable {

    @SerializedName("countries")
    private List<String> countries = new ArrayList<>();
    @SerializedName("directors")
    private List<Director> directors = new ArrayList<>();
    @SerializedName("filmingLocations")
    private List<String> filmingLocations = new ArrayList<>();
    @SerializedName("genres")
    private List<String> genres = new ArrayList<>();
    @SerializedName("idIMDB")
    private String idIMDB;
    @SerializedName("languages")
    private List<String> languages = new ArrayList<>();
    @SerializedName("metascore")
    private String metascore;
    @SerializedName("originalTitle")
    private String originalTitle;
    @SerializedName("plot")
    private String plot;
    @SerializedName("ranking")
    private Integer ranking;
    @SerializedName("rated")
    private String rated;
    @SerializedName("rating")
    private String rating;
    @SerializedName("releaseDate")
    private String releaseDate;
    @SerializedName("runtime")
    private List<String> runtime = new ArrayList<>();
    @SerializedName("simplePlot")
    private String simplePlot;
    @SerializedName("title")
    private String title;
    @SerializedName("urlIMDB")
    private String urlIMDB;
    @SerializedName("urlPoster")
    private String urlPoster;
    @SerializedName("votes")
    private String votes;
    @SerializedName("writers")
    private List<Writer> writers = new ArrayList<>();
    @SerializedName("year")
    private String year;

    public Film(List<String> countries, List<Director> directors, List<String> filmingLocations,
                List<String> genres, String idIMDB, List<String> languages,
                String metascore, String originalTitle, String plot, Integer ranking,
                String rated, String rating, String releaseDate, List<String> runtime,
                String simplePlot, String title, String urlIMDB, String urlPoster,
                String votes, List<Writer> writers, String year) {
        this.countries = countries;
        this.directors = directors;
        this.filmingLocations = filmingLocations;
        this.genres = genres;
        this.idIMDB = idIMDB;
        this.languages = languages;
        this.metascore = metascore;
        this.originalTitle = originalTitle;
        this.plot = plot;
        this.ranking = ranking;
        this.rated = rated;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.simplePlot = simplePlot;
        this.title = title;
        this.urlIMDB = urlIMDB;
        this.urlPoster = urlPoster;
        this.votes = votes;
        this.writers = writers;
        this.year = year;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<Director> getDirectors() {
        return directors;
    }

    public void setDirectors(List<Director> directors) {
        this.directors = directors;
    }

    public List<String> getFilmingLocations() {
        return filmingLocations;
    }

    public void setFilmingLocations(List<String> filmingLocations) {
        this.filmingLocations = filmingLocations;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getIdIMDB() {
        return idIMDB;
    }

    public void setIdIMDB(String idIMDB) {
        this.idIMDB = idIMDB;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public String getMetascore() {
        return metascore;
    }

    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<String> getRuntime() {
        return runtime;
    }

    public void setRuntime(List<String> runtime) {
        this.runtime = runtime;
    }

    public String getSimplePlot() {
        return simplePlot;
    }

    public void setSimplePlot(String simplePlot) {
        this.simplePlot = simplePlot;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlIMDB() {
        return urlIMDB;
    }

    public void setUrlIMDB(String urlIMDB) {
        this.urlIMDB = urlIMDB;
    }

    public String getUrlPoster() {
        return urlPoster;
    }

    public void setUrlPoster(String urlPoster) {
        this.urlPoster = urlPoster;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public List<Writer> getWriters() {
        return writers;
    }

    public void setWriters(List<Writer> writers) {
        this.writers = writers;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.countries);
        dest.writeTypedList(directors);
        dest.writeStringList(this.filmingLocations);
        dest.writeStringList(this.genres);
        dest.writeString(this.idIMDB);
        dest.writeStringList(this.languages);
        dest.writeString(this.metascore);
        dest.writeString(this.originalTitle);
        dest.writeString(this.plot);
        dest.writeValue(this.ranking);
        dest.writeString(this.rated);
        dest.writeString(this.rating);
        dest.writeString(this.releaseDate);
        dest.writeStringList(this.runtime);
        dest.writeString(this.simplePlot);
        dest.writeString(this.title);
        dest.writeString(this.urlIMDB);
        dest.writeString(this.urlPoster);
        dest.writeString(this.votes);
        dest.writeTypedList(writers);
        dest.writeString(this.year);
    }

    protected Film(Parcel in) {
        this.countries = in.createStringArrayList();
        this.directors = in.createTypedArrayList(Director.CREATOR);
        this.filmingLocations = in.createStringArrayList();
        this.genres = in.createStringArrayList();
        this.idIMDB = in.readString();
        this.languages = in.createStringArrayList();
        this.metascore = in.readString();
        this.originalTitle = in.readString();
        this.plot = in.readString();
        this.ranking = (Integer) in.readValue(Integer.class.getClassLoader());
        this.rated = in.readString();
        this.rating = in.readString();
        this.releaseDate = in.readString();
        this.runtime = in.createStringArrayList();
        this.simplePlot = in.readString();
        this.title = in.readString();
        this.urlIMDB = in.readString();
        this.urlPoster = in.readString();
        this.votes = in.readString();
        this.writers = in.createTypedArrayList(Writer.CREATOR);
        this.year = in.readString();
    }

    public static final Parcelable.Creator<Film> CREATOR = new Parcelable.Creator<Film>() {
        public Film createFromParcel(Parcel source) {
            return new Film(source);
        }

        public Film[] newArray(int size) {
            return new Film[size];
        }
    };
}