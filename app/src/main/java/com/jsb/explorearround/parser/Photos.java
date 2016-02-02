package com.jsb.explorearround.parser;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Created by JSB on 11/7/15.
 */
@Parcel
public class Photos implements Serializable {
    @SerializedName("height")
    private String height;

    @SerializedName("width")
    private String width;

    @SerializedName("photo_reference")
    private String photo_reference;

    @SerializedName("html_attributions")
    private String[] photoUrl;

    public String[] getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String[] photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getPhoto_reference() {
        return photo_reference;
    }

    public void setPhoto_reference(String photo_reference) {
        this.photo_reference = photo_reference;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }
}
