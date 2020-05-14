package com.jst.poc.dispatcher.rest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.jst.poc.dispatcher.rest.Status.ERROR;
import static com.jst.poc.dispatcher.rest.Status.LOADING;
import static com.jst.poc.dispatcher.rest.Status.SUCCESS;

public class ApiResponse {

  public static PatrolStatus patrolStatus;
  public final Status status;
  @Nullable
  public final Throwable error;
  @Nullable
  public Object data;

  public ApiResponse(Status status, @Nullable Object data, @Nullable PatrolStatus patrolStatus, @Nullable Throwable error) {
    this.status = status;
    if (patrolStatus == null && ApiResponse.patrolStatus == null)
      ApiResponse.patrolStatus = PatrolStatus.loading;
    else
      ApiResponse.patrolStatus = patrolStatus;
    this.data = data;
    this.error = error;
  }

  public static ApiResponse loading() {
    return new ApiResponse(LOADING, null, null, null);
  }

  public static ApiResponse success(@NonNull Object data, @Nullable PatrolStatus patrolStatus) {
    return new ApiResponse(SUCCESS, data, patrolStatus, null);
  }

  public static ApiResponse error(@NonNull Throwable error) {
    return new ApiResponse(ERROR, null, null, error);
  }

  public String responseString() {
    return "Data: " + data.toString() + ", status: " + this.status.name() + " patrol_status: " + patrolStatus.name();
  }
}
