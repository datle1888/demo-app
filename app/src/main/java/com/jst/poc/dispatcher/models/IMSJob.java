package com.jst.poc.dispatcher.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jst.poc.dispatcher.utilities.ValidatorAndFormatter;

public class IMSJob {

  String job_time_display;
  String eta_display;
  String distance_display;
  String job_id_display;
  @SerializedName("id")
  @Expose
  private String id;
  @SerializedName("code")
  @Expose
  private String code;
  @SerializedName("title")
  @Expose
  private String title;
  @SerializedName("description")
  @Expose
  private String description;
  @SerializedName("category")
  @Expose
  private String category;
  @SerializedName("type")
  @Expose
  private String type;
  @SerializedName("status")
  @Expose
  private String status;
  @SerializedName("reportedBy")
  @Expose
  private String reportedBy;
  @SerializedName("reportedMobile")
  @Expose
  private String reportedMobile;
  @SerializedName("location")
  @Expose
  private String location;
  @SerializedName("reportedTime")
  @Expose
  private String reportedTime;
  @SerializedName("createdTime")
  @Expose
  private String createdTime;
  @SerializedName("assignedTime")
  @Expose
  private String assignedTime;
  @SerializedName("assignAknowledgeTime")
  @Expose
  private String assignAknowledgeTime;
  @SerializedName("lastStatusUpdateTime")
  @Expose
  private String lastStatusUpdateTime;
  @SerializedName("vehicleCurrentLocation")
  @Expose
  private String vehicleCurrentLocation;
  @SerializedName("vehicleCurrentLocationTime")
  @Expose
  private String vehicleCurrentLocationTime;
  @SerializedName("eta")
  @Expose
  private String eta;
  @SerializedName("etaReportedTime")
  @Expose
  private String etaReportedTime;
  @SerializedName("comments")
  @Expose
  private String comments;
  @SerializedName("arrivedTime")
  @Expose
  private String arrivedTime;
  @SerializedName("completedTime")
  @Expose
  private String completedTime;
  @SerializedName("acceptedTime_text")
  @Expose
  private String acceptedTimeText;
  @SerializedName("arrivedTime_text")
  @Expose
  private String arrivedTimeText;
  @SerializedName("completedTime_text")
  @Expose
  private String completedTimeText;
  @SerializedName("area")
  @Expose
  private String area;
  @SerializedName("street")
  @Expose
  private String street;
  @SerializedName("landmark")
  @Expose
  private String landmark;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getReportedBy() {
    return reportedBy;
  }

  public void setReportedBy(String reportedBy) {
    this.reportedBy = reportedBy;
  }

  public String getReportedMobile() {
    return reportedMobile;
  }

  public void setReportedMobile(String reportedMobile) {
    this.reportedMobile = reportedMobile;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getReportedTime() {
    return reportedTime;
  }

  public void setReportedTime(String reportedTime) {
    this.reportedTime = reportedTime;
  }

  public String getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(String createdTime) {
    this.createdTime = createdTime;
  }

  public String getAssignedTime() {
    return assignedTime;
  }

  public void setAssignedTime(String assignedTime) {
    this.assignedTime = assignedTime;
  }

  public String getAssignAknowledgeTime() {
    return assignAknowledgeTime;
  }

  public void setAssignAknowledgeTime(String assignAknowledgeTime) {
    this.assignAknowledgeTime = assignAknowledgeTime;
  }

  public String getLastStatusUpdateTime() {
    return lastStatusUpdateTime;
  }

  public void setLastStatusUpdateTime(String lastStatusUpdateTime) {
    this.lastStatusUpdateTime = lastStatusUpdateTime;
  }

  public String getVehicleCurrentLocation() {
    return vehicleCurrentLocation;
  }

  public void setVehicleCurrentLocation(String vehicleCurrentLocation) {
    this.vehicleCurrentLocation = vehicleCurrentLocation;
  }

  public String getVehicleCurrentLocationTime() {
    return vehicleCurrentLocationTime;
  }

  public void setVehicleCurrentLocationTime(String vehicleCurrentLocationTime) {
    this.vehicleCurrentLocationTime = vehicleCurrentLocationTime;
  }

  public String getEta() {
    return eta;
  }

  public void setEta(String eta) {
    this.eta = eta;
  }

  public String getEtaReportedTime() {
    return etaReportedTime;
  }

  public void setEtaReportedTime(String etaReportedTime) {
    this.etaReportedTime = etaReportedTime;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public String getArrivedTime() {
    return arrivedTime;
  }

  public void setArrivedTime(String arrivedTime) {
    this.arrivedTime = arrivedTime;
  }

  public String getCompletedTime() {
    return completedTime;
  }

  public void setCompletedTime(String completedTime) {
    this.completedTime = completedTime;
  }

  public String getAcceptedTimeText() {
    return acceptedTimeText;
  }

  public void setAcceptedTimeText(String acceptedTimeText) {
    this.acceptedTimeText = acceptedTimeText;
  }

  public String getArrivedTimeText() {
    return arrivedTimeText;
  }

  public void setArrivedTimeText(String arrivedTimeText) {
    this.arrivedTimeText = arrivedTimeText;
  }

  public String getCompletedTimeText() {
    return completedTimeText;
  }

  public void setCompletedTimeText(String completedTimeText) {
    this.completedTimeText = completedTimeText;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }


  //Formatted variables

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getLandmark() {
    return landmark;
  }

  public void setLandmark(String landmark) {
    this.landmark = landmark;
  }

  public String getJob_time_display() {
    job_time_display = ValidatorAndFormatter.getInstance().displayDateAndTimeJob(getReportedTime());
    return job_time_display;
  }

  public String getEta_display() {
    eta_display = eta + " min";
    return eta_display;
  }

  public String getDistance_display() {
    distance_display = ValidatorAndFormatter.getInstance().getDistance(getVehicleCurrentLocation(), location);
    return distance_display;
  }

  public String getJob_id_display() {
    job_id_display = "#" + getId();
    return job_id_display;
  }
}
