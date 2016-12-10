package me.bitfrom.whattowatch.core.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class AboutModel {

    @JsonField(name = "version")
    private String version;
    @JsonField(name = "serverTime")
    private String serverTime;

    public String getVersion() {
        return version;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }
}