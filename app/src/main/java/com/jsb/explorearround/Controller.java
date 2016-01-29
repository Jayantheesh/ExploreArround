package com.jsb.explorearround;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;

import com.jsb.explorearround.network.Connectivity;
import com.jsb.explorearround.parser.ApiService;
import com.jsb.explorearround.parser.LocationResults;
import com.jsb.explorearround.parser.LocationResultsModel;
import com.jsb.explorearround.parser.Model;
import com.jsb.explorearround.parser.PlaceDetailsModel;
import com.jsb.explorearround.parser.Results;
import com.jsb.explorearround.utils.AppConstants;
import com.jsb.explorearround.utils.PreferencesHelper;

import java.util.HashSet;

import retrofit.RestAdapter;

/**
 * Created by JSB on 10/7/15.
 */
public class Controller {

    private static final String TAG = "Controller";
    public static final String BASE_URL = "https://maps.googleapis.com";
    private static Controller sInstance;
    private final Context mContext;
    private final HashSet<Result> mListeners = new HashSet<Result>();


    protected Controller(Context context) {
        mContext = context;

    }

    /**
     * Gets or creates the singleton instance of Controller
     *
     * @param _context
     * @return
     */
    public synchronized static Controller getsInstance(Context _context) {
        if (sInstance == null) {
            sInstance = new Controller(_context);
        }
        return sInstance;
    }

    public synchronized static Controller getInstance() {
        return sInstance;
    }

    /**
     * Any UI code that wishes for callback results (on async ops) should
     * register their callback here (typically from onResume()). Unregistered
     * callbacks will never be called, to prevent problems when the command
     * completes and the activity has already paused or finished.
     *
     * @param listener The callback that may be used in action methods
     */
    public void addResultCallback(Result listener) {
        synchronized (mListeners) {
            listener.setRegistered(true);
            mListeners.add(listener);
        }
    }

    /**
     * Any UI code that no longer wishes for callback results (on async ops)
     * should unregister their callback here (typically from onPause()).
     * Unregistered callbacks will never be called, to prevent problems when the
     * command completes and the activity has already paused or finished.
     *
     * @param listener The callback that may no longer be used
     */
    public void removeResultCallback(Result listener) {
        synchronized (mListeners) {
            if (listener != null) {
                listener.setRegistered(false);
                mListeners.remove(listener);
            }
        }
    }

    /**
     * Simple callback for synchronous commands. For many commands, this can be
     * largely ignored and the result is observed via provider cursors. The
     * callback will *not* necessarily be made from the UI thread, so you may
     * need further handlers to safely make UI updates.
     */
    public static abstract class Result {
        private volatile boolean mRegistered;

        protected void setRegistered(boolean registered) {
            mRegistered = registered;
        }

        protected final boolean isRegistered() {
            return mRegistered;
        }
        //Callback for teh API needs to be implemented here

        /**
         * Callback for getInformationCallback API
         *
         * @param bundle
         * @param reason
         */
        public void getInformationCallback(Model bundle, String reason) {
        }

        /**
         * Callback for getPlaceDetailsCallback API
         *
         * @param bundle
         * @param reason
         */
        public void getPlaceDetailsCallback(PlaceDetailsModel bundle, String reason) {
        }

        /**
         * Callback for getLocationDetailsCallback API
         *
         * @param bundle
         * @param reason
         */
        public void getLocationDetailsCallback(LocationResultsModel bundle, String reason) {
        }

    }

    /**
     * getInformation - API to get information about the requested item
     *
     * @param searchItem
     * @param radius
     * @param keyword
     */
    public void getInformation(Context context, final String searchItem,
                               final String radius, final String keyword) {
        Log.d(TAG, "getInformation");
        if (!Connectivity.isDataConnectionAvailable(mContext)) {
            postErrorCallBack(mContext.getResources().
                    getString(R.string.text_connectivity_error));
            return;
        }
        PreferencesHelper preference = PreferencesHelper.getPreferences(mContext);
        String longtitude = preference.getLongtitude();
        String latitude = preference.getLatitude();
        if (longtitude == null || latitude == null) {
            postErrorCallBack(mContext.getResources().
                    getString(R.string.text_location_error));
            return;
        }
        new PostTask(context, searchItem, longtitude, latitude, radius, keyword).execute();
    }

