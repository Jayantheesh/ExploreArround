package com.jsb.explorearround.parser;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by JSB on 10/18/15.
 */
@Parcel
public class Model {

    //@SerializedName("html_attributions")
    //private String html_attributions;

    @SerializedName("next_page_token")
    private String next_page_token;

    @SerializedName("results")
    private Results[] results;

    @SerializedName("status")
    private String status;

//    public String getHtml_attributions() {
//        return html_attributions;
//    }
//
//    public void setHtml_attributions(String html_attributions) {
//        this.html_attributions = html_attributions;
//    }

    public Results[] getResults() {

        return results;
    }

    public void setResults(Results[] results) {

        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNext_page_token() {
        return next_page_token;
    }

    public void setNext_page_token(String next_page_token) {
        this.next_page_token = next_page_token;
    }

}
