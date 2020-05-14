package com.jst.poc.dispatcher.rest;


import androidx.annotation.Nullable;

import com.jst.poc.dispatcher.rest_responses.RespFetchJobs;
import com.jst.poc.dispatcher.rest_responses.RespFetchPlates;
import com.jst.poc.dispatcher.rest_responses.RespGeoReverseCode;
import com.jst.poc.dispatcher.rest_responses.RespGoogleDirections;
import com.jst.poc.dispatcher.rest_responses.RespUpdateJobStatus;
import com.jst.poc.dispatcher.rest_responses.RespUpdatePatrolStatus;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {
  @GET(IpClass.check_pending_job)
  Observable<RespFetchJobs> checkPendingJobs(@Query("apiKey") String apiKey, @Query("vehicleUniqueId") String vehicleUniqueId);

  @GET(IpClass.check_new_plates)
  Observable<RespFetchPlates> checkDetectedPlates(@Query("apiKey") String apiKey, @Query("vehicleUniqueId") String vehicleUniqueId);


  @GET(IpClass.fetch_completed_jobs)
  Observable<RespFetchJobs> fetchCompletedJobs(@Query("apiKey") String apiKey, @Query("vehicleUniqueId") String vehicleUniqueId);


  @POST(IpClass.update_job_status)
  Observable<RespUpdateJobStatus> updateJobStatus(@Query("jobCode") String jobCode, @Query("apiKey") String apiKey,
                                                  @Query("updateType") String updateType, @Query("jobStatus") String jobStatus,
                                                  @Nullable @Query("comments") String comments);

  @POST(IpClass.update_patrol_status)
  Observable<RespUpdatePatrolStatus> updatePatrolStatus(@Query("apiKey") String apiKey,
                                                        @Query("updateType") String updateType, @Query("status") String patrolStatus);

  @GET
  Observable<RespGoogleDirections> getDirection(@Url String url, @Query("origin") String origin,
                                                @Query("destination") String destination,
                                                @Query("key") String key);

  @GET
  Observable<RespGeoReverseCode> getGeoCode(@Url String url, @Query("latlng") String latlng, @Query("key") String key);
}
