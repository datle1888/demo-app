package com.jst.poc.dispatcher.fragments_sub;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.jst.poc.dispatcher.R;
import com.jst.poc.dispatcher.databinding.VideoFragLayoutBinding;
import com.jst.poc.dispatcher.rest.IpClass;

public class CameraVideoFragment extends Fragment {//implements TextureView.SurfaceTextureListener {
  private static final String TAG = "~~CamFrag";
  private static final String TAG_SURFACE = "~~Surface";
  VideoFragLayoutBinding binding;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // mediaPlayer = new MediaPlayer();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(inflater, R.layout.video_frag_layout, container, false);
    if (!binding.vv1.isPlaying())
      startVideoPlayer(binding.vv1, IpClass.CarCh8, binding.progress1);
    if (!binding.vv2.isPlaying())
      startVideoPlayer(binding.vv2, IpClass.CarCh4, binding.progress2);
    return binding.getRoot();
  }

  private void startVideoPlayer(VideoView view, String url, ProgressBar progressBar) {
    view.setVideoPath(url);
    view.start();
    progressBar.setVisibility(View.VISIBLE);
    setBoundaryVisibilty(false);
    view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override
      public void onPrepared(MediaPlayer mediaPlayer) {
        progressBar.setVisibility(View.GONE);
        setBoundaryVisibilty(true);
      }
    });
    view.setOnErrorListener(new MediaPlayer.OnErrorListener() {
      @Override
      public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        view.start();
        return true;
      }
    });
  }

  private void setBoundaryVisibilty(boolean show) {
    if (binding == null) {
      return;
    }
    binding.w1.setVisibility(View.VISIBLE);
    if (show) {
      // binding.w1.setVisibility(View.VISIBLE);
      binding.w2.setVisibility(View.VISIBLE);
    } else {
      // binding.w1.setVisibility(View.GONE);
      binding.w2.setVisibility(View.GONE);
    }
  }
}


