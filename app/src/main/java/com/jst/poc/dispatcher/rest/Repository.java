package com.jst.poc.dispatcher.rest;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.jst.poc.dispatcher.rest_responses.RespFetchJobs;
import com.jst.poc.dispatcher.rest_responses.RespFetchPlates;
import com.jst.poc.dispatcher.rest_responses.RespGeoReverseCode;
import com.jst.poc.dispatcher.rest_responses.RespGoogleDirections;
import com.jst.poc.dispatcher.rest_responses.RespUpdateJobStatus;
import com.jst.poc.dispatcher.rest_responses.RespUpdatePatrolStatus;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class Repository {
  ApiService apiService;
  //String apiKey = "FCC0FE0A58B0008E7F05D61579548B1D8A6D2E561E01A2F6CCA035F385BFBEE7";
  String apiKey = "XVMDDP335rkDsfsxf23ESs";

  @Inject
  Context context;
  String google_map_key;
  public Repository(ApiService apiService) {
    this.apiService = apiService;
    //MyApplication.getInstance().getAppComponent().doInjection(this);
//        google_map_key = context.getString(R.string.google_maps_key);
    google_map_key = "";
  }

  /**
   * Check for any pending job
   *
   * @param vehicleUniqueId
   * @return
   */
  public Observable<RespFetchJobs> checkPendingJob(String vehicleUniqueId) {
    return apiService.checkPendingJobs(apiKey, vehicleUniqueId);
  }

  /**
   * Check for new detected paltes
   *
   * @param vehicleUniqueId
   * @return
   */
  public Observable<RespFetchPlates> checkDetectedPlates(String vehicleUniqueId) {
    return apiService.checkDetectedPlates(apiKey, vehicleUniqueId);
  }


  /**
   * @param vehicleId
   * @return
   */
  public Observable<RespFetchJobs> fetchCompletedJobs(String vehicleId) {
    return apiService.fetchCompletedJobs(apiKey, vehicleId);
  }

  /**
   * Update Patrol Status
   *
   * @param status
   * @return
   */
  public Observable<RespUpdatePatrolStatus> updatePatrolStatus(PatrolStatus status) {
    return apiService.updatePatrolStatus(apiKey, "status", String.valueOf(status));
  }

  /**
   * Update Job Status
   *
   * @param jobId
   * @param comments
   * @param status
   * @return
   */
  public Observable<RespUpdateJobStatus> updateJobStatus(String jobId, String comments, JobStatus status) {
    return apiService.updateJobStatus(jobId, apiKey, "status", String.valueOf(status), comments);
  }


  /**
   * Get google direction points in order to plot route between two points
   *
   * @param start
   * @param end
   * @return
   */
  public Observable<RespGoogleDirections> getRoute(LatLng start, LatLng end) {
    String origin = start.latitude + "," + start.longitude;
    String destination = end.latitude + "," + end.longitude;
    return apiService.getDirection(IpClass.google_directions, origin, destination, google_map_key);
  }


  public Observable<RespGeoReverseCode> getGeoCode(LatLng latLng) {
    return apiService.getGeoCode(IpClass.google_geo_code_api, latLng.latitude + "," +
        latLng.longitude, google_map_key);
  }
}
