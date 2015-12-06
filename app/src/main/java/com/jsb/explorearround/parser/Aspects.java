package com.jsb.explorearround.parser;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by JSB on 11/14/15.
 */
@Parcel
public class Aspects {

    @SerializedName("rating")
    private String rating;

    @SerializedName("type")
    private String type;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
