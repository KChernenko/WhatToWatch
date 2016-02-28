package me.bitfrom.whattowatch.core.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MoviePojo {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("originalTitle")
    @Expose
    private String originalTitle;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("releaseDate")
    @Expose
    private String releaseDate;
    @SerializedName("directors")
    @Expose
    private List<DirectorPojo> directors = new ArrayList<>();
    @SerializedName("writers")
    @Expose
    private List<WriterPojo> writers = new ArrayList<>();
    @SerializedName("runtime")
    @Expose
    private List<String> runtime = new ArrayList<>();
    @SerializedName("urlPoster")
    @Expose
    private String urlPoster;
    @SerializedName("countries")
    @Expose
    private List<String> countries = new ArrayList<>();
    @SerializedName("languages")
    @Expose
    private List<String> languages = new ArrayList<>();
    @SerializedName("genres")
    @Expose
    private List<String> genres = new ArrayList<>();
    @SerializedName("plot")
    @Expose
    private String plot;
    @SerializedName("simplePlot")
    @Expose
    private String simplePlot;
    @SerializedName("idIMDB")
    @Expose
    private String idIMDB;
    @SerializedName("urlIMDB")
    @Expose
    private String urlIMDB;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("metascore")
    @Expose
    private String metascore;
    @SerializedName("filmingLocations")
    @Expose
    private List<String> filmingLocations = new ArrayList<>();
    @SerializedName("rated")
    @Expose
    private String rated;
    @SerializedName("votes")
    @Expose
    private String votes;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("ranking")
    @Expose
    private Integer ranking;

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The originalTitle
     */
    public String getOriginalTitle() {
        return originalTitle;
    }

    /**
     *
     * @param originalTitle
     * The originalTitle
     */
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    /**
     *
     * @return
     * The year
     */
    public String getYear() {
        return year;
    }

    /**
     *
     * @param year
     * The year
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     *
     * @return
     * The releaseDate
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     *
     * @param releaseDate
     * The releaseDate
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     *
     * @return
     * The directors
     */
    public List<DirectorPojo> getDirectors() {
        return directors;
    }

    /**
     *
     * @param directors
     * The directors
     */
    public void setDirectors(List<DirectorPojo> directors) {
        this.directors = directors;
    }

    /**
     *
     * @return
     * The writers
     */
    public List<WriterPojo> getWriters() {
        return writers;
    }

    /**
     *
     * @param writers
     * The writers
     */
    public void setWriters(List<WriterPojo> writers) {
        this.writers = writers;
    }

    /**
     *
     * @return
     * The runtime
     */
    public List<String> getRuntime() {
        return runtime;
    }

    /**
     *
     * @param runtime
     * The runtime
     */
    public void setRuntime(List<String> runtime) {
        this.runtime = runtime;
    }

    /**
     *
     * @return
     * The urlPoster
     */
    public String getUrlPoster() {
        return urlPoster;
    }

    /**
     *
     * @param urlPoster
     * The urlPoster
     */
    public void setUrlPoster(String urlPoster) {
        this.urlPoster = urlPoster;
    }

    /**
     *
     * @return
     * The countries
     */
    public List<String> getCountries() {
        return countries;
    }

    /**
     *
     * @param countries
     * The countries
     */
    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    /**
     *
     * @return
     * The languages
     */
    public List<String> getLanguages() {
        return languages;
    }

    /**
     *
     * @param languages
     * The languages
     */
    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    /**
     *
     * @return
     * The genres
     */
    public List<String> getGenres() {
        return genres;
    }

    /**
     *
     * @param genres
     * The genres
     */
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    /**
     *
     * @return
     * The plot
     */
    public String getPlot() {
        return plot;
    }

    /**
     *
     * @param plot
     * The plot
     */
    public void setPlot(String plot) {
        this.plot = plot;
    }

    /**
     *
     * @return
     * The simplePlot
     */
    public String getSimplePlot() {
        return simplePlot;
    }

    /**
     *
     * @param simplePlot
     * The simplePlot
     */
    public void setSimplePlot(String simplePlot) {
        this.simplePlot = simplePlot;
    }

    /**
     *
     * @return
     * The idIMDB
     */
    public String getIdIMDB() {
        return idIMDB;
    }

    /**
     *
     * @param idIMDB
     * The idIMDB
     */
    public void setIdIMDB(String idIMDB) {
        this.idIMDB = idIMDB;
    }

    /**
     *
     * @return
     * The urlIMDB
     */
    public String getUrlIMDB() {
        return urlIMDB;
    }

    /**
     *
     * @param urlIMDB
     * The urlIMDB
     */
    public void setUrlIMDB(String urlIMDB) {
        this.urlIMDB = urlIMDB;
    }

    /**
     *
     * @return
     * The rating
     */
    public String getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     * The rating
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     *
     * @return
     * The metascore
     */
    public String getMetascore() {
        return metascore;
    }

    /**
     *
     * @param metascore
     * The metascore
     */
    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    /**
     *
     * @return
     * The filmingLocations
     */
    public List<String> getFilmingLocations() {
        return filmingLocations;
    }

    /**
     *
     * @param filmingLocations
     * The filmingLocations
     */
    public void setFilmingLocations(List<String> filmingLocations) {
        this.filmingLocations = filmingLocations;
    }

    /**
     *
     * @return
     * The rated
     */
    public String getRated() {
        return rated;
    }

    /**
     *
     * @param rated
     * The rated
     */
    public void setRated(String rated) {
        this.rated = rated;
    }

    /**
     *
     * @return
     * The votes
     */
    public String getVotes() {
        return votes;
    }

    /**
     *
     * @param votes
     * The votes
     */
    public void setVotes(String votes) {
        this.votes = votes;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The ranking
     */
    public Integer getRanking() {
        return ranking;
    }

    /**
     *
     * @param ranking
     * The ranking
     */
    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }
}
