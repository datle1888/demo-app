package com.jst.poc.dispatcher.models;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jst.poc.dispatcher.utilities.ValidatorAndFormatter;

public class BeanSocketObjWeapon {

  //*************** Local variable for display content
  String displayDate;
  String area = "Not available";
  long time_of_insertion;
  @SerializedName("type")
  @Expose
  private String type;
  @SerializedName("id")
  @Expose
  private String id;
  @SerializedName("time")
  @Expose
  private long time;
  @SerializedName("confidence")
  @Expose
  private Integer confidence;
  @SerializedName("imageBase64")
  @Expose
  private String imageBase64;
  @SerializedName("weaponImageUrl")
  @Expose
  private String weaponImageUrl;
  @SerializedName("faceImageUrl")
  @Expose
  private String faceImageUrl;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public Integer getConfidence() {
    return confidence;
  }

  public void setConfidence(Integer confidence) {
    this.confidence = confidence;
  }

  public String getImageBase64() {
    return imageBase64;
  }

  public void setImageBase64(String imageBase64) {
    this.imageBase64 = imageBase64;
  }

  public String getWeaponImageUrl() {
    return weaponImageUrl;
  }

  public void setWeaponImageUrl(String weaponImageUrl) {
    this.weaponImageUrl = weaponImageUrl;
  }

  public String getFaceImageUrl() {
    return faceImageUrl;
  }

  public void setFaceImageUrl(String faceImageUrl) {
    this.faceImageUrl = faceImageUrl;
  }

  public String getDisplayDate() {
    displayDate = ValidatorAndFormatter.getInstance().unixTimeStampToTimeAgo(time);
    return displayDate;
  }

  public String getArea() {
    if (TextUtils.isEmpty(area)) {
      area = "Not Available";
    } else {
      try {
        if (area.contains("United Arab Emirates")) {
          area = area.replace("United Arab Emirates", "UAE");
        }
      } catch (Exception e) {
      }
    }
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public long getTime_of_insertion() {
    return time_of_insertion;
  }

  public void setTime_of_insertion(long time_of_insertion) {
    this.time_of_insertion = time_of_insertion;
  }

  @Override
  public String toString() {
    return "BeanSocketObjWeapon{" +
        "type='" + type + '\'' +
        ", id='" + id + '\'' +
        ", time=" + time +
        ", confidence=" + confidence +
        ", imageBase64='" + imageBase64 + '\'' +
        ", weaponImageUrl='" + weaponImageUrl + '\'' +
        ", faceImageUrl='" + faceImageUrl + '\'' +
        ", displayDate='" + displayDate + '\'' +
        ", area='" + area + '\'' +
        ", time_of_insertion=" + time_of_insertion +
        '}';
  }
}
