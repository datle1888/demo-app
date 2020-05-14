package com.jst.poc.dispatcher.fragments_main;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.jst.poc.dispatcher.R;
import com.jst.poc.dispatcher.databinding.CamerasFragBinding;

public class FragCameras extends Fragment {

  CamerasFragBinding binding;
  Uri video1;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    video1 = Uri.parse(Environment.getExternalStorageDirectory().toString() + "/dispatcherVideos/video1.mp4");
//        video1 = Uri.parse("http://185.173.32.12:12060/live.flv?devid=009800012C&chl=4&svrid=127.0.0.1&svrport=17891&st=0&audio=1");
  }
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(inflater, R.layout.cameras_frag, container, false);
    binding.videoView1.setVideoURI(video1);
    binding.videoView1.start();
//        binding.videoView3.setVideoURI(video3);
//        binding.videoView3.start();
//        binding.videoView4.setVideoURI(video4);
//        binding.videoView4.start();
//        binding.videoView5.setVideoURI(video5);
//        binding.videoView5.start();
    return binding.getRoot();
  }
}
