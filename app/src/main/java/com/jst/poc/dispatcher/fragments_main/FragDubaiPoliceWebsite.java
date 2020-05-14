package com.jst.poc.dispatcher.fragments_main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.jst.poc.dispatcher.R;
import com.jst.poc.dispatcher.databinding.ActivityWebBinding;

public class FragDubaiPoliceWebsite extends Fragment {
  ActivityWebBinding binding;
  WebView webView;
  String url = "https://www.dubaipolice.gov.ae/wps/portal/home";

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(inflater, R.layout.activity_web, container, false);
    webView = binding.webView;
    WebSettings ws = webView.getSettings();
    //enable Javascript
    ws.setJavaScriptEnabled(true);
    ws.setAllowFileAccess(true);
    ws.setJavaScriptCanOpenWindowsAutomatically(false);
    ws.setDomStorageEnabled(true);
    webView.loadUrl(url);
    return binding.getRoot();
  }

  public void setWebUrl(String url) {
    this.url = url;
  }
}
