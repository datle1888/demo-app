package com.jst.poc.dispatcher.room_db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "number_table")
public class EntityNumberPlate {

  @PrimaryKey
  @ColumnInfo(name = "id")
  @NonNull
  String id;

  @ColumnInfo(name = "type")
  String type;

  @ColumnInfo(name = "time")
  long time;

  @ColumnInfo(name = "plateNumber")
  String plateNumber;


  @ColumnInfo(name = "emirate")
  String emirate;

  @ColumnInfo(name = "confidence")
  int confidence;


  @ColumnInfo(name = "detection_count")
  int detection_count;

  @ColumnInfo(name = "insert_time")
  long insert_time;


  @ColumnInfo(name = "image")
  String image;

  @ColumnInfo(name = "img_url")
  boolean img_url;

  @ColumnInfo(name = "dismissed")
  int dismissed;

  @ColumnInfo(name = "detected_by")
  String detected_by;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
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

  public int getConfidence() {
    return confidence;
  }

  public void setConfidence(int confidence) {
    this.confidence = confidence;
  }


  public int getDetection_count() {
    return detection_count;
  }

  public void setDetection_count(int detection_count) {
    this.detection_count = detection_count;
  }

  public long getInsert_time() {
    return insert_time;
  }

  public void setInsert_time(long insert_time) {
    this.insert_time = insert_time;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public boolean isImg_url() {
    return img_url;
  }

  public void setImg_url(boolean img_url) {
    this.img_url = img_url;
  }

  public int getDismissed() {
    return dismissed;
  }

  public void setDismissed(int dismissed) {
    this.dismissed = dismissed;
  }

  public String getDetected_by() {
    return detected_by;
  }

  public void setDetected_by(String detected_by) {
    this.detected_by = detected_by;
  }
}
