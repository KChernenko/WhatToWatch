package me.bitfrom.whattowatch.core.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class TheaterModel {

    @JsonField(name = "data")
    private DataTheaterModel data;
    @JsonField(name = "about")
    private AboutModel about;

    public DataTheaterModel getData() {
        return data;
    }

    public void setData(DataTheaterModel data) {
        this.data = data;
    }

    public AboutModel getAbout() {
        return about;
    }

    public void setAbout(AboutModel about) {
        this.about = about;
    }
}