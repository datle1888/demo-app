package com.jst.poc.dispatcher.room_db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.jst.poc.dispatcher.room_db.entities.EntityWeapon;

import java.util.List;

@Dao
public interface Dao_Weapon {

  @Query("SELECT * FROM weapon_table WHERE dismissed = 0 ORDER BY insert_time DESC LIMIT 100")
  List<EntityWeapon> getAllInsertedItem();

  @Query("UPDATE weapon_table SET detection_count = detection_count+1 WHERE id LIKE :id")
  void increment_detection_count(String id);

  @Query("UPDATE weapon_table SET dismissed = 1 WHERE id LIKE :id")
  void dismissWeapon(String id);


  @Query("SELECT detection_count FROM weapon_table WHERE id LIKE :id")
  int getDetectionCount(String id);


  @Query("SELECT dismissed FROM weapon_table WHERE id LIKE :id")
  int checkDismissed(String id);


  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertWeapon(EntityWeapon weapon);

  @Query("DELETE FROM weapon_table")
  void clearTable();

  @Query("DELETE FROM weapon_table WHERE id LIKE :id")
  void deleteSingleWeapon(String id);

  @Query("SELECT COUNT(id) FROM weapon_table")
  int getCount();

  @Query("SELECT * FROM weapon_table WHERE id LIKE :id")
  EntityWeapon getSingleWeapon(String id);

  @Query("SELECT SUM(detection_count) FROM weapon_table")
  int getTotalDetectionCount();

}
