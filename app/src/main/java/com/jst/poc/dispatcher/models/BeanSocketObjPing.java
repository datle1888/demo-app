package com.jst.poc.dispatcher.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BeanSocketObjPing {

  @SerializedName("type")
  @Expose
  private String type;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "BeanSocketObjPing{" +
        "type='" + type + '\'' +
        '}';
  }
}
