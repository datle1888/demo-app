package com.jst.poc.dispatcher.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import com.jst.poc.dispatcher.R;

public class CustomLoadingDialog {
  Activity activity;
  Dialog dialog;
  public CustomLoadingDialog(Activity activity) {
    this.activity = activity;
  }
  public void showDialog() {
    dialog = null;
    dialog = new Dialog(activity);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    //...set cancelable false so that it's never get hidden
    dialog.setCancelable(false);
    //...that's the layout i told you will inflate later
    dialog.setContentView(R.layout.custom_loading_dialog);
    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    ProgressBar progressBar = dialog.findViewById(R.id.progress);
    progressBar.setVisibility(View.VISIBLE);
    dialog.show();
  }

  //..also create a method which will hide the dialog when some work is done
  public void hideDialog() {
    if (dialog != null) {
      if (dialog.isShowing()) dialog.dismiss();
    }
  }
}
