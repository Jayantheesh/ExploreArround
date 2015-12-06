package com.jsb.explorearround.parser;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by JSB on 10/26/15.
 */
@Parcel
public class Result {

    @SerializedName("geometry")
    private Geometry geometry;

    @SerializedName("international_phone_number")
    private String international_phone_number;

    @SerializedName("name")
    private String name;

    @SerializedName("rating")
    private String rating;

    @SerializedName("vicinity")
    private String vicinity;

    @SerializedName("website")
    private String website;

    @SerializedName("url")
    private String url;

    @SerializedName("scope")
    private String scope;

    @SerializedName("reviews")
    private Reviews[] reviews;

    @SerializedName("opening_hours")
    private OpeningHours opening_hours;

    @SerializedName("photos")
    private Photos[] photos;

    public Photos[] getPhotos() {
        return photos;
    }

    public void setPhotos(Photos[] photos) {
        this.photos = photos;
    }

    public OpeningHours getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(OpeningHours opening_hours) {
        this.opening_hours = opening_hours;
    }

    public Reviews[] getReviews() {
        return reviews;
    }

    public void setReviews(Reviews[] reviews) {
        this.reviews = reviews;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInternational_phone_number() {
        return international_phone_number;
    }

    public void setInternational_phone_number(String international_phone_number) {
        this.international_phone_number = international_phone_number;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
