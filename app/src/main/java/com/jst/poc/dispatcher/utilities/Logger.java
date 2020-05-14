package com.jst.poc.dispatcher.utilities;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

public class Logger {

  public static Logger instance;
  private boolean isEnabled = true;

  public static Logger getInstance() {
    if (instance == null) {
      instance = new Logger();
    }
    return instance;
  }

  public void error_log(String TAG, String msg, @Nullable Throwable t) {
    if (!isEnabled) return;
    if (t == null) {
      Log.e(TAG, msg);
    } else {
      Log.e(TAG, msg, t);
    }
  }

  public void verbose_log(@Nullable String TAG, String msg) {
    if (!isEnabled) return;
    if (TextUtils.isEmpty(TAG)) {
      TAG = "~~";
    }
    Log.v(TAG, msg);
  }
}
