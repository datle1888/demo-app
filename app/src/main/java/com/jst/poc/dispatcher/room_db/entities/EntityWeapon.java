package com.jst.poc.dispatcher.room_db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weapon_table")
public class EntityWeapon {

  @PrimaryKey
  @ColumnInfo(name = "id")
  @NonNull
  String id;

  @ColumnInfo(name = "type")
  String type;

  @ColumnInfo(name = "time")
  long time;

  @ColumnInfo(name = "confidence")
  int confidence;


  @ColumnInfo(name = "detection_count")
  int detection_count;

  @ColumnInfo(name = "insert_time")
  long insert_time;


  @ColumnInfo(name = "dismissed")
  int dismissed;

  @ColumnInfo(name = "person_img")
  String person_image;

  @ColumnInfo(name = "weapon_img")
  String weapon_image;

  @ColumnInfo(name = "location")
  String location;


  @NonNull
  public String getId() {
    return id;
  }

  public void setId(@NonNull String id) {
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

  public int getDismissed() {
    return dismissed;
  }

  public void setDismissed(int dismissed) {
    this.dismissed = dismissed;
  }

  public String getPerson_image() {
    return person_image;
  }

  public void setPerson_image(String person_image) {
    this.person_image = person_image;
  }

  public String getWeapon_image() {
    return weapon_image;
  }

  public void setWeapon_image(String weapon_image) {
    this.weapon_image = weapon_image;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }
}
