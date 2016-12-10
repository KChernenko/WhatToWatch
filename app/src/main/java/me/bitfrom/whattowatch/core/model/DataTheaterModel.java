package me.bitfrom.whattowatch.core.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;

@JsonObject
public class DataTheaterModel {

    @JsonField(name = "inTheaters")
    private List<InTheaterModel> inTheaters = new ArrayList<>();

    public List<InTheaterModel> getInTheaters() {
        return inTheaters;
    }

    public void setInTheaters(List<InTheaterModel> inTheaters) {
        this.inTheaters = inTheaters;
    }
}