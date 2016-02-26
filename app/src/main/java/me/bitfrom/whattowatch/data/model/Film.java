package me.bitfrom.whattowatch.data.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Film implements Parcelable {

    public String title;
    public String year;
    public String directors;
    public String writers;
    public String runtime;
    public String urlPoster;
    public String countries;
    public String genres;
    public String plot;
    public String idIMDB;
    public String urlIMDB;
    public String rating;
    public String favorite;

    public Film() {
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

    protected Film(Parcel in) {
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

    public static final Creator<Film> CREATOR = new Creator<Film>() {
        public Film createFromParcel(Parcel source) {
            return new Film(source);
        }

        public Film[] newArray(int size) {
            return new Film[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Film film = (Film) o;

        if (!title.equals(film.title)) return false;
        if (!year.equals(film.year)) return false;
        if (!directors.equals(film.directors)) return false;
        if (!writers.equals(film.writers)) return false;
        if (!runtime.equals(film.runtime)) return false;
        if (!urlPoster.equals(film.urlPoster)) return false;
        if (!countries.equals(film.countries)) return false;
        if (!genres.equals(film.genres)) return false;
        if (!plot.equals(film.plot)) return false;
        if (!idIMDB.equals(film.idIMDB)) return false;
        if (!favorite.equals(film.favorite)) return false;
        if (!urlIMDB.equals(film.urlIMDB)) return false;

        return rating.equals(film.rating);
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
