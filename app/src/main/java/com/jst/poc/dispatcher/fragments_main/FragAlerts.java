package com.jst.poc.dispatcher.fragments_main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.jst.poc.dispatcher.R;
import com.jst.poc.dispatcher.adapters.ViewPagerAdapter;
import com.jst.poc.dispatcher.databinding.ActivityAlertsBinding;
import com.jst.poc.dispatcher.fragments_sub.CameraVideoFragment;
import com.jst.poc.dispatcher.fragments_sub.NumPlateListFrag;
import com.jst.poc.dispatcher.room_db.DbRepository;
import com.jst.poc.dispatcher.viewmodels.ApiViewModel;

public class FragAlerts extends Fragment {

  ActivityAlertsBinding binding;
  int colorGreen, colorWhite;
  ViewPager viewPager;
  ViewPagerAdapter adapter;
  NumPlateListFrag numPlateFrag;
  CameraVideoFragment videoFragment;
  DbRepository dbRepository;
  ApiViewModel viewModel;
  int selection_from_dash = -1;

  public void setDbRepository(DbRepository dbRepository) {
    this.dbRepository = dbRepository;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    colorGreen = getContext().getResources().getColor(R.color.text_unselected_green);
    colorWhite = getContext().getResources().getColor(R.color.white);
    adapter = new ViewPagerAdapter(getChildFragmentManager());
    //Item 1
    videoFragment = new CameraVideoFragment();
    adapter.addFragment(videoFragment, "VideoFragment");
    //Item 2
    numPlateFrag = new NumPlateListFrag();
    numPlateFrag.setDbRepository(dbRepository);
    numPlateFrag.setViewModel(viewModel);
    adapter.addFragment(numPlateFrag, "PlateDetectionFrag");
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(inflater, R.layout.activity_alerts, container, false);
    viewPager = binding.viewPager;
    viewPager.setOffscreenPageLimit(2);
    initViewPager();
    return binding.getRoot();
  }

  private void initViewPager() {
    viewPager.setAdapter(adapter);
    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // do some thing
      }

      @Override
      public void onPageSelected(int position) {
        initialView();
        switch (position) {
          case 0:
            modeSwitching(true, binding.btnLiveStream);
            break;
          case 1:
            modeSwitching(true, binding.btnPlateDetection);
            break;
        }
      }

      @Override
      public void onPageScrollStateChanged(int state) {
        // do some thing
      }
    });
    viewPager.setCurrentItem(0);

    binding.btnLiveStream.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        viewPager.setCurrentItem(0);
      }
    });

    binding.btnPlateDetection.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        viewPager.setCurrentItem(1);
      }
    });
  }

  private void modeSwitching(boolean isSelected, AppCompatButton btn) {
    btn.setTextColor(isSelected ? colorWhite : colorGreen);
    btn.setBackgroundDrawable(getContext().getResources().getDrawable(isSelected ? R.drawable.btn_green_in_camera :
        R.drawable.btn_green_in_camera_unselected));
  }

  private void initialView() {
    modeSwitching(false, binding.btnLiveStream);
    modeSwitching(false, binding.btnPlateDetection);
  }

  public void setFragSelected(int num) {
    selection_from_dash = num;
    if (binding != null && num != -1) {
      binding.viewPager.setCurrentItem(num);
    }
  }

  public void setViewModel(ApiViewModel viewModel) {
    this.viewModel = viewModel;
  }
}
