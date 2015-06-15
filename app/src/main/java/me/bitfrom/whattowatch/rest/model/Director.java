package me.bitfrom.whattowatch.rest.model;


import com.google.gson.annotations.Expose;

/**
 * Created by Constantine with love.
 */

public class Director {

    @Expose
    private String name;
    @Expose
    private String nameId;

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The nameId
     */
    public String getNameId() {
        return nameId;
    }

    /**
     * @param nameId The nameId
     */
    public void setNameId(String nameId) {
        this.nameId = nameId;
    }
}