package me.bitfrom.whattowatch.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilmPojo {

    @SerializedName("data")
    @Expose
    private DataPojo data;
    @SerializedName("about")
    @Expose
    private AboutPojo about;

    /**
     * @return
     * The data
     **/
    public DataPojo getData() {
        return data;
    }

    /**
     * @param data
     * The data
     **/
    public void setData(DataPojo data) {
        this.data = data;
    }

    /**
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
