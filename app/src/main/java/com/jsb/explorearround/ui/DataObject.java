package com.jsb.explorearround.ui;

import android.content.Context;

import com.jsb.explorearround.parser.Photos;

/**
 * Created by JSB on 10/9/15.
 */
public class DataObject {

    private String mName;
    private String icon;
    private String mAddress;
    private String mStatus;
    private String mDistance;
    private Photos[] photos;
    private String rating;

    public DataObject(String name, String icon, String address, String status, String distance, Photos[] photos, String ratingValue) {
        mName = name;
        icon = icon;
        mAddress = address;
        mStatus = status;
        mDistance = distance;
        this.photos = photos;
        rating = ratingValue;
    }

    public String getmAddress() {
        return mAddress;
    }

    public String getIcon() {
        return icon;
    }

    public String getmDistance() {
        return mDistance;
    }

    public String getmName() {
        return mName;
    }

    public String getmStatus() {
        return mStatus;
    }

    public Photos[] getPhotos() {
        return photos;
    }

    public String getRating() {
        return rating;
    }

    public static class DataBuilder {
        Context context;
        String name;
        String icon;
        String address;
        String status;
        String distance;
        Photos[] photoUrl;
        String rating;

        public DataBuilder(Context activity) {
            this.context = activity;
        }

        public DataBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public DataBuilder setIcon(String icon) {
            this.icon = icon;
            return this;
        }

        public DataBuilder setAddress(String address) {
            this.address = address;
            return this;
        }

        public DataBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public DataBuilder setDistance(String distance) {
            this.distance = distance;
            return this;
        }

        public DataBuilder setPhotos(Photos[] photoUrl) {
            this.photoUrl = photoUrl;
            return this;
        }

        public DataBuilder setRating(String rating) {
            this.rating = rating;
            return this;
        }

        public DataObject build() {
            return new DataObject(name, icon, address, status, distance, photoUrl, rating);
        }
    }
}
