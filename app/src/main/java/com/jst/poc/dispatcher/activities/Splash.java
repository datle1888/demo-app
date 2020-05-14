package com.jst.poc.dispatcher.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.jst.poc.dispatcher.R;
import com.jst.poc.dispatcher.utilities.LocationService;
import com.jst.poc.dispatcher.utilities.Logger;
import com.jst.poc.dispatcher.utilities.PreferanceClass;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Splash extends AppCompatActivity {

  private final String TAG = "~~Splash";
  Context context;
  private Intent locationServiceIntent;
  private LocationService locationService;
  //Connecting to location service
  private ServiceConnection serviceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

      LocationService.LocalBinder binder = (LocationService.LocalBinder) iBinder;
      locationService = binder.getServiceInstance(); //Get instance of your service!
      //locationService.registerListener(Splash.this); //Activity register here as listener
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
      Logger.getInstance().verbose_log(null, "service disconnected");
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    context = this;
    String token = PreferanceClass.getAccessToken(context);
    new android.os.Handler().postDelayed(
        new Runnable() {
          public void run() {
            // Fake data
            if (!token.isEmpty()) {
              gotoDash();
            }else{
              gotoLogin();
            }
          }
        },
        3000);
  }

  private void fetchUserCurrentLocation() {
    // findCurrentPlace and handle the response (first check that the user has granted permission).
    if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      startLocationService();
    } else {
      // A local method to request required permissions;
      // See https://developer.android.com/training/permissions/requesting
      getLocationPermission();
    }
  }

  private void getLocationPermission() {
    Dexter.withActivity(this)
        .withPermission(ACCESS_FINE_LOCATION)
        .withListener(new PermissionListener() {
          @Override
          public void onPermissionGranted(PermissionGrantedResponse response) {
            startLocationService();
          }

          @Override
          public void onPermissionDenied(PermissionDeniedResponse response) {
            gotoLogin();
          }

          @Override
          public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
            token.continuePermissionRequest();
          }
        }).check();
  }

  private void startLocationService() {
    //Get location
    startService(locationServiceIntent);
    //bindService(locationServiceIntent, serviceConnection, Context.BIND_NOT_FOREGROUND); //Binding to the service!
    Logger.getInstance().verbose_log(TAG, "startLocationService");
    gotoLogin();
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

  private void gotoLogin() {
    try {
      Intent intent = new Intent(context, LoginActivity.class);
      finish();
      startActivity(intent);
      overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    } catch (Exception ex) {
      Logger.getInstance().error_log(TAG, ex.getMessage(), ex.fillInStackTrace());
    }
  }
}
