package com.jsb.explorearround.location;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.jsb.explorearround.R;
import com.jsb.explorearround.utils.PreferencesHelper;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

/**
 * Created by JSB on 10/18/15.
 */
public class LocationTracker extends Service implements OnLocationUpdatedListener {

    // Get Class Name
    private static String TAG = "LocationTracker";

    private final Context mContext;
    private Location mLocation;
    private double mLatitude;
    private double mLongitude;
    private static LocationTracker sInstance;

    // How many Geocoder should return our GPSTracker
    private int mGeocoderMaxResults = 1;

    protected LocationTracker(Context context) {
        mContext = context;
    }

    /**
     * Gets or creates the singleton instance of LocationTracker
     *
     * @param _context
     * @return
     */
    public synchronized static LocationTracker getsInstance(Context _context) {
        if (sInstance == null) {
            sInstance = new LocationTracker(_context);
        }
        return sInstance;
    }

    public synchronized static LocationTracker getInstance() {
        return sInstance;
    }


    /**
     * Try to get my current location by GPS or Network Provider
     */
    public void getLocation() {
        SmartLocation.with(mContext).location().oneFix(). start(this);
    }

    /**
     * Update GPSTracker latitude and longitude
     */
    private void updateGPSCoordinates() {
        if (mLocation != null) {
            mLatitude = mLocation.getLatitude();
            mLongitude = mLocation.getLongitude();
        }
    }

    /**
     * Update GPSTracker latitude and longitude
     */
    private void updateGPSCoordinates(double latitude, double longtitude) {
        mLatitude = latitude;
        mLongitude = longtitude;
    }

    /**
     * GPSTracker latitude getter and setter
     * @return latitude
     */
    private double getLatitude() {
        if (mLocation != null) {
            mLatitude = mLocation.getLatitude();
        }
        return mLatitude;
    }

    /**
     * GPSTracker longitude getter and setter
     * @return
     */
    private double getLongitude() {
        if (mLocation != null) {
            mLongitude = mLocation.getLongitude();
        }
        return mLongitude;
    }

    /**
     * Get list of address by latitude and longitude
     * @return null or List<Address>
     */
    private List<Address> getGeocoderAddress(Context context) {
        if (mLocation != null) {
            Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
            try {
                /**
                 * Geocoder.getFromLocation - Returns an array of Addresses 
                 * that are known to describe the area immediately surrounding the given latitude and longitude.
                 */
                List<Address> addresses = geocoder.getFromLocation(mLatitude, mLongitude, this.mGeocoderMaxResults);
                return addresses;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Impossible to connect to Geocoder", e);
            }
        }
        return null;
    }

    /**
     * Try to get AddressLine
     * @return null or addressLine
     */
    private String getAddressLine(Context context) {
        List<Address> addresses = getGeocoderAddress(context);
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String addressLine = address.getAddressLine(0);
            return addressLine;
        } else {
            return null;
        }
    }

    /**
     * Try to get Locality
     * @return null or locality
     */
    private String getLocality(Context context) {
        List<Address> addresses = getGeocoderAddress(context);
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String locality = address.getLocality();
            return locality;
        } else {
            return null;
        }
    }

    /**
     * Try to get Postal Code
     * @return null or postalCode
     */
    private String getPostalCode(Context context) {
        List<Address> addresses = getGeocoderAddress(context);
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String postalCode = address.getPostalCode();
            return postalCode;
        } else {
            return null;
        }
    }

    /**
     * Try to get CountryName
     * @return null or postalCode
     */
    private String getCountryName(Context context) {
        List<Address> addresses = getGeocoderAddress(context);
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String countryName = address.getCountryName();
            return countryName;
        } else {
            return null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public boolean isLocationEnabled(final Context context) {
        boolean gps_enabled = SmartLocation.with(context).location().state().isGpsAvailable();
        boolean network_enabled = SmartLocation.with(context).location().state().isNetworkAvailable();
        if (gps_enabled || network_enabled) {
            return true;
        } else if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(context.getResources().getString(R.string.open_location_settings),
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                }
            });
            dialog.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                }
            });
            dialog.show();
        }
        return false;
    }

    @Override
    public void onLocationUpdated(Location loc) {
        mLocation = loc;
        updateGPSCoordinates();
//        Toast.makeText(mContext, "Status Lat=" + getLatitude()
//                + " Long=" + getLongitude(), Toast.LENGTH_SHORT).show();

        if (getLatitude() == 0.0 && getLongitude() == 0.0) {
            Log.e(TAG, "No location information found");
            return;
        }
        PreferencesHelper preference = PreferencesHelper.getPreferences(mContext);
        preference.setAddress(getAddressLine(mContext));
        preference.setCityname(getLocality(mContext));
        preference.setCountryname(getCountryName(mContext));
        preference.setLatitude(String.valueOf(getLatitude()));
        preference.setLongtitude(String.valueOf(getLongitude()));
        preference.setPostalCode(getPostalCode(mContext));

        //Stop the location updates
        SmartLocation.with(mContext).location().stop();
    }

    /**
     * updateLocationPref - Update the preference with latest value
     * @param latitude
     * @param longtitude
     */
    public void updateLocationPref(String location, String latitude, String longtitude) {
        Log.d(TAG, "updateLocationPref");
        if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longtitude)) {
            Log.e(TAG, "Long/Lat values null");
            return;
        }
        updateGPSCoordinates(Double.valueOf(latitude), Double.valueOf(longtitude));
        PreferencesHelper preference = PreferencesHelper.getPreferences(mContext);
        preference.setAddress(getAddressLine(mContext));
        preference.setCityname(getLocality(mContext));
        preference.setCountryname(getCountryName(mContext));
        preference.setLatitude(latitude);
        preference.setLongtitude(longtitude);
        preference.setPostalCode(getPostalCode(mContext));
        preference.setSearchLocation(location);
    }
}