    private void postErrorCallBack(String reason) {
        synchronized (mListeners) {
            for (Result l : mListeners) {
                l.getInformationCallback(null, reason);
            }
        }
    }

    private class PostTask extends AsyncTask<Void, Void, Model> {
        RestAdapter restAdapter;

        final Context mContext;
        ProgressDialog mProgressDialog;
        String mSearchItem;
        String mKeyword;
        String mLongtitude;
        String mLatitude;
        String mRaduius;

        private PostTask(Context context, String searchItem, String longtitude, String latitude,
                         String radius, String keyword) {
            mContext = context;
            mProgressDialog = new ProgressDialog(context);
            mSearchItem = searchItem;
            mLongtitude = longtitude;
            mLatitude = latitude;
            mRaduius = radius;
            mKeyword = keyword;
        }

        @Override
        protected void onPreExecute() {

            //mProgressDialog.setTitle(R.string.text_please_wait);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage(mContext.getResources().getString(R.string.text_downloading));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface info, int keycode, KeyEvent keyevent) {
                    return false;
                }
            });
            mProgressDialog.show();

            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(BASE_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setLog(new RestAdapter.Log() {
                        @Override
                        public void log(String msg) {
                            Log.i(TAG, msg);
                        }
                    }).build();
        }

        @Override
        protected Model doInBackground(Void... params) {
            ApiService methods = restAdapter.create(ApiService.class);
            Model posts = null;
            try {
                String location = mLatitude + "," + mLongtitude;
                posts = methods.getSearchResults(location, /*mRaduius*/ mSearchItem, mKeyword,
                        AppConstants.API_KEY, /*"true",*/ "distance");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return posts;
        }

        @Override
        protected void onPostExecute(Model posts) {
            mProgressDialog.dismiss();
            Log.d(TAG, "Status " + (posts.getStatus() != null));
            Results[] results = posts.getResults();
            if (results.length > 0) {
                Log.d(TAG, "Name " + results[0].getName());
                Log.d(TAG, "Lat " + results[0].getGeometry().getLocation().getLatitude());
                Log.d(TAG, "Long " + results[0].getGeometry().getLocation().getLongtitude());
                Log.d(TAG, "icon " + results[0].getIcon());
                Log.d(TAG, "id " + results[0].getId());
                Log.d(TAG, "Vicinity " + results[0].getVicinity());
            }

            synchronized (mListeners) {
                for (Result l : mListeners) {
                    l.getInformationCallback(posts, null);
                }
            }
        }
    }

    /**
     * getPlaceDetails - API to get information about the requested item
     *
     * @param place_id
     */
    public void getPlaceDetails(Context context, final String place_id) {
        Log.d(TAG, "getPlaceDetails");
        if (!Connectivity.isDataConnectionAvailable(mContext)) {
            postPlaceDetailsErrorCallBack(mContext.getResources().
                    getString(R.string.text_connectivity_error));
            return;
        }
        new getPlaceDetailsTask(context, place_id).execute();
    }

    private void postPlaceDetailsErrorCallBack(String reason) {
        synchronized (mListeners) {
            for (Result l : mListeners) {
                l.getPlaceDetailsCallback(null, reason);
            }
        }
    }

    private class getPlaceDetailsTask extends AsyncTask<Void, Void, PlaceDetailsModel> {
        RestAdapter restAdapter;

        final Context mContext;
        ProgressDialog mProgressDialog;
        String mPlaceId;

        private getPlaceDetailsTask(Context context, String place_id) {
            mContext = context;
            mProgressDialog = new ProgressDialog(context);
            mPlaceId = place_id;
        }

        @Override
        protected void onPreExecute() {

            //mProgressDialog.setTitle(R.string.text_please_wait);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage(mContext.getResources().getString(R.string.text_downloading));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface info, int keycode, KeyEvent keyevent) {
                    return false;
                }
            });
            mProgressDialog.show();

            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(BASE_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setLog(new RestAdapter.Log() {
                        @Override
                        public void log(String msg) {
                            Log.i(TAG, msg);
                        }
                    }).build();
        }

        @Override
        protected PlaceDetailsModel doInBackground(Void... params) {
            ApiService methods = restAdapter.create(ApiService.class);
            PlaceDetailsModel posts = null;
            try {
                posts = methods.getPlaceDetails(mPlaceId, AppConstants.API_KEY);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return posts;
        }

        @Override
        protected void onPostExecute(PlaceDetailsModel posts) {
            mProgressDialog.dismiss();
            Log.d(TAG, "Status " + (posts.getStatus() != null));
            com.jsb.explorearround.parser.Result result = posts.getResult();
            Log.d(TAG, "Name " + result.getName());
            Log.d(TAG, "Lat " + result.getGeometry().getLocation().getLatitude());
            Log.d(TAG, "Long " + result.getGeometry().getLocation().getLongtitude());
            Log.d(TAG, "Phone number " + result.getInternational_phone_number());
            Log.d(TAG, "rating " + result.getRating());
            Log.d(TAG, "website " + result.getWebsite());
            Log.d(TAG, "Vicinity " + result.getVicinity());
            Log.d(TAG, "url " + result.getUrl());
            Log.d(TAG, "scope " + result.getScope());

            synchronized (mListeners) {
                for (Result l : mListeners) {
                    l.getPlaceDetailsCallback(posts, null);
                }
            }
        }
    }


    /**
     * getLocationDetails - API to get information about the requested item
     *
     * @param location
     */
    public void getLocationDetails(Context context, final String location) {
        Log.d(TAG, "getLocationDetails");
        if (!Connectivity.isDataConnectionAvailable(mContext)) {
            postLocationDetailsErrorCallBack(mContext.getResources().
                    getString(R.string.text_connectivity_error));
            return;
        }
        new getLocationDetailsTask(context, location).execute();
    }

    private void postLocationDetailsErrorCallBack(String reason) {
        synchronized (mListeners) {
            for (Result l : mListeners) {
                l.getLocationDetailsCallback(null, reason);
            }
        }
    }

    private class getLocationDetailsTask extends AsyncTask<Void, Void, LocationResultsModel> {
        RestAdapter restAdapter;

        final Context mContext;
        ProgressDialog mProgressDialog;
        String mLocation;

        private getLocationDetailsTask(Context context, String location) {
            mContext = context;
            mProgressDialog = new ProgressDialog(context);
            mLocation = location;
        }

        @Override
        protected void onPreExecute() {

            //mProgressDialog.setTitle(R.string.text_please_wait);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage(mContext.getResources().getString(R.string.text_downloading));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface info, int keycode, KeyEvent keyevent) {
                    return false;
                }
            });
            mProgressDialog.show();

            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(BASE_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setLog(new RestAdapter.Log() {
                        @Override
                        public void log(String msg) {
                            Log.i(TAG, msg);
                        }
                    }).build();
        }

        @Override
        protected LocationResultsModel doInBackground(Void... params) {
            ApiService methods = restAdapter.create(ApiService.class);
            LocationResultsModel posts = null;
            try {
                posts = methods.getLocationDetails(mLocation, "true");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return posts;
        }

        @Override
        protected void onPostExecute(LocationResultsModel posts) {
            mProgressDialog.dismiss();
            Log.d(TAG, "Status " + (posts.getStatus() != null));
            LocationResults[] results = posts.getResults();
            for (LocationResults result: results) {
                Log.d(TAG, "Formatted Address " + result.getFormatted_address());
                Log.d(TAG, "Lat " + result.getGeometry().getLocation().getLatitude());
                Log.d(TAG, "Long " + result.getGeometry().getLocation().getLongtitude());
            }

            synchronized (mListeners) {
                for (Result l : mListeners) {
                    l.getLocationDetailsCallback(posts, null);
                }
            }
        }
    }
}
