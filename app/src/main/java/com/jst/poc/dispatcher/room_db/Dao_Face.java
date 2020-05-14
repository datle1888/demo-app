package com.jst.poc.dispatcher.room_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.jst.poc.dispatcher.room_db.entities.EntFaceWithImg;
import com.jst.poc.dispatcher.room_db.entities.EntityFace;

import java.util.List;

@Dao
public interface Dao_Face {

  @Transaction
  @Query("SELECT * FROM face_table WHERE dismissed = 0 ORDER BY insert_time DESC LIMIT 100")
  List<EntFaceWithImg> getAllInsertedItem();

  @Query("UPDATE face_table SET detection_count = detection_count+1 WHERE id LIKE :id")
  void increment_detection_count(String id);

  @Query("UPDATE face_table SET dismissed = 1 WHERE id LIKE :id")
  void dismissFace(String id);


  @Query("SELECT detection_count FROM face_table WHERE id LIKE :id")
  int getDetectionCount(String id);

  @Query("SELECT dismissed FROM face_table WHERE id LIKE :id")
  int checkDismissed(String id);

  @Query("SELECT * FROM face_table WHERE id LIKE :id")
  EntityFace getSingleFace(String id);


  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertFace(EntityFace face);

  @Query("DELETE FROM face_table")
  void clearTable();

  @Query("DELETE FROM face_table WHERE id LIKE :id")
  void deleteSingleFace(String id);


  @Query("SELECT COUNT(id) FROM face_table")
  int getCount();

  @Query("SELECT SUM(detection_count) FROM face_table")
  int getTotalDetectionCount();


}
