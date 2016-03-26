package me.bitfrom.whattowatch.core.model;

import com.google.gson.annotations.SerializedName;

public class DirectorPojo {

    @SerializedName("name")
    private String name;
    @SerializedName("nameId")
    private String nameId;

    /**
     * @return
     * The name
     **/
    public String getName() {
        return name;
    }

    /**
     * @param name
     * The name
     **/
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return
     * The nameId
     **/
    public String getNameId() {
        return nameId;
    }

    /**
     * @param nameId
     * The nameId
     **/
    public void setNameId(String nameId) {
        this.nameId = nameId;
    }
}
