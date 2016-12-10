package me.bitfrom.whattowatch.core.model.comingsoon;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import me.bitfrom.whattowatch.core.model.AboutModel;

@JsonObject
public class NewMoviesModel {

    @JsonField(name = "data")
    private ComingSoonDataModel comingSoonDataModel;
    @JsonField(name = "about")
    private AboutModel about;

    public ComingSoonDataModel getComingSoonDataModel() {
        return comingSoonDataModel;
    }

    public void setComingSoonDataModel(ComingSoonDataModel comingSoonDataModel) {
        this.comingSoonDataModel = comingSoonDataModel;
    }

    public AboutModel getAbout() {
        return about;
    }

    public void setAbout(AboutModel about) {
        this.about = about;
    }
}