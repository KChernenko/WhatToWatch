package me.bitfrom.whattowatch.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FilmModel implements Parcelable {

    @SerializedName("title")
    public String title;
    @SerializedName("year")
    public String year;
    @SerializedName("directors")
    public String directors;
    @SerializedName("writers")
    public String writers;
    @SerializedName("runtime")
    public String runtime;
    @SerializedName("urlPoster")
    public String urlPoster;
    @SerializedName("countries")
    public String countries;
    @SerializedName("genres")
    public String genres;
    @SerializedName("plot")
    public String plot;
    @SerializedName("idIMDB")
    public String idIMDB;
    @SerializedName("urlIMDB")
    public String urlIMDB;
    @SerializedName("rating")
    public String rating;
    public String favorite;

    public FilmModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.year);
        dest.writeString(this.directors);
        dest.writeString(this.writers);
        dest.writeString(this.runtime);
        dest.writeString(this.urlPoster);
        dest.writeString(this.countries);
        dest.writeString(this.genres);
        dest.writeString(this.plot);
        dest.writeString(this.idIMDB);
        dest.writeString(this.urlIMDB);
        dest.writeString(this.favorite);
        dest.writeString(this.rating);
    }

    protected FilmModel(Parcel in) {
        this.title = in.readString();
        this.year = in.readString();
        this.directors = in.readString();
        this.writers = in.readString();
        this.runtime = in.readString();
        this.urlPoster = in.readString();
        this.countries = in.readString();
        this.genres = in.readString();
        this.plot = in.readString();
        this.idIMDB = in.readString();
        this.urlIMDB = in.readString();
        this.favorite = in.readString();
        this.rating = in.readString();
    }

    public static final Creator<FilmModel> CREATOR = new Creator<FilmModel>() {
        public FilmModel createFromParcel(Parcel source) {
            return new FilmModel(source);
        }

        public FilmModel[] newArray(int size) {
            return new FilmModel[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilmModel filmModel = (FilmModel) o;

        if (!title.equals(filmModel.title)) return false;
        if (!year.equals(filmModel.year)) return false;
        if (!directors.equals(filmModel.directors)) return false;
        if (!writers.equals(filmModel.writers)) return false;
        if (!runtime.equals(filmModel.runtime)) return false;
        if (!urlPoster.equals(filmModel.urlPoster)) return false;
        if (!countries.equals(filmModel.countries)) return false;
        if (!genres.equals(filmModel.genres)) return false;
        if (!plot.equals(filmModel.plot)) return false;
        if (!idIMDB.equals(filmModel.idIMDB)) return false;
        if (!favorite.equals(filmModel.favorite)) return false;
        if (!urlIMDB.equals(filmModel.urlIMDB)) return false;

        return rating.equals(filmModel.rating);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + year.hashCode();
        result = 31 * result + directors.hashCode();
        result = 31 * result + writers.hashCode();
        result = 31 * result + runtime.hashCode();
        result = 31 * result + urlPoster.hashCode();
        result = 31 * result + countries.hashCode();
        result = 31 * result + genres.hashCode();
        result = 31 * result + plot.hashCode();
        result = 31 * result + idIMDB.hashCode();
        result = 31 * result + urlIMDB.hashCode();
        result = 31 * result + favorite.hashCode();
        result = 31 * result + rating.hashCode();

        return result;
    }

}
