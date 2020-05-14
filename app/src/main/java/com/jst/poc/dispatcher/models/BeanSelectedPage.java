package com.jst.poc.dispatcher.models;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class BeanSelectedPage {
  Top_selection selection;
  AppCompatActivity activity;
  Bundle bundle;
  ViewPager viewPager;
  public void setViewPager(ViewPager viewPager) {
    this.viewPager = viewPager;
  }
  public Top_selection getSelection() {
    return selection;
  }
  public void setSelection(Top_selection selection) {
    this.selection = selection;
  }
  public void setActivity(AppCompatActivity activity) {
    this.activity = activity;
  }
  public void setBundle(Bundle bundle) {
    this.bundle = bundle;
  }
  public void onClickItem(View view, Top_selection sel) {
    try {
//            Intent intent = null;
      if (viewPager != null) {
        switch (sel) {
          case dashboard:
//                        intent = new Intent(activity, DashBoard.class);
            viewPager.setCurrentItem(0);
            break;
          case jobs:
//                        intent = new Intent(activity, Jobs.class);
            viewPager.setCurrentItem(1);
            break;
          case cameras:
//                        intent = new Intent(activity, CamerasNew.class);
            viewPager.setCurrentItem(2);
            break;
          case vehicle:
//                        intent = new Intent(activity, Vehicles.class);
            viewPager.setCurrentItem(3);
            break;
        }
      }
//            activity.startActivity(intent,bundle);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void onClickApplication(View view) {
    try {
//            Intent intent = null;
//            intent = new Intent(activity, MainActivity.class);
//            activity.startActivity(intent);
      if (viewPager != null) {
        //Website
        viewPager.setCurrentItem(4);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
