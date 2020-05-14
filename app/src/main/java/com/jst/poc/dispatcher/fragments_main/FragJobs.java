package com.jst.poc.dispatcher.fragments_main;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jst.poc.dispatcher.R;
import com.jst.poc.dispatcher.databinding.ActivityJobsBinding;
import com.jst.poc.dispatcher.databinding.ActivityMainContainerBinding;
import com.jst.poc.dispatcher.fragments_sub.BottomSheetJobNotes;
import com.jst.poc.dispatcher.interfaces.DialogDismissListener;
import com.jst.poc.dispatcher.interfaces.LocationServiceListener;
import com.jst.poc.dispatcher.interfaces.SwitchFragListener;
import com.jst.poc.dispatcher.models.BeanJob;
import com.jst.poc.dispatcher.models.BeanJobViewSet;
import com.jst.poc.dispatcher.models.BeanSelectedPage;
import com.jst.poc.dispatcher.models.Top_selection;
import com.jst.poc.dispatcher.rest.ApiResponse;
import com.jst.poc.dispatcher.rest.JobStatus;
import com.jst.poc.dispatcher.utilities.CustomLoadingDialog;
import com.jst.poc.dispatcher.utilities.LocationService;
import com.jst.poc.dispatcher.utilities.Logger;
import com.jst.poc.dispatcher.utilities.ValidatorAndFormatter;
import com.jst.poc.dispatcher.viewmodels.ApiViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//import com.jst.poc.dispatcher.adapters.JobCompletedAdapter;
//import com.jst.poc.dispatcher.fragments_sub.BottomSheetJobDelegate;
//import com.jst.poc.dispatcher.fragments_sub.BottomSheetJobNotes;

public class FragJobs extends Fragment implements OnMapReadyCallback, LocationServiceListener {
  private static final String TAG = "~~Jobs";
  ActivityJobsBinding binding;
  ActivityMainContainerBinding mainContainerBinding;
  BeanJob job = null;
  ApiViewModel viewModel;
  BeanSelectedPage selectedPage;
  List<BeanJob> completedJobsList = new ArrayList<>();
  //    JobCompletedAdapter adapter;
  Context context;
  PolylineOptions options = null;
  LocationServiceListener locationServiceListener;
  SwitchFragListener switchFragListener;
  private GoogleMap mMap;
  private LatLng latLngDest = null, vehicleLoc = null;
  private Location vehicleLocFromLocService = null;
  private List<LatLng> routes = new ArrayList<>();
  private int first_time = 0;

  public void setSwitchFragListener(SwitchFragListener switchFragListener) {
    this.switchFragListener = switchFragListener;
  }

  public void setMainContainerBinding(ActivityMainContainerBinding mainContainerBinding) {
    this.mainContainerBinding = mainContainerBinding;
  }

  public void setSelectedPage(BeanSelectedPage selectedPage) {
    this.selectedPage = selectedPage;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = getContext();
    Toast.makeText(context, "jobs frag created", Toast.LENGTH_SHORT).show();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(inflater, R.layout.activity_jobs, container, false);
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
    initBinding();
//        initRecyclerCompleted();
    modeSetting();
    init_livedata();
    init_clicks();
    Toast.makeText(context, "jobs view created", Toast.LENGTH_SHORT).show();
    return binding.getRoot();
  }

