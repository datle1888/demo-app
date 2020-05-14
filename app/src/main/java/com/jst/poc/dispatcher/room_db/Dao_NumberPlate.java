package com.jst.poc.dispatcher.room_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.jst.poc.dispatcher.room_db.entities.EntityNumberPlate;

import java.util.List;

@Dao
public interface Dao_NumberPlate {

  @Query("SELECT * FROM number_table WHERE dismissed = 0 ORDER BY insert_time DESC LIMIT 100")
  List<EntityNumberPlate> getAllInsertedItem();

  @Query("UPDATE number_table SET detection_count = detection_count+1 WHERE id LIKE :id")
  void increment_detection_count(String id);

  @Query("UPDATE number_table SET dismissed = 1 WHERE id LIKE :id")
  void dismissPlate(String id);


  @Query("SELECT detection_count FROM number_table WHERE id LIKE :id")
  int getDetectionCount(String id);


  @Query("SELECT dismissed FROM number_table WHERE id LIKE :id")
  int checkDismissed(String id);


  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertNumberPlate(EntityNumberPlate numberPlate);

  @Query("DELETE FROM number_table")
  void clearTable();

  @Query("DELETE FROM number_table WHERE id LIKE :id")
  void deleteSinglePlate(String id);

  @Query("SELECT COUNT(id) FROM number_table")
  int getCount();

  @Query("SELECT * FROM number_table WHERE id LIKE :id")
  EntityNumberPlate getSinglePlate(String id);

  @Query("SELECT SUM(detection_count) FROM number_table")
  int getTotalDetectionCount();


}
