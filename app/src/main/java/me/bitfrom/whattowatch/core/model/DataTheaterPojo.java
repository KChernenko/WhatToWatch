package me.bitfrom.whattowatch.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class DataTheaterPojo {

    @SerializedName("inTheaters")
    @Expose
    private List<InTheaterPojo> inTheaters = new ArrayList<>();

    /**
     * @return
     * The inTheaters
     **/
    public List<InTheaterPojo> getInTheaters() {
        return inTheaters;
    }

    /**
     * @param inTheaters
     * The inTheaters
     **/
    public void setInTheaters(List<InTheaterPojo> inTheaters) {
        this.inTheaters = inTheaters;
    }
}
