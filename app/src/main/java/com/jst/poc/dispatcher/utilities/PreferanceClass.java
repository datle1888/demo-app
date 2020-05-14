package com.jst.poc.dispatcher.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.jst.poc.dispatcher.models.User;

import org.jetbrains.annotations.NotNull;

public class PreferanceClass {
  public static final String PREF_NAME = "dispatcher_preferences";
  public static final int MODE = Context.MODE_PRIVATE;

  public static void setAccessToken(String accessToken, Context context) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(PreferenceKey.AUTH_TOKEN, accessToken);
    editor.apply();
  }

  public static String getAccessToken(Context context) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE);
    return sharedPreferences.getString(PreferenceKey.AUTH_TOKEN, "");
  }

  public static String getAccessToken(String accessToken, Context context) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE);
    return sharedPreferences.getString(PreferenceKey.AUTH_TOKEN, "");
  }

  public static void setAuthAccessToken(String authAccessToken, Context context) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(PreferenceKey.AUTHKEY, authAccessToken);
    editor.apply();
  }

  public static String getAuthAccessToken(Context context) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE);
    return sharedPreferences.getString(PreferenceKey.AUTHKEY, "");
  }


  public static void setAuthRefreshAccessToken(String authAccessToken, Context context) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(PreferenceKey.AUTHKEYREFERSH, authAccessToken);
    editor.apply();
  }

  public static String getAuthRefreshAccessToken(Context context) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE);
    return sharedPreferences.getString(PreferenceKey.AUTHKEYREFERSH, "");
  }

  public static void setLoggedIn(boolean loggedIn, @NotNull Context context) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putBoolean(PreferenceKey.IS_LOGGED_IN, loggedIn);
    editor.apply();
  }

  public static boolean isLoggedIn(Context context) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE);
    return sharedPreferences.getBoolean(PreferenceKey.IS_LOGGED_IN, false);
  }

  public static void setUserName(String userName, @NotNull Context context) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(PreferenceKey.USER_NAME, userName);
    editor.apply();
  }

  public static String getUserName(Context context) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE);
    return sharedPreferences.getString(PreferenceKey.USER_NAME, "");
  }

  public static void setUser(String user, @NotNull Context context) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    Gson gson = new Gson();
    String reservationString = gson.toJson(user);
    editor.putString(PreferenceKey.USER, reservationString);
    editor.apply();
  }

  public static User getUser(Context context) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE);
    String reservationString = sharedPreferences.getString(PreferenceKey.USER, "");
    Gson gson = new Gson();
    return gson.fromJson(reservationString, User.class);
  }

  private static SharedPreferences getPreferences(Context context) {
    return context.getSharedPreferences(PREF_NAME, MODE);
  }

  private static SharedPreferences.Editor getEditor(Context context) {
    return getPreferences(context).edit();
  }

  public static void RemoveAll(Context context) {
    getEditor(context).remove(PreferenceKey.AUTH_TOKEN).commit();
    getEditor(context).remove(PreferenceKey.IS_LOGGED_IN).commit();
    getEditor(context).remove(PreferenceKey.USER_NAME).commit();
    getEditor(context).remove(PreferenceKey.USER_PHONE).commit();
  }

  public static void RemoveItem(Context context, String key) {
    getEditor(context).remove(key).commit();
  }
}
