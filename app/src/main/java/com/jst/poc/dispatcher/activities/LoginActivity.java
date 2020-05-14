package com.jst.poc.dispatcher.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.jst.poc.dispatcher.R;
import com.jst.poc.dispatcher.databinding.ActivityLoginBinding;
import com.jst.poc.dispatcher.utilities.Logger;

public class LoginActivity extends AppCompatActivity {

  private final String TAG = "~~LoginActivity";
  Context context;
  boolean detecting = false;
  ActivityLoginBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
    context = this;
    try {
      MediaPlayer mp = MediaPlayer.create(this, R.raw.robot);
      mp.setLooping(false);
      View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mp.start();
          if (!detecting) {
            detecting = true;
            Animation rotation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.rotate);
            rotation.setFillAfter(true);
            binding.loginImg.startAnimation(rotation);
            binding.loginImg.setImageResource(R.drawable.dp_circle4);
            binding.loginTv.setText(R.string.lbl_loading_face);
            binding.loginTv.setTextColor(getResources().getColor(R.color.white));
            new android.os.Handler().postDelayed(
                new Runnable() {
                  public void run() {
                    detecting = false;
                    new AlertDialog.Builder(context)
                        .setMessage("Face detection failed, click ok to sign in with your username and password")
                        .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog, int which) {
                                gotoLoginUserPwd();
                              }
                            }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                      @Override
                      public void onDismiss(DialogInterface dialog) {
                        gotoLoginUserPwd();
                      }
                    }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                      @Override
                      public void onCancel(DialogInterface dialog) {
                        gotoLoginUserPwd();
                      }
                    }).show();
                  }
                },
                1000);
          }
        }
      };
      binding.loginTv.setOnClickListener(onClickListener);
      binding.loginImg.setOnClickListener(onClickListener);
    } catch (Exception e) {
      Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
    }
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

  private void gotoLoginUserPwd() {
    try {
      Intent intent = new Intent(context, LoginActivityUserPwd.class);
      finish();
      startActivity(intent);
      overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    } catch (Exception ex) {
      Logger.getInstance().error_log(TAG, ex.getMessage(), ex.fillInStackTrace());
    }
  }
}
