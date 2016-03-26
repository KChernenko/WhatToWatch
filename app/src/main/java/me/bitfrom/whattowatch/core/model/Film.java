package me.bitfrom.whattowatch.core.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Film implements Parcelable {

    public abstract String title();
    public abstract String year();
    public abstract String directors();
    public abstract String writers();
    public abstract String runtime();
    public abstract String urlPoster();
    public abstract String countries();
    public abstract String genres();
    public abstract String plot();
    public abstract String idIMDB();
    public abstract String urlIMDB();
    public abstract String rating();
    public abstract String favorite();

    public static Builder builder() {return new AutoValue_Film.Builder();}

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder title(String title);

        public abstract Builder year(String year);

        public abstract Builder directors(String directors);

        public abstract Builder writers(String writers);

        public abstract Builder runtime(String runtime);

        public abstract Builder urlPoster(String urlPoster);

        public abstract Builder countries(String countries);

        public abstract Builder genres(String genres);

        public abstract Builder plot(String plot);

        public abstract Builder idIMDB(String idIMBD);

        public abstract Builder urlIMDB(String urlIMDB);

        public abstract Builder rating(String rating);

        public abstract Builder favorite(String favorite);

        public abstract Film autoBuild();

        public Film build() {return autoBuild();}
    }

}
