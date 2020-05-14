package com.jst.poc.dispatcher.room_db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.jst.poc.dispatcher.room_db.entities.EntityFace;
import com.jst.poc.dispatcher.room_db.entities.EntityFaceImg;
import com.jst.poc.dispatcher.room_db.entities.EntityNumberPlate;
import com.jst.poc.dispatcher.room_db.entities.EntityWeapon;

@Database(entities = {EntityFace.class, EntityNumberPlate.class, EntityFaceImg.class, EntityWeapon.class},
    version = 20)
public abstract class AppDataBase extends RoomDatabase {

  static String DB_NAME = "DubaiPolice_DB";

  public abstract Dao_Face dao_face();

  public abstract Dao_NumberPlate dao_numberPlate();

  public abstract Dao_FaceImg dao_faceImg();

  public abstract Dao_Weapon dao_weapon();


}
