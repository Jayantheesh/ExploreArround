package com.jsb.explorearround.parser;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by JSB on 11/14/15.
 */
@Parcel
public class OpeningHours {

    @SerializedName("open_now")
    private boolean open_now;

    @SerializedName("periods")
    private Periods[] periods;

    @SerializedName("weekday_text")
    private String[] weekday_text;

    public boolean isOpen_now() {
        return open_now;
    }

    public void setOpen_now(boolean open_now) {
        this.open_now = open_now;
    }

    public Periods[] getPeriods() {
        return periods;
    }

    public void setPeriods(Periods[] periods) {
        this.periods = periods;
    }

    public String[] getWeekday_text() {
        return weekday_text;
    }

    public void setWeekday_text(String[] weekday_text) {
        this.weekday_text = weekday_text;
    }
}
