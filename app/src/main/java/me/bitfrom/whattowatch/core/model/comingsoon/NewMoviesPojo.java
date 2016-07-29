package me.bitfrom.whattowatch.core.model.comingsoon;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import me.bitfrom.whattowatch.core.model.AboutPojo;

@JsonObject
public class NewMoviesPojo {

    @JsonField
    private Data data;
    @JsonField
    private AboutPojo about;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public AboutPojo getAbout() {
        return about;
    }

    public void setAbout(AboutPojo about) {
        this.about = about;
    }
}