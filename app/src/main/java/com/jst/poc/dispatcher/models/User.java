package com.jst.poc.dispatcher.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jst.poc.dispatcher.utilities.ValidatorAndFormatter;

public class User {
  @SerializedName("user_id")
  @Expose
  private String user_id;
  @SerializedName("user_name")
  @Expose
  private String user_name;
  @SerializedName("user_email")
  @Expose
  private String user_email;
  @SerializedName("user_phone")
  @Expose
  private String user_phone;


  public String getUser_name() {
    return user_name;
  }

  public void setUser_name(String user_name) {
    this.user_name = user_name;
  }

  public String getUser_id() {
    return user_id;
  }

  public void setUser_id(String user_id) {
    this.user_id = user_id;
  }

  public String getUser_email() {
    return user_email;
  }

  public void setUser_email(String user_email) {
    this.user_email = user_email;
  }

  public String getUser_phone() {
    return user_phone;
  }

  public void setUser_phone(String user_phone) {
    this.user_phone = user_phone;
  }

}
