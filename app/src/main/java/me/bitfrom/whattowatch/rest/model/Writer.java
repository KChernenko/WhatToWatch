package me.bitfrom.whattowatch.rest.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Constantine with love.
 */
public class Writer implements Parcelable {

    @SerializedName("name")
    private String name;

    @SerializedName("nameId")
    private String nameId;

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

    protected Writer(Parcel in) {
        name = in.readString();
        nameId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(nameId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Writer> CREATOR = new Parcelable.Creator<Writer>() {
        @Override
        public Writer createFromParcel(Parcel in) {
            return new Writer(in);
        }

        @Override
        public Writer[] newArray(int size) {
            return new Writer[size];
        }
    };
}
