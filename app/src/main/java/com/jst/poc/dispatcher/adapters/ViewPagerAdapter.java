package com.jst.poc.dispatcher.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

  private final List<Fragment> mFragmentList = new ArrayList<>();
  private final List<String> mFragmentTitleList = new ArrayList<>();

  public ViewPagerAdapter(FragmentManager manager) {
    super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    return mFragmentList.get(position);
  }

  public void addFragment(Fragment fragment, String title) {
    Log.d("mFragmentList", String.valueOf(mFragmentList));
    mFragmentList.add(fragment);
    mFragmentTitleList.add(title);
  }

  @Override
  public int getCount() {
    return mFragmentList == null ? 0 : mFragmentList.size();
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    return mFragmentTitleList.get(position);
  }
}
