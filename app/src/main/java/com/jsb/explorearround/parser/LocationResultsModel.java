package com.jsb.explorearround.parser;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by JSB on 12/5/15.
 */
@Parcel
public class LocationResultsModel {

    @SerializedName("results")
    private LocationResults[] results;

    @SerializedName("status")
    private String status;

    public LocationResults[] getResults() {
        return results;
    }

    public void setResults(LocationResults[] results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
