package com.jst.poc.dispatcher.utilities;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.jst.poc.dispatcher.interfaces.LocationServiceListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LocationService extends Service {

  private static final String TAG = "~~" + "ilLocation";
  public static Location BestLocation = null;
  private final IBinder mBinder = new LocalBinder();
  List<Location> locationList = new ArrayList<>();
  LocationListener[] mLocationListeners = new LocationListener[]{
      new LocationListener(LocationManager.GPS_PROVIDER),
      new LocationListener(LocationManager.NETWORK_PROVIDER)
  };
  private LocationManager mLocationManager = null;
  private LocationServiceListener locationServiceListener;

  public LocationService() {
  }

  public void registerListener(LocationServiceListener locationServiceListener) {
    this.locationServiceListener = locationServiceListener;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    BestLocation = null;
    startLocationUpdate();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return START_NOT_STICKY;
  }

  @Override
  public void onDestroy() {
    BestLocation = null;
    stopLocationUpdate();
    super.onDestroy();
  }

  private void initializeLocationManager() {
    if (mLocationManager == null) {
      mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    }
  }

  private void stopLocationUpdate() {
    if (mLocationManager != null) {
      for (LocationListener mLocationListener : mLocationListeners) {
        try {
          mLocationManager.removeUpdates(mLocationListener);
        } catch (Exception ignored) {
        }
      }
    }
  }

  private void startLocationUpdate() {
    initializeLocationManager();
    float LOCATION_DISTANCE = 1f;
    long LOCATION_INTERVAL = 1000;
    try {
      mLocationManager.requestLocationUpdates(
          LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
          mLocationListeners[1]);
    } catch (SecurityException | IllegalArgumentException ignored) {
    }
    try {
      mLocationManager.requestLocationUpdates(
          LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
          mLocationListeners[0]);
    } catch (SecurityException | IllegalArgumentException ignored) {
    }
  }

  private boolean selectSecondPoint(Location loc1, Location loc2) {
    // Checking distance between loc1 and loc2
    double distance = loc1.distanceTo(loc2);  // This is in metre
    long timeDifference = TimeUnit.MILLISECONDS.toSeconds(loc2.getTime() - loc1.getTime()); //This is in seconds
//        Logger.getInstance().verbose_log(TAG, "distance b/w loc1 & loc2 = " + distance);
//        Logger.getInstance().verbose_log(TAG, "Time diff b/w loc1 & loc2 = " + timeDifference);
    if (distance >= 500) {
      //dist >= 500 then consider loc2 -- condition User in Vehicle
//            Logger.getInstance().verbose_log(TAG, "Assumption : user is in vehicle , Location b set by 1st condition " );
      setLocationAndSend(loc2);
      return true;
    } else if (timeDifference >= 60 && distance >= 100) {
      // if time difference between loc1 & loc2 >60  && distance between two locations >100, then consider loc2
      // Assumption : user is in running mode
      setLocationAndSend(loc2);
//            Logger.getInstance().verbose_log(TAG, "Assumption : user is in running mode , Location b set by 2nd condition " );
      return true;
    } else if (timeDifference >= 60 && distance >= 20 && loc2.getAccuracy() <= 25) {
      // Assumption : user is walking ,consider loc 2
      setLocationAndSend(loc2);
//            Logger.getInstance().verbose_log(TAG, "Assumption : user is in walking mode , Location b set by 3rd condition ");
      return true;
    } else if (timeDifference >= 60 && loc2.getAccuracy() <= 25) {
      //Assumption : user in stationary
//            Logger.getInstance().verbose_log(TAG, "Assumption : user is in stationary mode , Location b set by 4th condition ");
      return true;
    } else if (timeDifference >= 360) {
//            Logger.getInstance().verbose_log(TAG, "Assumption : user is in vehicle , Location b set by 5th condition ");
      setLocationAndSend(loc2);
      return true;
    } else {
      return false;
    }
  }

  private void setLocationAndSend(Location location) {
    BestLocation = location;
    if (locationServiceListener != null) {
      locationServiceListener.onLocationUpdation(location);
    }
  }

  private class LocationListener implements android.location.LocationListener {
    Location mLastLocation;

    LocationListener(String provider) {
      mLastLocation = new Location(provider);
    }

    @Override
    public void onLocationChanged(Location location) {
      Logger.getInstance().verbose_log(null, "onLocationChanged " + location.getAccuracy());
      if (location.getAccuracy() < 50) {
        if (BestLocation == null) {
          setLocationAndSend(location);
        } else {
          selectSecondPoint(BestLocation, location);
        }
      } else {
        if (BestLocation == null) {
          locationList.add(location);
          if (locationList.size() > 2) {
            for (Location l : locationList) {
              if (BestLocation != null) {
                if (l.getAccuracy() < BestLocation.getAccuracy()) {
                  BestLocation = l;
                }
              } else {
                BestLocation = l;
              }
            }
            locationList.clear();
            if (locationServiceListener != null) {
              locationServiceListener.onLocationUpdation(BestLocation);
            }
          }
        }
      }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
  }

  //returns the instance of the service
  public class LocalBinder extends Binder {
    public LocationService getServiceInstance() {
      return LocationService.this;
    }
  }
}
