package com.jsb.explorearround.parser;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by JSB on 10/20/15.
 */
@Parcel
public class OpenHours {
    @SerializedName("open_now")
    private boolean open_now;

    public boolean getOpen_now() {
        return open_now;
    }

    public void setOpen_now(boolean open_now) {
        this.open_now = open_now;
    }
}
