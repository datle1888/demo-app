package com.jst.poc.dispatcher.models;

import com.entraze.rxwebsocket.SocketEventTypeEnum;

public class BeanSocketEventObj {
  SocketEventTypeEnum typeEnum;
  Object object;
  public SocketEventTypeEnum getTypeEnum() {
    return typeEnum;
  }
  public void setTypeEnum(SocketEventTypeEnum typeEnum) {
    this.typeEnum = typeEnum;
  }
  public Object getObject() {
    return object;
  }
  public void setObject(Object object) {
    this.object = object;
  }
}
