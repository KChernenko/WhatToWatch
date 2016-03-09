package me.bitfrom.whattowatch.core.model.comingsoon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import me.bitfrom.whattowatch.core.model.AboutPojo;

public class NewMoviesPojo {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("about")
    @Expose
    private AboutPojo about;

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
    public AboutPojo getAbout() {
        return about;
    }

    /**
     *
     * @param about
     * The about
     */
    public void setAbout(AboutPojo about) {
        this.about = about;
    }
}