  private void init_livedata() {
    final CustomLoadingDialog loadingDialog = new CustomLoadingDialog(getActivity());
//        binding.backendLogView.setText(binding.backendLogView.getText()+"\n"+"Calling the API...");
    viewModel.getLiveDataJobStat().observe(this, new Observer<ApiResponse>() {
      @Override
      public void onChanged(ApiResponse apiResponse) {
        switch (apiResponse.status) {
          case LOADING:
            loadingDialog.hideDialog();
            if (first_time == 0) {
              loadingDialog.showDialog();
            }
            first_time++;
            break;
          case SUCCESS:
            loadingDialog.hideDialog();
//                        binding.backendLogView.setText(binding.backendLogView.getText()+"\n"+apiResponse.responseString());
            if (apiResponse.data != null) {
              JobStatus status = (JobStatus) apiResponse.data;
              if (job != null) {
                job.setStatus(String.valueOf(status));
                modeSetting();
              }
              if (status == JobStatus.completed) {
                if (switchFragListener != null) {
                  switchFragListener.onSwitchFragment(0, -1);
                }
                selectedPage.onClickItem(mainContainerBinding.bottomPanel.btnCompleted, Top_selection.dashboard);
              }
            }
            break;
          case ERROR:
            loadingDialog.hideDialog();
            break;
        }
      }
    });

    viewModel.getLiveDataCompletedJobs().observe(this, new Observer<ApiResponse>() {
      @Override
      public void onChanged(ApiResponse apiResponse) {
        switch (apiResponse.status) {
          case LOADING:
            break;
          case SUCCESS:
            completedJobsList.clear();
            completedJobsList.addAll((Collection<? extends BeanJob>) apiResponse.data);
            modeSetting();
            break;
          case ERROR:
            break;
        }
      }
    });

    viewModel.getLiveDataRoutes().observe(this, new Observer<ApiResponse>() {
      @Override
      public void onChanged(ApiResponse apiResponse) {
        switch (apiResponse.status) {
          case LOADING:
            loadingDialog.hideDialog();
            loadingDialog.showDialog();
            break;
          case SUCCESS:
            List<LatLng> list = (List<LatLng>) apiResponse.data;
            if (list != null) {
              routes.clear();
              routes.addAll(list);
              setMarker();
              options.addAll(routes);
              mMap.addPolyline(options);
              mMap.addMarker(new MarkerOptions().position(routes.get(0)).title("Vehicle location"));
            }
            loadingDialog.hideDialog();
            break;
          case ERROR:
            loadingDialog.hideDialog();
            break;
        }
      }
    });
  }

  private void init_clicks() {
    binding.snippetPending.btnAcceptJob.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        viewModel.apiCallUpdateJobStatus(job.getCode(), null, JobStatus.assignAccepted);
      }
    });
