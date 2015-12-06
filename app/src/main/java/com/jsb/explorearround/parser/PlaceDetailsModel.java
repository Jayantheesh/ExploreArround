package com.jsb.explorearround.parser;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by JSB on 10/26/15.
 */
@Parcel
public class PlaceDetailsModel {

    //@SerializedName("html_attributions")
    //private String html_attributions;

    @SerializedName("result")
    private Result result;

    @SerializedName("status")
    private String status;

//    public String getHtml_attributions() {
//        return html_attributions;
//    }
//
//    public void setHtml_attributions(String html_attributions) {
//        this.html_attributions = html_attributions;
//    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
