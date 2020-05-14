package com.jst.poc.dispatcher.room_db.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class EntFaceWithImg {
  @Embedded
  public EntityFace face;
  @Relation(parentColumn = "id", entityColumn = "id")
  public List<EntityFaceImg> faceImgs;
}
