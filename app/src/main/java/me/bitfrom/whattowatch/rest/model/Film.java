package me.bitfrom.whattowatch.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Film {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("about")
    @Expose
    private About about;

    /**
     *
     * @return
     * The data
     */
    public Data getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(Data data) {
        this.data = data;
    }

    /**
     *
     * @return
     * The about
     */
    public About getAbout() {
        return about;
    }

    /**
     *
     * @param about
     * The about
     */
    public void setAbout(About about) {
        this.about = about;
    }

}
