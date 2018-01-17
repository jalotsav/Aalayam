package com.jalotsav.aalayamnavjivan.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Jalotsav on 1/17/2018.
 */

public class GeneralFunctions {

    private static final String TAG = GeneralFunctions.class.getSimpleName();

    /***
     * Check Internet Connection
     * Mobile device is connect with Internet or not?
     * ***/
    public static boolean isNetConnected(Context context){

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //This for Wifi.
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected())
            return true;

        //This for Mobile Network.
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected())
            return true;

        //This for Return true else false for Current status.
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
