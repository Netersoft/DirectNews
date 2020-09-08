package com.neteru.hermod.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

@SuppressWarnings("unused")
public class Connectivity {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private boolean connected = false;

    public static Connectivity getInstance(Context ctx) {
        context = ctx.getApplicationContext();

        return new Connectivity();
    }

    public boolean isOnline() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivityManager == null){ return false; }

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();

            return connected;

        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }
}
