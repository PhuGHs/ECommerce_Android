package com.example.ecommerce_hvpp.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkChangeBroadcastReceiver extends BroadcastReceiver {
    private NetworkConnectivityListener listener;

    public interface NetworkConnectivityListener {
        void onNetworkConnectivityChanged(boolean isConnected);
    }

    public void setListener(NetworkConnectivityListener listener) {
        this.listener = listener;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if(listener != null) {
            listener.onNetworkConnectivityChanged(isNetworkAvailable(context));
        }
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
