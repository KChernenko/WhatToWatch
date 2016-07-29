package me.bitfrom.whattowatch.core.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class FilmPojo {

    @JsonField
    private DataPojo data;
    @JsonField
    private AboutPojo about;

    public DataPojo getData() {
        return data;
    }

    public void setData(DataPojo data) {
        this.data = data;
    }

    public AboutPojo getAbout() {
        return about;
    }

    public void setAbout(AboutPojo about) {
        this.about = about;
    }

}