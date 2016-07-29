package me.bitfrom.whattowatch.core.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;

@JsonObject
public class DataTheaterPojo {

    @JsonField
    private List<InTheaterPojo> inTheaters = new ArrayList<>();

    public List<InTheaterPojo> getInTheaters() {
        return inTheaters;
    }

    public void setInTheaters(List<InTheaterPojo> inTheaters) {
        this.inTheaters = inTheaters;
    }
}