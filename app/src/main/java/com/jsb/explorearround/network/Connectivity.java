package com.jsb.explorearround.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by JSB on 10/7/15.
 */
public class Connectivity {

    public static boolean isDataConnectionAvailable(Context context) {
        boolean isAvailable = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isAvailable = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isAvailable;
    }
}