/*
        binding.snippetPending.btnDelegateJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetJobDelegate delegate = new BottomSheetJobDelegate();
                delegate.setListener(new BottomSheetJobDelegate.JobDelegateListener() {
                    @Override
                    public void onDone(String notes) {
                        viewModel.apiCallUpdateJobStatus(job.getCode(),notes, JobStatus.assignRejected);
                    }
                });
                delegate.show(getChildFragmentManager(),getString(R.string.lbl_delegate_job));
            }
        });
*/
    mainContainerBinding.bottomPanel.btnArrived.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        viewModel.apiCallUpdateJobStatus(job.getCode(), null, JobStatus.arrived);
      }
    });

    mainContainerBinding.bottomPanel.btnCompleted.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        BottomSheetJobNotes notes = new BottomSheetJobNotes();
        notes.setListener(new DialogDismissListener() {
          @Override
          public void onDismiss(Object obj) {
            String notes = (String) obj;
            viewModel.apiCallUpdateJobStatus(job.getCode(), notes, JobStatus.completed);
          }
        });
        notes.show(getChildFragmentManager(), "notes_frag");
      }
    });
  }

  private void initBinding() {
    BeanJobViewSet pending = new BeanJobViewSet();
    pending.setCompleted(false);
    binding.snippetPending.setDat(pending);
  }

  private void modeSetting() {
    disableViews();
    if (job != null) {
      binding.setDat(job);
      if (job.getStatus().equals(String.valueOf(JobStatus.assigned)) ||
          job.getStatus().equals(String.valueOf(JobStatus.assignRejected))) {
        disableViews();
        binding.snippetPending.containerAll.setVisibility(View.VISIBLE);
        binding.tvCaptionPending.setVisibility(View.VISIBLE);
        Toast.makeText(context, "New job received", Toast.LENGTH_LONG).show();
      } else {
        disableViews();
        //Map view visibilty setting
        binding.mapContainer.setVisibility(View.VISIBLE);
        binding.mapImageView.setVisibility(getView().VISIBLE);
        binding.linContainerTop.setVisibility(View.VISIBLE);
        binding.linContainerBottom.setVisibility(View.VISIBLE);
        mainContainerBinding.bottomPanel.btnCompleted.setVisibility(View.VISIBLE);
        mainContainerBinding.bottomPanel.btnArrived.setVisibility(View.VISIBLE);
        if (job.getStatus().equals(String.valueOf(JobStatus.arrived))) {
          bottomButtonModeSwitch(mainContainerBinding.bottomPanel.btnArrived, false);
          bottomButtonModeSwitch(mainContainerBinding.bottomPanel.btnCompleted, true);
        } else {
          bottomButtonModeSwitch(mainContainerBinding.bottomPanel.btnArrived, true);
          bottomButtonModeSwitch(mainContainerBinding.bottomPanel.btnCompleted, false);
        }
      }
    } else {
      disableViews();
/*
            if (!completedJobsList.isEmpty()){
                binding.recyclerCompleted.setVisibility(View.VISIBLE);
                binding.tvCaptionCompleted.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }
*/
    }
  }

  private void bottomButtonModeSwitch(AppCompatButton button, boolean isEnabled) {
    button.setBackgroundResource(isEnabled ? R.drawable.clickable_button_bg : R.drawable.btn_job_completed_bg);
    button.setTextColor(getResources().getColor(isEnabled ? R.color.white : R.color.dim_white));
    button.setClickable(isEnabled);
  }

  /*
      private void initRecyclerCompleted(){
          adapter = new JobCompletedAdapter(completedJobsList, context);
          binding.recyclerCompleted.setLayoutManager(new LinearLayoutManager(context));
          binding.recyclerCompleted.setAdapter(adapter);
      }
  */
  private void apiCall() {
    if (mMap == null) {
      return;
    }
    if (vehicleLocFromLocService != null) {
      vehicleLoc = new LatLng(vehicleLocFromLocService.getLatitude(),
          vehicleLocFromLocService.getLongitude());
    }
    if (vehicleLoc == null) {
      return;
    }
    viewModel.apiCallGetRoute(vehicleLoc, latLngDest);
  }

  private void setMarker() {
    if (mMap == null) {
      return;
    }
    options = null;
    options = new PolylineOptions().width(5).color(Color.GREEN).geodesic(true);
    mMap.clear();
    mMap.addMarker(new MarkerOptions().position(latLngDest).title("Destination"));
    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngDest, 11f));
  }

  private void disableViews() {
    binding.tvCaptionPending.setVisibility(View.GONE);
    binding.snippetPending.containerAll.setVisibility(View.GONE);
    binding.tvCaptionCompleted.setVisibility(View.GONE);
    binding.recyclerCompleted.setVisibility(View.GONE);
    binding.linContainerTop.setVisibility(View.GONE);
    binding.linContainerBottom.setVisibility(View.GONE);
    binding.mapContainer.setVisibility(View.GONE);
    binding.mapImageView.setVisibility(getView().GONE);
  }


  @Override
  public void onMapReady(GoogleMap googleMap) {
    googleMap.setMapStyle(new MapStyleOptions(getResources()
        .getString(R.string.style_json)));
    mMap = googleMap;
    vehicleLocFromLocService = LocationService.BestLocation;
    if (job != null) {
      setMarker();
      apiCall();
    }
  }


  @Override
  public void onLocationUpdation(Location location) {
    Logger.getInstance().verbose_log(TAG, "Updated location " + location.getAccuracy() + " " + location.getProvider());
    if (location != null) {
      vehicleLocFromLocService = location;
      apiCall();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    job = FragDashBoard.pending_job;
    vehicleLocFromLocService = LocationService.BestLocation;
    if (job != null) {
      latLngDest = ValidatorAndFormatter.getInstance().getLocFromString(job.getLocation());
      setMarker();
      apiCall();
    }
    viewModel.apiCallFetchCompletedJobs();
  }

  public void setLocationServiceListener(LocationServiceListener locationServiceListener) {
    this.locationServiceListener = locationServiceListener;
  }

  public void setViewModel(ApiViewModel viewModel) {
    this.viewModel = viewModel;
  }
}
