package com.jsb.explorearround.ui;

import android.content.Context;

/**
 * Created by JSB on 1/13/16.
 */
public class LocationSearchDataObject {
    private String mName;

    public LocationSearchDataObject(String name) {
        mName = name;
    }

    public String getmName() {
        return mName;
    }

    public static class DataBuilder {
        Context context;
        String name;

        public DataBuilder(Context activity) {
            this.context = activity;
        }

        public DataBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public LocationSearchDataObject build() {
            return new LocationSearchDataObject(name);
        }
    }
}
