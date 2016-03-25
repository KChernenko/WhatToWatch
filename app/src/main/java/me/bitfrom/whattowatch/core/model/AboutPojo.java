package me.bitfrom.whattowatch.core.model;

import com.google.gson.annotations.SerializedName;

public class AboutPojo {

    @SerializedName("version")
    private String version;

    /**
     * @return
     * The version
     **/
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     * The version
     **/
    public void setVersion(String version) {
        this.version = version;
    }
}
