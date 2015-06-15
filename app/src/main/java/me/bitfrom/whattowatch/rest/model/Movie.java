package me.bitfrom.whattowatch.rest.model;


import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Constantine with love.
 */
public class Movie {
    @Expose
    private List<String> countries = new ArrayList<String>();
    @Expose
    private List<Director> directors = new ArrayList<Director>();
    @Expose
    private List<String> filmingLocations = new ArrayList<String>();
    @Expose
    private List<String> genres = new ArrayList<String>();
    @Expose
    private String idIMDB;
    @Expose
    private List<String> languages = new ArrayList<String>();
    @Expose
    private String metascore;
    @Expose
    private String originalTitle;
    @Expose
    private String plot;
    @Expose
    private Integer ranking;
    @Expose
    private String rated;
    @Expose
    private String rating;
    @Expose
    private String releaseDate;
    @Expose
    private List<String> runtime = new ArrayList<String>();
    @Expose
    private String simplePlot;
    @Expose
    private String title;
    @Expose
    private String urlIMDB;
    @Expose
    private String urlPoster;
    @Expose
    private String votes;
    @Expose
    private List<Writer> writers = new ArrayList<Writer>();
    @Expose
    private String year;

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
     * The directors
     */
    public List<Director> getDirectors() {
        return directors;
    }

    /**
     *
     * @param directors
     * The directors
     */
    public void setDirectors(List<Director> directors) {
        this.directors = directors;
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
     * The writers
     */
    public List<Writer> getWriters() {
        return writers;
    }

    /**
     *
     * @param writers
     * The writers
     */
    public void setWriters(List<Writer> writers) {
        this.writers = writers;
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

}
