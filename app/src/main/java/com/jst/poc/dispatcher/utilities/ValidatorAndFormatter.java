package com.jst.poc.dispatcher.utilities;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.jst.poc.dispatcher.utilities.TimeAgoPack.TimeAgo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ValidatorAndFormatter {
  static ValidatorAndFormatter instance;
  private static TimeAgo timeAgo;
  String date_tim_input_format = "yyyy-MM-dd'T'HH:mm:ss'Z'";
  SimpleDateFormat sdf = new SimpleDateFormat(date_tim_input_format, Locale.US);

  public static ValidatorAndFormatter getInstance() {
    if (instance == null) {
      instance = new ValidatorAndFormatter();
    }
    if (timeAgo == null) {
      timeAgo = new TimeAgo();
    }
    return instance;
  }

  public String displayDateAndTimeJob(String inputDate) {
    String output_format = "dd MMM - hh:mm a";
    try {
      Date date = sdf.parse(inputDate);
      SimpleDateFormat outFormat = new SimpleDateFormat(output_format, Locale.US);
      inputDate = outFormat.format(date);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return inputDate;
  }

  public String getDistance(String latLong1, String latLong2) {
    String returnStr = "0 KM";
    try {
      String[] loc1 = latLong1.split(",");
      String[] loc2 = latLong2.split(",");

      double lat1 = Double.parseDouble(loc1[0]);
      double lon1 = Double.parseDouble(loc1[1]);

      double lat2 = Double.parseDouble(loc2[0]);
      double lon2 = Double.parseDouble(loc2[1]);

      Location startPoint = new Location("locationA");
      startPoint.setLatitude(lat1);
      startPoint.setLongitude(lon1);

      Location endPoint = new Location("locationA");
      endPoint.setLatitude(lat2);
      endPoint.setLongitude(lon2);
      double distance = startPoint.distanceTo(endPoint) / 1000;
      String latSTR = String.format("%.2f", distance);
      returnStr = latSTR + " KM";
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
    return returnStr;
  }

  public LatLng getLocFromString(String latLong1) {
    LatLng latLng = new LatLng(0, 0);
    try {
      String[] loc1 = latLong1.split(",");
      double lat1 = Double.parseDouble(loc1[0]);
      double lon1 = Double.parseDouble(loc1[1]);
      latLng = new LatLng(lat1, lon1);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return latLng;
  }

  public String longTimeToDate(long time) {
    String returnString = " ";
    try {
      Date date = new Date(TimeUnit.SECONDS.toMillis(time));
      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
      returnString = sdf.format(date);
    } catch (Exception e) {
    }
    return returnString;
  }

  public String unixTimeStampToTimeAgo(long timeStamp) {
    Date time = new Date(timeStamp * 1000);
    return timeAgo.timeAgo(time);
  }
}
