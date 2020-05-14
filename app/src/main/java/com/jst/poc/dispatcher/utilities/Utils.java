package com.jst.poc.dispatcher.utilities;

import android.util.Pair;
import android.view.View;

public class Utils {

  private static Utils utils;

  public static Utils getInstance() {
    if (utils == null) {
      utils = new Utils();
    }
    return utils;
  }
  public Pair<View, String> createShareElementPair(View view) {
    return new Pair(view, view.getTransitionName());
  }
}
