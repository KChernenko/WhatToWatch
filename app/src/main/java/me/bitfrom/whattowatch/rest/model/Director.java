package me.bitfrom.whattowatch.rest.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Constantine with love.
 */

public class Director implements Parcelable {

    @SerializedName("name")
    private String name;
    @SerializedName("nameId")
    private String nameId;

    public Director(String name, String nameId) {
        this.name = name;
        this.nameId = nameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.nameId);
    }

    protected Director(Parcel in) {
        this.name = in.readString();
        this.nameId = in.readString();
    }

    public static final Parcelable.Creator<Director> CREATOR = new Parcelable.Creator<Director>() {
        public Director createFromParcel(Parcel source) {
            return new Director(source);
        }

        public Director[] newArray(int size) {
            return new Director[size];
        }
    };
}