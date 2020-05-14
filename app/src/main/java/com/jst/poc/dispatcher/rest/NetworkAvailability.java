package com.jst.poc.dispatcher.rest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkAvailability {
  private Context context;
  public NetworkAvailability(Context context) {
    this.context = context;
  }
  boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager
        = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    boolean val = (activeNetworkInfo != null && activeNetworkInfo.isConnected());
    return val;
  }
}
