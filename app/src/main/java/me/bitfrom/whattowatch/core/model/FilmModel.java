package me.bitfrom.whattowatch.core.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class FilmModel {

    @JsonField(name = "data")
    private DataModel data;
    @JsonField(name = "about")
    private AboutModel about;

    public DataModel getData() {
        return data;
    }

    public void setData(DataModel data) {
        this.data = data;
    }

    public AboutModel getAbout() {
        return about;
    }

    public void setAbout(AboutModel about) {
        this.about = about;
    }

}