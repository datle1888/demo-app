package com.jst.poc.dispatcher.room_db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "face_img_table", indices = {@Index(value = {"image"}, unique = true)})
public class EntityFaceImg {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "key_id")
  int keyId;

  @ColumnInfo(name = "id")
  @NonNull
  String id;


  @ColumnInfo(name = "image")
  String image;

  @ColumnInfo(name = "confidence")
  int confidence;


//    @ColumnInfo(name = "img1_url")
//    boolean img1_url;
//
//    @ColumnInfo(name = "img2_url")
//    boolean img2_url;
//
//    @ColumnInfo(name = "img3_url")
//    boolean img3_url;
//
//    @ColumnInfo(name = "img4_url")
//    boolean img4_url;


  public int getKeyId() {
    return keyId;
  }

  public void setKeyId(int keyId) {
    this.keyId = keyId;
  }

  @NonNull
  public String getId() {
    return id;
  }

  public void setId(@NonNull String id) {
    this.id = id;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }


  public int getConfidence() {
    return confidence;
  }

  public void setConfidence(int confidence) {
    this.confidence = confidence;
  }


  @Override
  public String toString() {
    return "EntityFaceImg{" +
        "keyId=" + keyId +
        ", id='" + id + '\'' +
        ", image='" + image + '\'' +
        ", confidence=" + confidence +
        '}';
  }
}
