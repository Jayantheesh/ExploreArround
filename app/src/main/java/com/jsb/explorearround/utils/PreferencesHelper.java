package com.jsb.explorearround.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by JSB on 11/12/15.
 */
public class PreferencesHelper {

    private static final String APP_PREFERENCES = "appPreferences";
    private static final String PREFERENCE_ADDRESS = APP_PREFERENCES + ".address";
    private static final String PREFERENCE_CITY_NAME = APP_PREFERENCES + ".city";
    private static final String PREFERENCE_LONGTITUDE = APP_PREFERENCES + ".latitude";
    private static final String PREFERENCE_LATITUDE = APP_PREFERENCES + ".longtitude";
    private static final String PREFERENCE_POSTALCODE = APP_PREFERENCES + ".postalcode";
    private static final String PREFERENCE_COUNTRY = APP_PREFERENCES + ".country";
    private static final String PREFERENCE_UNITS = APP_PREFERENCES + ".units";
    private static final String PREFERENCE_SEARCH_LOCATION = APP_PREFERENCES + ".searchLocation";

    private static PreferencesHelper sPreferences;
    private SharedPreferences mSharedPreferences = null;


    private PreferencesHelper() {
        //no instance
    }

    private PreferencesHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    /**
     * TODO need to think about what happens if this gets GCed along with the
     * Activity that initialized it. Do we lose ability to read Preferences in
     * further Activities? Maybe this should be stored in the Application
     * context.
     */
    public static synchronized PreferencesHelper getPreferences(Context context) {
        if (sPreferences == null) {
            sPreferences = new PreferencesHelper(context);
        }
        return sPreferences;
    }


    public void setCityname(String value) {
        mSharedPreferences.edit().putString(PREFERENCE_CITY_NAME,value).apply();
    }

    public String getCityname() {
        return mSharedPreferences.getString(PREFERENCE_CITY_NAME, null);
    }

    public void setAddress(String value) {
        mSharedPreferences.edit().putString(PREFERENCE_ADDRESS,value).apply();
    }

    public String getAddress() {
        return mSharedPreferences.getString(PREFERENCE_ADDRESS, null);
    }

    public void setCountryname(String value) {
        mSharedPreferences.edit().putString(PREFERENCE_COUNTRY,value).apply();
    }

    public String getCountryname() {
        return mSharedPreferences.getString(PREFERENCE_COUNTRY, null);
    }

    public void setPostalCode(String value) {
        mSharedPreferences.edit().putString(PREFERENCE_POSTALCODE,value).apply();
    }

    public String getPostalCode() {
        return mSharedPreferences.getString(PREFERENCE_POSTALCODE, null);
    }

    public void setLatitude(String value) {
        mSharedPreferences.edit().putString(PREFERENCE_LATITUDE, value).apply();
    }

    public String getLatitude() {
        return mSharedPreferences.getString(PREFERENCE_LATITUDE, null);
    }

    public void setLongtitude(String value) {
        mSharedPreferences.edit().putString(PREFERENCE_LONGTITUDE, value).apply();
    }

    public String getLongtitude() {
        return mSharedPreferences.getString(PREFERENCE_LONGTITUDE, null);
    }

    public void setUnits(int value) {
        mSharedPreferences.edit().putInt(PREFERENCE_UNITS, value).apply();
    }

    public int getUnits() {
        return mSharedPreferences.getInt(PREFERENCE_UNITS, 0);
    }

    public void setSearchLocation(String value) {
        mSharedPreferences.edit().putString(PREFERENCE_SEARCH_LOCATION, value).apply();
    }

    public String getSearchLocation() {
        return mSharedPreferences.getString(PREFERENCE_SEARCH_LOCATION, "Current Location");
    }
}
