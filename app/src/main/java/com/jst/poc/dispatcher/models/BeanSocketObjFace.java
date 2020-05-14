package com.jst.poc.dispatcher.models;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jst.poc.dispatcher.utilities.ValidatorAndFormatter;

import java.util.List;

public class BeanSocketObjFace {

  //*************** Local variable for display content
  String displayDate;
  String area = "Not available";
  List<String> imgUrlListFromDB = null;
  long time_of_insertion;
  @SerializedName("type")
  @Expose
  private String type;
  @SerializedName("id")
  @Expose
  private String id;
  @SerializedName("time")
  @Expose
  private Long time;
  @SerializedName("confidence")
  @Expose
  private Integer confidence;
  @SerializedName("imageBase64")
  @Expose
  private String imageBase64;
  @SerializedName("imageUrl")
  @Expose
  private String imageUrl;
  @SerializedName("wantedPerson")
  @Expose
  private String wantedPerson;
  @SerializedName("wantedPersonName")
  @Expose
  private String wantedPersonName;
  @SerializedName("wantedImageBase64")
  @Expose
  private String wantedImageBase64;
  @SerializedName("wantedImageUrl")
  @Expose
  private String wantedImageUrl;
  @SerializedName("detectedBy")
  @Expose
  private String detectedBy;

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

  public Long getTime() {
    return time;
  }

  public void setTime(Long time) {
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

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getWantedPerson() {
    return wantedPerson;
  }

  public void setWantedPerson(String wantedPerson) {
    this.wantedPerson = wantedPerson;
  }

  public String getWantedPersonName() {

    if (TextUtils.isEmpty(wantedPersonName)) {
      wantedPersonName = "No Name";
    }

    return wantedPersonName;
  }

  public void setWantedPersonName(String wantedPersonName) {
    this.wantedPersonName = wantedPersonName;
  }

  public String getWantedImageBase64() {
    return wantedImageBase64;
  }

  public void setWantedImageBase64(String wantedImageBase64) {
    this.wantedImageBase64 = wantedImageBase64;
  }

  public String getWantedImageUrl() {
    return wantedImageUrl;
  }

  public void setWantedImageUrl(String wantedImageUrl) {
    this.wantedImageUrl = wantedImageUrl;
  }

  public String getDetectedBy() {
    if (TextUtils.isEmpty(detectedBy)) {
      detectedBy = "Not Available";
    }
    return detectedBy;
  }

  public void setDetectedBy(String detectedBy) {
    this.detectedBy = detectedBy;
  }

  public String getDisplayDate() {
    displayDate = ValidatorAndFormatter.getInstance().unixTimeStampToTimeAgo(time);
    return displayDate;
  }

  public String getArea() {
    if (TextUtils.isEmpty(area)) {
      area = "Not Available";
    }
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public List<String> getImgUrlListFromDB() {
    return imgUrlListFromDB;
  }

  public void setImgUrlListFromDB(List<String> imgUrlListFromDB) {
    this.imgUrlListFromDB = imgUrlListFromDB;
  }

  public long getTime_of_insertion() {
    return time_of_insertion;
  }

  public void setTime_of_insertion(long time_of_insertion) {
    this.time_of_insertion = time_of_insertion;
  }


  @Override
  public String toString() {
    return "BeanSocketObjFace{" +
        "type='" + type + '\'' +
        ", id='" + id + '\'' +
        ", time=" + time +
        ", confidence=" + confidence +
        ", imageBase64='" + imageBase64 + '\'' +
        ", imageUrl='" + imageUrl + '\'' +
        ", wantedPerson='" + wantedPerson + '\'' +
        ", wantedPersonName='" + wantedPersonName + '\'' +
        ", wantedImageBase64='" + wantedImageBase64 + '\'' +
        ", wantedImageUrl='" + wantedImageUrl + '\'' +
        '}';
  }
}
