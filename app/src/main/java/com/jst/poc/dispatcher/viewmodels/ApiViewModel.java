package com.jst.poc.dispatcher.viewmodels;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.jst.poc.dispatcher.models.BeanJob;
import com.jst.poc.dispatcher.models.BeanSocketObjNum;
import com.jst.poc.dispatcher.rest.ApiResponse;
import com.jst.poc.dispatcher.rest.JobStatus;
import com.jst.poc.dispatcher.rest.PatrolStatus;
import com.jst.poc.dispatcher.rest.Repository;
import com.jst.poc.dispatcher.rest_responses.RespFetchJobs;
import com.jst.poc.dispatcher.rest_responses.RespFetchPlates;
import com.jst.poc.dispatcher.rest_responses.RespGeoReverseCode;
import com.jst.poc.dispatcher.rest_responses.RespGoogleDirections;
import com.jst.poc.dispatcher.rest_responses.RespUpdateJobStatus;
import com.jst.poc.dispatcher.rest_responses.RespUpdatePatrolStatus;
import com.jst.poc.dispatcher.utilities.LocationService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ApiViewModel extends ViewModel {

  public static String current_location_address = null;
  private final CompositeDisposable disposables = new CompositeDisposable();
  private String stat_ok = "ok", stat_error = "error";
  private String vehicleId = "DPPATROL01";
  private LatLng origin = null, dest = null;
  private List<LatLng> routes = null;
  //Local variables
  private Repository repository;
  //Live data
  private MutableLiveData<ApiResponse> liveDataJobsCheck = new MutableLiveData<>();
  private MutableLiveData<ApiResponse> liveDataTime = new MutableLiveData<>();
  private MutableLiveData<ApiResponse> liveDataJobStat = new MutableLiveData<>();
  private MutableLiveData<ApiResponse> liveDataCompletedJobs = new MutableLiveData<>();
  private MutableLiveData<ApiResponse> liveDataRoutes = new MutableLiveData<>();
  private MutableLiveData<ApiResponse> liveDataPlates = new MutableLiveData<>();


  //Constructor
  public ApiViewModel(Repository repository) {
    this.repository = repository;
  }

  //Getter
  public LiveData<ApiResponse> getLiveDataJobsCheck() {
    return liveDataJobsCheck;
  }

  //Getter
  public LiveData<ApiResponse> getLiveDataPlates() {
    return liveDataPlates;
  }

  public LiveData<ApiResponse> getLiveDataTime() {
    return liveDataTime;
  }

  public LiveData<ApiResponse> getLiveDataJobStat() {
    return liveDataJobStat;
  }

  public LiveData<ApiResponse> getLiveDataCompletedJobs() {
    return liveDataCompletedJobs;
  }

  public LiveData<ApiResponse> getLiveDataRoutes() {
    return liveDataRoutes;
  }

  //ApiCall
  public void apiCallCheckPendingJob() {

    disposables.add(repository.checkPendingJob(vehicleId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override
          public void accept(Disposable disposable) throws Exception {
            liveDataJobsCheck.setValue(ApiResponse.loading());
          }
        })
        .subscribe(new Consumer<RespFetchJobs>() {
          @Override
          public void accept(RespFetchJobs resp) throws Exception {
            BeanJob pendingJob = null;
            try {
              if (resp.getStatus().equals("ok") && !resp.getData().isEmpty()) {

                pendingJob = resp.getData().get(0);
              }
            } catch (Exception e) {

              e.printStackTrace();
            }
            liveDataJobsCheck.setValue(ApiResponse.success(pendingJob, PatrolStatus.valueOf(resp.getPatrolStatus())));
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            liveDataJobsCheck.setValue(ApiResponse.error(throwable));
          }
        }));
  }

  //ApiCall
  public void apiCallCheckDetectedPlates() {
    disposables.add(repository.checkDetectedPlates(vehicleId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override
          public void accept(Disposable disposable) throws Exception {
            liveDataPlates.setValue(ApiResponse.loading());
          }
        })
        .subscribe(new Consumer<RespFetchPlates>() {
          @Override
          public void accept(RespFetchPlates resp) throws Exception {
            List<BeanSocketObjNum> newPlates = null;
            try {
              if (resp.getStatus().equals("ok") && !resp.getData().isEmpty()) {
                Log.v("TESTPLATE", "Plates response ok");
                newPlates = resp.getData();
              } else
                Log.v("TESTPLATE", "Plates response empty");
            } catch (Exception e) {
              Log.e("TESTPLATE", e.getLocalizedMessage());
              e.printStackTrace();
            }
            liveDataPlates.setValue(ApiResponse.success(newPlates, PatrolStatus.valueOf(resp.getPatrolStatus())));
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            liveDataPlates.setValue(ApiResponse.error(throwable));
          }
        }));
  }

  public void apiCallUpdateJobStatus(String jobId, String comments, final JobStatus status) {
    disposables.add(repository.updateJobStatus(jobId, comments, status)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override
          public void accept(Disposable disposable) throws Exception {
            liveDataJobStat.setValue(ApiResponse.loading());
          }
        })
        .subscribe(new Consumer<RespUpdateJobStatus>() {
          @Override
          public void accept(RespUpdateJobStatus respUpdateJobStatus) throws Exception {
            try {
              String jobStatus = respUpdateJobStatus.getStatus();
              if (jobStatus.equals(stat_ok)) {
                liveDataJobStat.setValue(ApiResponse.success(status, PatrolStatus.valueOf(respUpdateJobStatus.getPatrolStatus())));
              } else {
                liveDataJobStat.setValue(ApiResponse.success(null, PatrolStatus.valueOf(respUpdateJobStatus.getPatrolStatus())));
              }
            } catch (Exception e) {
              e.printStackTrace();
              liveDataJobStat.setValue(ApiResponse.success(null, PatrolStatus.valueOf(respUpdateJobStatus.getPatrolStatus())));
            }
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            liveDataJobStat.setValue(ApiResponse.error(throwable));
          }
        }));
  }

  public void apiCallUpdatePatrolStatus(final PatrolStatus status) {
    disposables.add(repository.updatePatrolStatus(status)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override
          public void accept(Disposable disposable) throws Exception {
            liveDataJobStat.setValue(ApiResponse.loading());
          }
        })
        .subscribe(new Consumer<RespUpdatePatrolStatus>() {
          @Override
          public void accept(RespUpdatePatrolStatus respUpdatePatrolStatus) throws Exception {
            try {
              String jobStatus = respUpdatePatrolStatus.getStatus();
              if (jobStatus.equals(stat_ok)) {
                liveDataJobStat.setValue(ApiResponse.success(status, PatrolStatus.valueOf(respUpdatePatrolStatus.getPatrolStatus())));
              } else {
                liveDataJobStat.setValue(ApiResponse.success(null, PatrolStatus.valueOf(respUpdatePatrolStatus.getPatrolStatus())));
              }
            } catch (Exception e) {
              e.printStackTrace();
              liveDataJobStat.setValue(ApiResponse.success(null, PatrolStatus.valueOf(respUpdatePatrolStatus.getPatrolStatus())));
            }
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            liveDataJobStat.setValue(ApiResponse.error(throwable));
          }
        }));
  }

  public void apiCallFetchCompletedJobs() {
    disposables.add(repository.fetchCompletedJobs(vehicleId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override
          public void accept(Disposable disposable) throws Exception {
            liveDataCompletedJobs.setValue(ApiResponse.loading());
          }
        })
        .subscribe(new Consumer<RespFetchJobs>() {
          @Override
          public void accept(RespFetchJobs resp) throws Exception {
            List<BeanJob> jobList = new ArrayList<>();
            try {
              if (resp.getStatus().equals("ok")) {
                jobList.addAll(resp.getData());
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
            liveDataCompletedJobs.setValue(ApiResponse.success(jobList, PatrolStatus.valueOf(resp.getPatrolStatus())));
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            liveDataCompletedJobs.setValue(ApiResponse.error(throwable));
          }
        }));
  }

  public void apiCallGetRoute(final LatLng start, LatLng end) {
    if (start == origin && end == dest && routes != null) {
      liveDataRoutes.setValue(ApiResponse.success(routes, null));
    } else {
      origin = start;
      dest = end;
      disposables.add(repository.getRoute(start, end)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
              liveDataRoutes.setValue(ApiResponse.loading());
            }
          })
          .subscribe(new Consumer<RespGoogleDirections>() {
            @Override
            public void accept(RespGoogleDirections resp) throws Exception {
              List<LatLng> list = new ArrayList<>();
              if (resp.getStatus().equals("OK")) {
                if (!resp.getRoutes().isEmpty()) {
                  RespGoogleDirections.Route main_route = resp.getRoutes().get(0);
                  RespGoogleDirections.Leg leg = main_route.getLegs().get(0);
                  for (RespGoogleDirections.Step step : leg.getSteps()) {
                    list.addAll(decodePoly(step.getPolyline().getPoints()));
                  }
                }
                if (routes == null) {
                  routes = new ArrayList<>();
                }
                routes.clear();
                routes.addAll(list);
                //Notify change
                liveDataRoutes.setValue(ApiResponse.success(routes, null));
              } else {
                liveDataRoutes.setValue(ApiResponse.success(null, null));
              }
            }
          }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
              liveDataRoutes.setValue(ApiResponse.error(throwable));
            }
          }));
    }
  }

  private List<LatLng> decodePoly(String encoded) {
    List<LatLng> poly = new ArrayList<>();
    int index = 0, len = encoded.length();
    int lat = 0, lng = 0;
    while (index < len) {
      int b, shift = 0, result = 0;
      do {
        b = encoded.charAt(index++) - 63;
        result |= (b & 0x1f) << shift;
        shift += 5;
      } while (b >= 0x20);
      int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
      lat += dlat;
      shift = 0;
      result = 0;
      do {
        b = encoded.charAt(index++) - 63;
        result |= (b & 0x1f) << shift;
        shift += 5;
      } while (b >= 0x20);
      int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
      lng += dlng;
      LatLng p = new LatLng((((double) lat / 1E5)),
          (((double) lng / 1E5)));
      poly.add(p);
    }
    return poly;
  }

  public void apiCallGeoCode() {
    Location location = LocationService.BestLocation;
    if (location == null) {
      return;
    }

    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    disposables.add(repository.getGeoCode(latLng)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override
          public void accept(Disposable disposable) throws Exception {
          }
        })
        .subscribe(new Consumer<RespGeoReverseCode>() {
          @Override
          public void accept(RespGeoReverseCode resp) throws Exception {

            if (resp != null) {
              if (resp.getStatus().equals("OK")) {

                current_location_address = resp.getResults().get(0).getFormattedAddress();

              }
            }

          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
          }
        }));
  }

  public void timeCheck() {
    Observable<Long> observable = Observable.interval(2000L, TimeUnit.MILLISECONDS);
    disposables.add(observable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override
          public void accept(Disposable disposable) throws Exception {
            liveDataTime.setValue(ApiResponse.loading());
          }
        })
        .subscribe(new Consumer<Long>() {
          @Override
          public void accept(Long aLong) throws Exception {
            liveDataTime.setValue(ApiResponse.success(aLong, null));
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            liveDataTime.setValue(ApiResponse.error(throwable));
          }
        }));
  }

  @Override
  protected void onCleared() {
    disposables.clear();
    super.onCleared();
  }
}
