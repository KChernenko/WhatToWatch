package me.bitfrom.whattowatch.rest.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Constantine with love.
 */
public class Movie implements Parcelable {

    @SerializedName("countries")
    private List<String> countries = new ArrayList<String>();
    @SerializedName("directors")
    private List<Director> directors = new ArrayList<Director>();
    @SerializedName("filmingLocations")
    private List<Object> filmingLocations = new ArrayList<Object>();
    @SerializedName("genres")
    private List<String> genres = new ArrayList<String>();
    @SerializedName("idIMDB")
    private String idIMDB;
    @SerializedName("languages")
    private List<String> languages = new ArrayList<String>();
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
    private List<String> runtime = new ArrayList<String>();
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
    private List<Writer> writers = new ArrayList<Writer>();
    @SerializedName("year")
    private String year;

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

    public List<Object> getFilmingLocations() {
        return filmingLocations;
    }

    public void setFilmingLocations(List<Object> filmingLocations) {
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

    protected Movie(Parcel in) {
        if (in.readByte() == 0x01) {
            countries = new ArrayList<String>();
            in.readList(countries, String.class.getClassLoader());
        } else {
            countries = null;
        }
        if (in.readByte() == 0x01) {
            directors = new ArrayList<Director>();
            in.readList(directors, Director.class.getClassLoader());
        } else {
            directors = null;
        }
        if (in.readByte() == 0x01) {
            filmingLocations = new ArrayList<Object>();
            in.readList(filmingLocations, Object.class.getClassLoader());
        } else {
            filmingLocations = null;
        }
        if (in.readByte() == 0x01) {
            genres = new ArrayList<String>();
            in.readList(genres, String.class.getClassLoader());
        } else {
            genres = null;
        }
        idIMDB = in.readString();
        if (in.readByte() == 0x01) {
            languages = new ArrayList<String>();
            in.readList(languages, String.class.getClassLoader());
        } else {
            languages = null;
        }
        metascore = in.readString();
        originalTitle = in.readString();
        plot = in.readString();
        ranking = in.readByte() == 0x00 ? null : in.readInt();
        rated = in.readString();
        rating = in.readString();
        releaseDate = in.readString();
        if (in.readByte() == 0x01) {
            runtime = new ArrayList<String>();
            in.readList(runtime, String.class.getClassLoader());
        } else {
            runtime = null;
        }
        simplePlot = in.readString();
        title = in.readString();
        urlIMDB = in.readString();
        urlPoster = in.readString();
        votes = in.readString();
        if (in.readByte() == 0x01) {
            writers = new ArrayList<Writer>();
            in.readList(writers, Writer.class.getClassLoader());
        } else {
            writers = null;
        }
        year = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (countries == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(countries);
        }
        if (directors == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(directors);
        }
        if (filmingLocations == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(filmingLocations);
        }
        if (genres == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(genres);
        }
        dest.writeString(idIMDB);
        if (languages == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(languages);
        }
        dest.writeString(metascore);
        dest.writeString(originalTitle);
        dest.writeString(plot);
        if (ranking == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(ranking);
        }
        dest.writeString(rated);
        dest.writeString(rating);
        dest.writeString(releaseDate);
        if (runtime == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(runtime);
        }
        dest.writeString(simplePlot);
        dest.writeString(title);
        dest.writeString(urlIMDB);
        dest.writeString(urlPoster);
        dest.writeString(votes);
        if (writers == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(writers);
        }
        dest.writeString(year);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
