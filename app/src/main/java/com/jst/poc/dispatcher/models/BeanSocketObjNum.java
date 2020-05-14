package com.jst.poc.dispatcher.models;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jst.poc.dispatcher.utilities.ValidatorAndFormatter;

public class BeanSocketObjNum {

  //************ Variables for display
  String timeAgo;
  long time_of_insertion;
  @SerializedName("type")
  @Expose
  private String type;
  @SerializedName("id")
  @Expose
  private String id;
  @SerializedName("plateNumber")
  @Expose
  private String plateNumber;
  @SerializedName("emirate")
  @Expose
  private String emirate;
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
  @SerializedName("wantedPlate")
  @Expose
  private String wantedPlate;
  @SerializedName("wantedPlateNumber")
  @Expose
  private String wantedPlateNumber;
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

  public String getPlateNumber() {
    return plateNumber;
  }

  public void setPlateNumber(String plateNumber) {
    this.plateNumber = plateNumber;
  }

  public String getEmirate() {
    return emirate;
  }

  public void setEmirate(String emirate) {
    this.emirate = emirate;
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

  public String getWantedPlate() {
    return wantedPlate;
  }

  public void setWantedPlate(String wantedPlate) {
    this.wantedPlate = wantedPlate;
  }

  public String getWantedPlateNumber() {
    return wantedPlateNumber;
  }

  public void setWantedPlateNumber(String wantedPlateNumber) {
    this.wantedPlateNumber = wantedPlateNumber;
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

  public String getTimeAgo() {
    timeAgo = ValidatorAndFormatter.getInstance().unixTimeStampToTimeAgo(time);
    return timeAgo;
  }

  public void setTimeAgo(String timeAgo) {
    this.timeAgo = timeAgo;
  }

  public long getTime_of_insertion() {
    return time_of_insertion;
  }

  public void setTime_of_insertion(long time_of_insertion) {
    this.time_of_insertion = time_of_insertion;
  }

  @Override
  public String toString() {
    return "BeanSocketObjNum{" +
        "type='" + type + '\'' +
        ", id='" + id + '\'' +
        ", plateNumber='" + plateNumber + '\'' +
        ", emirate='" + emirate + '\'' +
        ", time=" + time +
        ", confidence=" + confidence +
        ", imageBase64='" + imageBase64 + '\'' +
        ", imageUrl='" + imageUrl + '\'' +
        ", wantedPlate='" + wantedPlate + '\'' +
        ", wantedPlateNumber='" + wantedPlateNumber + '\'' +
        ", wantedImageBase64='" + wantedImageBase64 + '\'' +
        ", wantedImageUrl='" + wantedImageUrl + '\'' +
        '}';
  }
}
