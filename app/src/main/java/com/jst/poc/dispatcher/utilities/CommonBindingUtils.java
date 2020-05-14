package com.jst.poc.dispatcher.utilities;

import android.content.res.ColorStateList;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.jst.poc.dispatcher.R;

public class CommonBindingUtils {

  @RequiresApi(api = Build.VERSION_CODES.M)
  @BindingAdapter("selectionChange")
  public static void setDrawableTint(AppCompatTextView view, boolean selected) {

    int color = 0;

    if (selected) {
      color = ContextCompat.getColor(view.getContext(), R.color.white);

    } else {
      color = view.getContext().getColor(R.color.text_unselected_green);
    }
    view.setSupportCompoundDrawablesTintList(ColorStateList.valueOf(color));
    view.setTextColor(ColorStateList.valueOf(color));
  }
}
