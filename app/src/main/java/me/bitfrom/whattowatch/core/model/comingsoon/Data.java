package me.bitfrom.whattowatch.core.model.comingsoon;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Data {

    @SerializedName("comingSoon")
    private List<ComingSoon> comingSoon = new ArrayList<>();

    /**
     *
     * @return
     * The comingSoon
     */
    public List<ComingSoon> getComingSoon() {
        return comingSoon;
    }

    /**
     *
     * @param comingSoon
     * The comingSoon
     */
    public void setComingSoon(List<ComingSoon> comingSoon) {
        this.comingSoon = comingSoon;
    }

}
