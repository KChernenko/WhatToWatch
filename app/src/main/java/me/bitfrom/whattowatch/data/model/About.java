package me.bitfrom.whattowatch.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class About {

    @SerializedName("version")
    @Expose
    private String version;

    /**
     *
     * @return
     * The version
     */
    public String getVersion() {
        return version;
    }

    /**
     *
     * @param version
     * The version
     */
    public void setVersion(String version) {
        this.version = version;
    }
}
