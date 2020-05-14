package com.jst.poc.dispatcher.room_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.jst.poc.dispatcher.room_db.entities.EntityFaceImg;

import java.util.List;

@Dao
public interface Dao_FaceImg {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertImage(EntityFaceImg faceImg);

  @Query("DELETE FROM face_img_table")
  void clearTable();

  @Query("DELETE FROM face_img_table WHERE key_id IN ( SELECT key_id FROM face_img_table WHERE id LIKE :id ORDER BY confidence LIMIT 1)")
  void delete_raw_with_low_confidence(String id);

  @Query("DELETE FROM face_img_table WHERE id LIKE :id")
  void delete_all_rows_with_id(String id);


  @Query("SELECT * FROM face_img_table WHERE id LIKE :id")
  List<EntityFaceImg> getFaceImageList(String id);

  @Query("SELECT * FROM face_img_table WHERE id LIKE :id ORDER BY confidence LIMIT 1")
  EntityFaceImg getImageWithLowestConfidence(String id);


  @Query("SELECT COUNT(key_id) FROM face_img_table")
  int getCount();


}
