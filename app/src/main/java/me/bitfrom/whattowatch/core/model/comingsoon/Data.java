package me.bitfrom.whattowatch.core.model.comingsoon;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;

@JsonObject
public class Data {

    @JsonField
    private List<ComingSoon> comingSoon = new ArrayList<>();

    public List<ComingSoon> getComingSoon() {
        return comingSoon;
    }

    public void setComingSoon(List<ComingSoon> comingSoon) {
        this.comingSoon = comingSoon;
    }

}