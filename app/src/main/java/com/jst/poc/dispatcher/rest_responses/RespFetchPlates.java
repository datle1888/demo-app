package com.jst.poc.dispatcher.rest_responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jst.poc.dispatcher.models.BeanSocketObjNum;

import java.util.List;

public class RespFetchPlates {

  @SerializedName("status")
  @Expose
  private String status;
  @SerializedName("errorMessage")
  @Expose
  private String errorMessage;
  @SerializedName("data")
  @Expose
  private List<BeanSocketObjNum> data = null;
  @SerializedName("patrol_status")
  @Expose
  private String patrol_status;

  public String getPatrolStatus() {
    return patrol_status;
  }

  public void setPatrolStatus(String patrol_status) {
    this.patrol_status = patrol_status;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public List<BeanSocketObjNum> getData() {
    return data;
  }

  public void setData(List<BeanSocketObjNum> data) {
    this.data = data;
  }
}
