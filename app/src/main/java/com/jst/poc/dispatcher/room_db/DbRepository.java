package com.jst.poc.dispatcher.room_db;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jst.poc.dispatcher.models.BeanSocketObjFace;
import com.jst.poc.dispatcher.models.BeanSocketObjNum;
import com.jst.poc.dispatcher.models.BeanSocketObjWeapon;
import com.jst.poc.dispatcher.room_db.entities.EntFaceWithImg;
import com.jst.poc.dispatcher.room_db.entities.EntityFace;
import com.jst.poc.dispatcher.room_db.entities.EntityFaceImg;
import com.jst.poc.dispatcher.room_db.entities.EntityNumberPlate;
import com.jst.poc.dispatcher.room_db.entities.EntityWeapon;
import com.jst.poc.dispatcher.utilities.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DbRepository {

  private static final String TAG = "~~DbRepository";

  Dao_NumberPlate dao_numberPlate;

  Dao_Face dao_face;

  Dao_FaceImg dao_faceImg;

  Dao_Weapon dao_weapon;


  //Live data
  private MutableLiveData<BeanSocketObjFace> liveDataInsertedFaceData = new MutableLiveData<>();
  private MutableLiveData<BeanSocketObjNum> liveDataInsertedNumData = new MutableLiveData<>();
  private MutableLiveData<BeanSocketObjWeapon> liveDataInsertedWeapon = new MutableLiveData<>();
  private MutableLiveData<String> liveDataDismissFace = new MutableLiveData<>();
  private MutableLiveData<String> liveDataDismissPlate = new MutableLiveData<>();
  private MutableLiveData<String> liveDataDismissWeapon = new MutableLiveData<>();

  @Inject
  public DbRepository(Dao_NumberPlate dao_numberPlate, Dao_Face dao_face, Dao_FaceImg dao_faceImg, Dao_Weapon dao_weapon) {
    this.dao_numberPlate = dao_numberPlate;
    this.dao_face = dao_face;
    this.dao_faceImg = dao_faceImg;
    this.dao_weapon = dao_weapon;
  }

  //Live data getter methods
  public LiveData<BeanSocketObjFace> getLiveDataInsertedFaceData() {
    return liveDataInsertedFaceData;
  }

  public LiveData<BeanSocketObjNum> getLiveDataInsertedNumData() {
    return liveDataInsertedNumData;
  }

  public LiveData<BeanSocketObjWeapon> getLiveDataInsertedWeapon() {
    return liveDataInsertedWeapon;
  }

  public LiveData<String> getLiveDataDismissFace() {
    return liveDataDismissFace;
  }

  public LiveData<String> getLiveDataDismissPlate() {
    return liveDataDismissPlate;
  }

  public LiveData<String> getLiveDataDismissWeapon() {
    return liveDataDismissWeapon;
  }


  //-------------------------------------------------------------------------------
  //                      Start Of Operations Related to Face
  // ------------------------------------------------------------------------------

  public void insertFace(BeanSocketObjFace face) {

    boolean item_inserted = false;

    //If wanted person == yes , do not insert to db ---> Ignore case  1
    if (face.getWantedPerson().equals("yes")) {
      return;
    }

    //************** face insertion ********************

    //How many times this id detected
    int detect_count = dao_face.getDetectionCount(face.getId());

    //If is_dismissed = 1 --> Item dismissed , else = 0 --> item not dismissed
    int is_dismissed = dao_face.checkDismissed(face.getId());

    //If any item with this id present, it is taking as as old face
    EntityFace oldFace = dao_face.getSingleFace(face.getId());


    //If old id and new face id same & time stamps same, do not insert to db ---> Ignore case 2
    if (oldFace != null) {

      String old_face_id = oldFace.getId();

      long old_face_time_stamp = oldFace.getTime();

      if (!TextUtils.isEmpty(old_face_id)) { //enter inside this condition iff old_face_id not null

        if (old_face_id.equals(face.getId()) && old_face_time_stamp == face.getTime()) {

          //Do not insert the item, return from method

          return;
        }

      }

    }

    //This entity is inserting to face table ( face table contain face details )
    EntityFace entityFace = new EntityFace();
    entityFace.setId(face.getId());
    entityFace.setWantedPersonName(TextUtils.isEmpty(face.getWantedPersonName()) ? "No Name" : face.getWantedPersonName());
    entityFace.setTime(face.getTime());
    entityFace.setType(face.getType());
    entityFace.setConfidence(face.getConfidence());
    entityFace.setDetection_count(detect_count + 1);
    entityFace.setInsert_time(System.currentTimeMillis());
    entityFace.setDismissed(is_dismissed);
    entityFace.setDetected_by(face.getDetectedBy());
    entityFace.setArea(face.getArea());


    //-------------------------------------------------
    //          Insertion part of face item
    //------------------------------------------------
    if (oldFace == null) { // ------------------------------------------------------->Face data insert condition 1

      dao_face.insertFace(entityFace);

    } else if (oldFace.getConfidence() < face.getConfidence()) { // ----------------->Face data insert condition 2

      dao_face.insertFace(entityFace);

    } else { // ---------------------------------------------------->Face with id detection count increment condition

      incrementFaceDetectCount(face.getId());

    }


    //**************** Insertion part of face item end here ****************

    //--------------------------------------------------------------
    //           Face Image Insertion to face image table
    //--------------------------------------------------------------

    //Creating and setting Face Image Entity. This is the item inserting to face image table
    EntityFaceImg entityFaceImg = new EntityFaceImg();
    entityFaceImg.setId(face.getId());
    entityFaceImg.setImage(face.getImageUrl());
    entityFaceImg.setConfidence(face.getConfidence());

    Logger.getInstance().verbose_log(TAG, entityFaceImg.toString());

    //Getting all face images with "id" to a list.This later need to
    List<EntityFaceImg> faceImageList = dao_faceImg.getFaceImageList(face.getId());

    //Taking image with lowest confidence, this is need in future comparison for insertion
    EntityFaceImg lowestConfImg = dao_faceImg.getImageWithLowestConfidence(face.getId());

    //Determining this image insertable
    boolean imageInsertable = (lowestConfImg == null || lowestConfImg.getConfidence() < face.getConfidence());


    if (faceImageList.size() > 3) {// ----------------------------------->Face image insert condition 1

      //Count of existing images with this face id is 4 , if image insertable,
      // then delete image with lowest confidence  and insert image

      if (imageInsertable) {

        //Delete the image with lowest confidence
        dao_faceImg.delete_raw_with_low_confidence(face.getId());

        String count = " dao_face " + dao_face.getCount() + " dao_face_img " + dao_faceImg.getCount();

        Logger.getInstance().verbose_log(TAG, "deleted raw & count " + lowestConfImg.getKeyId() + count + "\n************\n");


        dao_faceImg.insertImage(entityFaceImg);

        item_inserted = true;

      }


    } else {// ----------------------------------->Face image insert condition 2

      //If image list size < 4, insert this image

      dao_faceImg.insertImage(entityFaceImg);

      item_inserted = true;

    }


    String count = " dao_face " + dao_face.getCount() + " dao_face_img " + dao_faceImg.getCount();

    Logger.getInstance().verbose_log(TAG, "Count after insertion " + count + "\n************\n");

    //Checking whether face item inserted,
    // then we have to notify the list showing activity
    if (item_inserted) {

      //This object is passing to activity,  setting all the images as a list in this object
      face.setImgUrlListFromDB(faceImgUrlsFromEntityFace(faceImageList));

      //Notifying activity that image inserted, with newly inserted item
      liveDataInsertedFaceData.setValue(face);

    }


  }

  public void deleteAllDetectedFace() {

    dao_faceImg.clearTable();

    dao_face.clearTable();

  }

  public List<BeanSocketObjFace> getAllDetectedFace(int offset) {

    List<BeanSocketObjFace> returnList = new ArrayList<>();


    for (EntFaceWithImg w : dao_face.getAllInsertedItem()) {

      //Extract entity face
      EntityFace entityFace = w.face;

      //List entity face image
      List<EntityFaceImg> faceImgList = w.faceImgs;


      BeanSocketObjFace face = entityFaceToBeansFace(entityFace);
      face.setImgUrlListFromDB(faceImgUrlsFromEntityFace(faceImgList));


      //Insertion to hash map
      returnList.add(face);

    }

    Logger.getInstance().verbose_log(TAG, "hash map size " + returnList.size());


    return returnList;
  }

  public void deleteSingleFace(String id) {

    dao_faceImg.delete_all_rows_with_id(id);

    dao_face.deleteSingleFace(id);

  }

  public int getFaceDetectedCount() {
    return dao_face.getTotalDetectionCount();
  }

  private void incrementFaceDetectCount(String id) {
    dao_face.increment_detection_count(id);
  }

  private BeanSocketObjFace entityFaceToBeansFace(EntityFace entityFace) {

    BeanSocketObjFace objFace = new BeanSocketObjFace();

    objFace.setId(entityFace.getId());
    objFace.setConfidence(entityFace.getConfidence());
    objFace.setTime(entityFace.getTime());
    objFace.setType(entityFace.getType());
    objFace.setWantedPersonName(entityFace.getWantedPersonName());
    objFace.setWantedPerson("no");
    objFace.setDetectedBy(entityFace.getDetected_by());
    objFace.setArea(entityFace.getArea());
    objFace.setTime_of_insertion(entityFace.getInsert_time());

    return objFace;

  }

  private List<String> faceImgUrlsFromEntityFace(List<EntityFaceImg> faceImgList) {

    List<String> imageListOutPut = new ArrayList<>();

    //Insertion output list
    for (EntityFaceImg e : faceImgList) {

      imageListOutPut.add(e.getImage());

    }

    Logger.getInstance().verbose_log(null, " return list size " + imageListOutPut.size());

    return imageListOutPut;

  }

  public void dismissFaceItem(String id) {

    dao_face.dismissFace(id);


    liveDataDismissFace.setValue(id);

  }


  //-------------------------------------------------------------------------------
  //                      Start Of Operations Related to Number plate
  // ------------------------------------------------------------------------------

  public void insertNumberPlate(BeanSocketObjNum num) {

    boolean item_inserted = false;

    //If wanted plate == yes , then skip insertion
    if (num.getWantedPlate().equals("yes")) {
      return;
    }

    int detect_count = dao_numberPlate.getDetectionCount(num.getId());
    int isDismissed = dao_numberPlate.checkDismissed(num.getId());

    EntityNumberPlate oldPlate = dao_numberPlate.getSinglePlate(num.getId());


    //If old id and new face id same , check time stamp if , that same, ignore
    if (oldPlate != null) {

      String old_plate_id = oldPlate.getId();
      long old_plate_time_stamp = oldPlate.getTime();

      if (!TextUtils.isEmpty(old_plate_id)) {

        if (old_plate_id.equals(num.getId()) && old_plate_time_stamp == num.getTime()) {

          return;
        }

      }

    }


    EntityNumberPlate plate = new EntityNumberPlate();

    plate.setId(num.getId());
    plate.setConfidence(num.getConfidence());
    plate.setPlateNumber(num.getPlateNumber());
    plate.setTime(num.getTime());
    plate.setImage(num.getImageBase64());
    plate.setImg_url(false);
    plate.setEmirate(TextUtils.isEmpty(num.getEmirate()) ? "No data" : num.getEmirate());
    plate.setType(num.getType());
    plate.setDetection_count(detect_count + 1);
    plate.setInsert_time(System.currentTimeMillis());
    plate.setDismissed(isDismissed);
    plate.setDetected_by(num.getDetectedBy());


    if (oldPlate == null) {

      dao_numberPlate.insertNumberPlate(plate);

      item_inserted = true;

    } else if (num.getConfidence() > oldPlate.getConfidence()) {

      dao_numberPlate.insertNumberPlate(plate);

      item_inserted = true;

    } else {

      incrementPlateDetectCount(num.getId());

      item_inserted = false;
    }

    Logger.getInstance().verbose_log(TAG, "number plate count after insertion " + dao_numberPlate.getCount());

    if (item_inserted) {

      Logger.getInstance().verbose_log(TAG, "number plate count after insertion  item inserted " + dao_numberPlate.getCount());

      liveDataInsertedNumData.setValue(num);
    }


  }

  public void deleteAllNumberPlate() {

    dao_numberPlate.clearTable();
  }

  public void deleteSingleNumberPlate(String id) {

    dao_numberPlate.deleteSinglePlate(id);
  }

  public List<BeanSocketObjNum> getAllNumberPlate(int offset) {

    List<BeanSocketObjNum> numList = new ArrayList<>();

    for (EntityNumberPlate en : dao_numberPlate.getAllInsertedItem()) {

      BeanSocketObjNum objNum = new BeanSocketObjNum();

      objNum.setId(en.getId());
      objNum.setConfidence(en.getConfidence());
      objNum.setEmirate(en.getEmirate());
      objNum.setImageUrl("");
      objNum.setImageBase64(en.getImage());
      objNum.setPlateNumber(en.getPlateNumber());
      objNum.setType(en.getType());
      objNum.setTime(en.getTime());
      objNum.setDetectedBy(en.getDetected_by());
      objNum.setTime_of_insertion(en.getInsert_time());

      //Add object to return list
      numList.add(objNum);

    }


    return numList;

  }

  public int getDetectedPlateCount() {
    return dao_numberPlate.getTotalDetectionCount();
  }

  private void incrementPlateDetectCount(String id) {
    dao_numberPlate.increment_detection_count(id);
  }

  public void dismissPlate(String id) {

    dao_numberPlate.dismissPlate(id);

    liveDataDismissPlate.setValue(id);
  }


  //-------------------------------------------------------------------------------
  //                      Start Of Operations Related to Weapons
  // ------------------------------------------------------------------------------

  public void insertWeapon(BeanSocketObjWeapon weapon) {

    boolean item_inserted = false;

//        //If wanted plate == yes , then skip insertion
//        if (weapon.getWantedPlate().equals("yes")){
//            return;
//        }

    int detect_count = dao_weapon.getDetectionCount(weapon.getId());
    int isDismissed = dao_weapon.checkDismissed(weapon.getId());

    EntityWeapon oldWeapon = dao_weapon.getSingleWeapon(weapon.getId());


    //If old id and new face id same , check time stamp if , that same, ignore
    if (oldWeapon != null) {

      String old_weapon_id = oldWeapon.getId();
      long old_weapon_time_stamp = oldWeapon.getTime();

      if (!TextUtils.isEmpty(old_weapon_id)) {

        if (old_weapon_id.equals(weapon.getId()) && old_weapon_time_stamp == weapon.getTime()) {

          return;
        }

      }

    }


    EntityWeapon ent_weapon = new EntityWeapon();

    ent_weapon.setId(weapon.getId());

    ent_weapon.setConfidence(weapon.getConfidence());
    ent_weapon.setTime(weapon.getTime());
    ent_weapon.setPerson_image(weapon.getFaceImageUrl());
    ent_weapon.setWeapon_image(weapon.getWeaponImageUrl());
    ent_weapon.setLocation(TextUtils.isEmpty(weapon.getArea()) ? "No data" : weapon.getArea());
    ent_weapon.setType(weapon.getType());
    ent_weapon.setDetection_count(detect_count + 1);
    ent_weapon.setInsert_time(System.currentTimeMillis());
    ent_weapon.setDismissed(isDismissed);


    if (oldWeapon == null) {

      dao_weapon.insertWeapon(ent_weapon);

      item_inserted = true;

    } else if (weapon.getConfidence() > oldWeapon.getConfidence()) {

      dao_weapon.insertWeapon(ent_weapon);

      item_inserted = true;

    } else {

      incrementWeaponDetectCount(weapon.getId());

      item_inserted = false;
    }

    Logger.getInstance().verbose_log(TAG, "weapon count after insertion " + dao_weapon.getCount());

    if (item_inserted) {

      liveDataInsertedWeapon.setValue(weapon);
    }


  }

  public void deleteAllWeapon() {

    dao_weapon.clearTable();
  }

  public void deleteSingleWeapon(String id) {

    dao_weapon.deleteSingleWeapon(id);
  }

  public List<BeanSocketObjWeapon> getAllWeapon(int offset) {

    List<BeanSocketObjWeapon> weaponList = new ArrayList<>();

    for (EntityWeapon en : dao_weapon.getAllInsertedItem()) {

      BeanSocketObjWeapon objWeapon = new BeanSocketObjWeapon();

      objWeapon.setId(en.getId());
      objWeapon.setConfidence(en.getConfidence());
      objWeapon.setArea(en.getLocation());
      objWeapon.setFaceImageUrl(en.getPerson_image());
      objWeapon.setWeaponImageUrl(en.getWeapon_image());
      objWeapon.setType(en.getType());
      objWeapon.setTime(en.getTime());
      objWeapon.setTime_of_insertion(en.getInsert_time());

      //Add object to return list
      weaponList.add(objWeapon);

    }

    Logger.getInstance().verbose_log(TAG, "return weapon list size " + weaponList.size());

    return weaponList;

  }

  public int getDetectedWeaponCount() {
    return dao_weapon.getTotalDetectionCount();
  }

  private void incrementWeaponDetectCount(String id) {
    dao_weapon.increment_detection_count(id);
  }

  public void dismissWeapon(String id) {

    dao_weapon.dismissWeapon(id);

    liveDataDismissWeapon.setValue(id);
  }


}
