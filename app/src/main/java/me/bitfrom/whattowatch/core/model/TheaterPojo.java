package me.bitfrom.whattowatch.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TheaterPojo {

    @SerializedName("data")
    @Expose
    private DataTheaterPojo data;
    @SerializedName("about")
    @Expose
    private AboutPojo about;

    /**
     * @return
     * The data
     **/
    public DataTheaterPojo getData() {
        return data;
    }

    /**
     * @param data
     * The data
     **/
    public void setData(DataTheaterPojo data) {
        this.data = data;
    }

    /**
     *
     * @return
     * The about
     **/
    public AboutPojo getAbout() {
        return about;
    }

    /**
     * @param about
     * The about
     **/
    public void setAbout(AboutPojo about) {
        this.about = about;
    }
}
