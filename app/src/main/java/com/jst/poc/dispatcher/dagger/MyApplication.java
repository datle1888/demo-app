package com.jst.poc.dispatcher.dagger;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.jst.poc.dispatcher.rest.ApiClientModule;
import com.jst.poc.dispatcher.rest.NetworkAvailability;
import com.jst.poc.dispatcher.room_db.RoomDBModule;

import javax.inject.Singleton;

@Singleton
public class MyApplication extends Application {
  protected static MyApplication instance;
  AppComponent appComponent;
  Context context;
  NetworkAvailability networkAvailability;
  ApiClientModule apiClientModule;
  RoomDBModule roomDBModule;

  public static MyApplication getInstance() {
    return instance;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    Log.v("TEST", "Start: Application created");
    context = this;
    instance = this;
    networkAvailability = new NetworkAvailability(context);
    apiClientModule = new ApiClientModule(networkAvailability);
    roomDBModule = new RoomDBModule(instance);
    appComponent = DaggerAppComponent.builder()
        .appModule(new AppModule(instance))
        .apiClientModule(apiClientModule)
        .roomDBModule(roomDBModule)
        .build();
    Log.v("TEST", (appComponent == null) ? "no appComponent" : "appComponent available");
    Log.v("TEST", "End: Application created");
  }

  public AppComponent getAppComponent() {
    return appComponent;
  }

  @Override
  protected void attachBaseContext(Context context) {
    super.attachBaseContext(context);
  }
}
