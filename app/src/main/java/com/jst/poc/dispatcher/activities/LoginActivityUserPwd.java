package com.jst.poc.dispatcher.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.jst.poc.dispatcher.R;
import com.jst.poc.dispatcher.databinding.ActivityLoginUserPassBinding;
import com.jst.poc.dispatcher.utilities.Logger;
import com.jst.poc.dispatcher.utilities.PreferanceClass;

public class LoginActivityUserPwd extends AppCompatActivity {
  private final String TAG = "~~LoginActivityUserPwd";
  Context context;
  private String userName;
  private String pwd;
  ActivityLoginUserPassBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_login_user_pass);
    context = this;
    setAction();
  }

  private void setAction() {
    actionLogin();
  }

  private void actionLogin() {
    binding.loginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        userName = String.valueOf(binding.editTextUserName.getText());
        pwd = String.valueOf(binding.editTextPassword.getText());
        if (validate(userName, pwd)) {
          // Call api

          PreferanceClass.setAccessToken("accessToken", getApplicationContext());
          PreferanceClass.setUserName(userName, getApplicationContext());
//          PreferanceClass.setUser("Img", getApplicationContext());
          PreferanceClass.setLoggedIn(true, getApplicationContext());
          //
          gotoDash();
        }
      }
    });
  }

  private boolean validate(String userName, String pwd) {
    if (userName.isEmpty() || pwd.isEmpty()  ) {
      return false;
    }
    return true;
  }

  private void gotoDash() {
    try {
      Intent intent = new Intent(context, MainActivity.class);
      finish();
      startActivity(intent);
      overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    } catch (Exception ex) {
      Logger.getInstance().error_log(TAG, ex.getMessage(), ex.fillInStackTrace());
    }
  }
}

