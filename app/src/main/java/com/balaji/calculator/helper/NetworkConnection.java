package com.balaji.calculator.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class NetworkConnection {

    public static boolean hasConnection(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cm != null)
        {
            try {
                if (Build.VERSION.SDK_INT < 23)
                {
                    @SuppressLint("MissingPermission")
                    final NetworkInfo ni = cm.getActiveNetworkInfo();

                    if (ni != null) {
                        return (ni.isConnected() && (ni.getType() == ConnectivityManager.TYPE_WIFI || ni.getType() == ConnectivityManager.TYPE_MOBILE));
                    }
                } else {
                    @SuppressLint("MissingPermission")
                    final Network n = cm.getActiveNetwork();

                    if (n != null) {
                        @SuppressLint("MissingPermission")
                        final NetworkCapabilities nc = cm.getNetworkCapabilities(n);

                        return (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
                    }
                }
            }
            catch(Exception e) {}
        }
        return false;
    }
}
