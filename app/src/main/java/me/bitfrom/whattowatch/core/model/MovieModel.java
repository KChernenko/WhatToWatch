package me.bitfrom.whattowatch.core.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;

@JsonObject
public class MovieModel {

    @JsonField(name = "title")
    private String title;
    @JsonField(name = "originalTitle")
    private String originalTitle;
    @JsonField(name = "year")
    private String year;
    @JsonField(name = "releaseDate")
    private String releaseDate;
    @JsonField(name = "directors")
    private List<DirectorModel> directors = new ArrayList<>();
    @JsonField(name = "writers")
    private List<WriterModel> writers = new ArrayList<>();
    @JsonField(name = "runtime")
    private String runtime;
    @JsonField(name = "urlPoster")
    private String urlPoster;
    @JsonField(name = "countries")
    private List<String> countries = new ArrayList<>();
    @JsonField(name = "languages")
    private List<String> languages = new ArrayList<>();
    @JsonField(name = "genres")
    private List<String> genres = new ArrayList<>();
    @JsonField(name = "plot")
    private String plot;
    @JsonField(name = "simplePlot")
    private String simplePlot;
    @JsonField(name = "idIMDB")
    private String idIMDB;
    @JsonField(name = "urlIMDB")
    private String urlIMDB;
    @JsonField(name = "rating")
    private String rating;
    @JsonField(name = "metaScore")
    private String metaScore;
    @JsonField(name = "filmingLocations")
    private List<String> filmingLocations = new ArrayList<>();
    @JsonField(name = "rated")
    private String rated;
    @JsonField(name = "votes")
    private String votes;
    @JsonField(name = "type")
    private String type;
    @JsonField(name = "ranking")
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

    public List<DirectorModel> getDirectors() {
        return directors;
    }

    public void setDirectors(List<DirectorModel> directors) {
        this.directors = directors;
    }

    public List<WriterModel> getWriters() {
        return writers;
    }

    public void setWriters(List<WriterModel> writers) {
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

    public String getMetaScore() {
        return metaScore;
    }

    public void setMetaScore(String metaScore) {
        this.metaScore = metaScore;
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