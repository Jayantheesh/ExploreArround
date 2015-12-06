package com.jsb.explorearround.parser;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by JSB on 11/14/15.
 */
@Parcel
public class Reviews {

    @SerializedName("aspects")
    private Aspects[] aspects;

    @SerializedName("author_name")
    private String author_name;

    @SerializedName("author_url")
    private String author_url;

    @SerializedName("language")
    private String language;

    @SerializedName("profile_photo_url")
    private String profile_photo_url;

    @SerializedName("rating")
    private String rating;

    @SerializedName("text")
    private String text;

    @SerializedName("time")
    private String time;

    public Aspects[] getAspects() {
        return aspects;
    }

    public void setAspects(Aspects[] aspects) {
        this.aspects = aspects;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_url() {
        return author_url;
    }

    public void setAuthor_url(String author_url) {
        this.author_url = author_url;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getProfile_photo_url() {
        return profile_photo_url;
    }

    public void setProfile_photo_url(String profile_photo_url) {
        this.profile_photo_url = profile_photo_url;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
