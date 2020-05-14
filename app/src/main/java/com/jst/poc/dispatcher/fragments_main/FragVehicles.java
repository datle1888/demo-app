package com.jst.poc.dispatcher.fragments_main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.jst.poc.dispatcher.R;
import com.jst.poc.dispatcher.databinding.NewActivityVehicleBinding;

public class FragVehicles extends Fragment {
  NewActivityVehicleBinding binding;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(inflater, R.layout.new_activity_vehicle, container, false);
    return binding.getRoot();
  }
}
