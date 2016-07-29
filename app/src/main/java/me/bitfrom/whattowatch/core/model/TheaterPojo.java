package me.bitfrom.whattowatch.core.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class TheaterPojo {

    @JsonField
    private DataTheaterPojo data;
    @JsonField
    private AboutPojo about;

    public DataTheaterPojo getData() {
        return data;
    }

    public void setData(DataTheaterPojo data) {
        this.data = data;
    }

    public AboutPojo getAbout() {
        return about;
    }

    public void setAbout(AboutPojo about) {
        this.about = about;
    }
}