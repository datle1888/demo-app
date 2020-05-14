package com.jst.poc.dispatcher.room_db;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomDBModule {


  AppDataBase appDataBase;

  public RoomDBModule(Context context) {
    appDataBase = Room.databaseBuilder(context, AppDataBase.class, AppDataBase.DB_NAME)
        .fallbackToDestructiveMigration()
        .allowMainThreadQueries()
        .build();
  }


  @Singleton
  @Provides
  AppDataBase provideAppDataBase() {

    return appDataBase;
  }


  @Singleton
  @Provides
  Dao_Face dao_face(AppDataBase appDataBase) {
    return appDataBase.dao_face();
  }

  @Singleton
  @Provides
  Dao_FaceImg dao_faceImg(AppDataBase appDataBase) {
    return appDataBase.dao_faceImg();
  }


  @Singleton
  @Provides
  Dao_NumberPlate dao_numberPlate(AppDataBase appDataBase) {
    return appDataBase.dao_numberPlate();
  }

  @Singleton
  @Provides
  Dao_Weapon dao_weapon(AppDataBase appDataBase) {
    return appDataBase.dao_weapon();
  }


  @Singleton
  @Provides
  DbRepository provideDbRepository(Dao_Face dao_face, Dao_NumberPlate dao_numberPlate, Dao_FaceImg dao_faceImg, Dao_Weapon dao_weapon) {
    return new DbRepository(dao_numberPlate, dao_face, dao_faceImg, dao_weapon);
  }


}
