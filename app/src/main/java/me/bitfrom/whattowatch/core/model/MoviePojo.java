package me.bitfrom.whattowatch.core.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;

@JsonObject
public class MoviePojo {

    @JsonField
    private String title;
    @JsonField
    private String originalTitle;
    @JsonField
    private String year;
    @JsonField
    private String releaseDate;
    @JsonField
    private List<DirectorPojo> directors = new ArrayList<>();
    @JsonField
    private List<WriterPojo> writers = new ArrayList<>();
    @JsonField
    private String runtime;
    @JsonField
    private String urlPoster;
    @JsonField
    private List<String> countries = new ArrayList<>();
    @JsonField
    private List<String> languages = new ArrayList<>();
    @JsonField
    private List<String> genres = new ArrayList<>();
    @JsonField
    private String plot;
    @JsonField
    private String simplePlot;
    @JsonField
    private String idIMDB;
    @JsonField
    private String urlIMDB;
    @JsonField
    private String rating;
    @JsonField
    private String metascore;
    @JsonField
    private List<String> filmingLocations = new ArrayList<>();
    @JsonField
    private String rated;
    @JsonField
    private String votes;
    @JsonField
    private String type;
    @JsonField
    private Integer ranking;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<DirectorPojo> getDirectors() {
        return directors;
    }

    public void setDirectors(List<DirectorPojo> directors) {
        this.directors = directors;
    }

    public List<WriterPojo> getWriters() {
        return writers;
    }

    public void setWriters(List<WriterPojo> writers) {
        this.writers = writers;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getUrlPoster() {
        return urlPoster;
    }

    public void setUrlPoster(String urlPoster) {
        this.urlPoster = urlPoster;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getSimplePlot() {
        return simplePlot;
    }

    public void setSimplePlot(String simplePlot) {
        this.simplePlot = simplePlot;
    }

    public String getIdIMDB() {
        return idIMDB;
    }

    public void setIdIMDB(String idIMDB) {
        this.idIMDB = idIMDB;
    }

    public String getUrlIMDB() {
        return urlIMDB;
    }

    public void setUrlIMDB(String urlIMDB) {
        this.urlIMDB = urlIMDB;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getMetascore() {
        return metascore;
    }

    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    public List<String> getFilmingLocations() {
        return filmingLocations;
    }

    public void setFilmingLocations(List<String> filmingLocations) {
        this.filmingLocations = filmingLocations;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }
}