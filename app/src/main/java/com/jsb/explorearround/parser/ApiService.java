package com.jsb.explorearround.parser;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by JSB on 10/18/15.
 */
public interface ApiService {


    //@GET("/maps/api/place/nearbysearch/json?location=40.5488035,-74.74311260000002&radius=5000&types=food&key=AIzaSyC9OpeSXAjldUge51N0PPjg5DajOMGNTog")
    @GET("/maps/api/place/nearbysearch/json")
    public Model getSearchResults(@Query("location") String location,
                                       //@Query("radius") String radius,
                                       @Query("types") String food,
                                       @Query("keyword") String keyword,
                                       @Query("key") String key,
                                       //@Query("opennow") String open,
                                       @Query("rankby") String distance);

    @GET("/maps/api/place/nearbysearch/json")
    public Model getSearchResults(@Query("pagetoken") String place_token, @Query("key") String key);

    @GET("/maps/api/place/details/json")
    public PlaceDetailsModel getPlaceDetails(@Query("placeid") String place_id,
                            @Query("key") String key);

    @GET("/maps/api/place/photo")
    public PlaceDetailsModel getPhotoDetails(@Query("maxwidth") String maxwidth,
                                             @Query("photoreference") String photoreference,
                                             @Query("key") String key);

    //@GET("/maps.googleapis.com/maps/api/geocode/json?address=srimushnam&sensor=true")
    @GET("/maps/api/geocode/json")
    public LocationResultsModel getLocationDetails(@Query("address") String address,
                                                   @Query("sensor") String sensor);
}
