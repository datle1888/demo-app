package com.jst.poc.dispatcher.fragments_main;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.jst.poc.dispatcher.R;
import com.jst.poc.dispatcher.databinding.ActivityDashboardBinding;
import com.jst.poc.dispatcher.interfaces.SwitchFragListener;
import com.jst.poc.dispatcher.models.BeanJob;
import com.jst.poc.dispatcher.models.BeanSelectedPage;
import com.jst.poc.dispatcher.models.Top_selection;
import com.jst.poc.dispatcher.rest.ApiResponse;
import com.jst.poc.dispatcher.rest.PatrolStatus;
import com.jst.poc.dispatcher.viewmodels.ApiViewModel;

import java.util.Timer;

//import com.jst.poc.dispatcher.fragments_sub.BottomSheetReportIncident;

public class FragDashBoard extends Fragment {

  public static BeanJob pending_job = null;
  static boolean jobModeOngoing = false;
  final String key_job_mode = "MODE_JOB_ONGOING";
  Timer jobFlasherTimer;
  boolean isGreen = true;
  boolean timerStarted = false;
  ActivityDashboardBinding binding;
  int color_green, color_white;
  boolean loading = false;
  int count = 0;
  ApiViewModel viewModel;

  BeanSelectedPage selectedPage = null;

  SwitchFragListener listener = null;

  MediaPlayer mp;
  android.os.Handler customHandler;
  private Runnable updateTimerThread = new Runnable() {
    public void run() {
      if (isGreen) {
        binding.includePendingJob.centerCircle.setBackgroundResource(R.drawable.dp_circle4);
        isGreen = false;
      } else {
        binding.includePendingJob.centerCircle.setBackgroundResource(R.drawable.dp_circle3);
        isGreen = true;
      }
      if (timerStarted)
        customHandler.postDelayed(this, 1000);
    }
  };

  public void setListener(SwitchFragListener listener) {
    this.listener = listener;
  }

  public void setSelectedPage(BeanSelectedPage selectedPage) {
    this.selectedPage = selectedPage;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//        color_green = getContext().getColor(R.color.text_unselected_green);
//        color_white = getContext().getColor(R.color.white);

    if (savedInstanceState != null) {
      jobModeOngoing = savedInstanceState.getBoolean(key_job_mode);
    }
    Uri alramPAth = Uri.parse(Environment.getExternalStorageDirectory().toString() + "/dispatcherAudios/jobAlert1.mp3");
//        mp = MediaPlayer.create(this.getContext(), alramPAth);
    mp = MediaPlayer.create(this.getContext(), R.raw.new_job_male_2);
    mp.setLooping(false);
    jobModeOngoing = false;
  }

  private void startFlasher() {
    timerStarted = true;
    customHandler = new android.os.Handler();
    customHandler.postDelayed(updateTimerThread, 0);
  }

  private void stopFlasher() {
    timerStarted = false;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(inflater, R.layout.activity_dashboard, container, false);
    initBinding();
    setJobModeView();
    init_clicks();
    initLiveData();
    return binding.getRoot();
  }

  private void init_clicks() {
    binding.linDashCentreBox.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (jobModeOngoing && listener != null) {
          listener.onSwitchFragment(1, -1);
          mp.pause();
          selectedPage.onClickItem(view, Top_selection.jobs);
        }
      }
    });

    binding.includePlateDetect.constr.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (listener != null) {
          listener.onSwitchFragment(2, 1);
        }
      }
    });

    binding.includeFacialDetect.constr.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (listener != null) {
          listener.onSwitchFragment(2, 2);
        }
      }
    });

//        binding.btnReportIncident.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                BottomSheetReportIncident reportIncident = new BottomSheetReportIncident();
//                reportIncident.show(getChildFragmentManager(),"report_incident");
//            }
//        });

    binding.btnPatrolStatus.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        binding.btnPatrolStatus.setEnabled(false);
        if (ApiResponse.patrolStatus == null || ApiResponse.patrolStatus.equals(PatrolStatus.not_available)) {
          viewModel.apiCallUpdatePatrolStatus(PatrolStatus.available_via_radio);
        } else if (ApiResponse.patrolStatus.equals(PatrolStatus.available_via_radio))
          viewModel.apiCallUpdatePatrolStatus(PatrolStatus.not_available);

        viewModel.getLiveDataJobsCheck().observe(FragDashBoard.this, new Observer<ApiResponse>() {
          @Override
          public void onChanged(ApiResponse apiResponse) {
            switch (apiResponse.status) {
              case LOADING:
                loading = true;
                break;
              case SUCCESS:
                loading = false;
                pending_job = null;
                pending_job = (BeanJob) apiResponse.data;
                jobModeOngoing = pending_job != null;
                if (ApiResponse.patrolStatus != null) {
                  if (ApiResponse.patrolStatus.toString().equals("available_via_radio"))
                    binding.btnPatrolStatus.setText(R.string.available_via_radio);
                  else if (ApiResponse.patrolStatus.toString().equals("not_available"))
                    binding.btnPatrolStatus.setText(R.string.not_available);
                  else if (ApiResponse.patrolStatus.toString().equals("on_the_way"))
                    binding.btnPatrolStatus.setText(R.string.on_the_way);
                  else
                    binding.btnPatrolStatus.setText(ApiResponse.patrolStatus.toString());
                }
                binding.btnPatrolStatus.setEnabled(true);
                setJobModeView();
                break;
              case ERROR:
                loading = false;
                if (count == 0) {
                  Toast.makeText(getContext(), apiResponse.error.getLocalizedMessage(),
                      Toast.LENGTH_SHORT).show();
                }
                count++;
                break;
            }
          }
        });
      }
    });
  }

  private void initBinding() {
//        binding.includePlateDetect.constr.setBackgroundResource(R.drawable.ic_dash_circle_right);
//        binding.includeFacialDetect.constr.setBackgroundResource(R.drawable.ic_dash_circle_left);

    binding.includeFacialDetect.tvLabel.setText(getString(R.string.lbl_facial_detection));
    binding.includePlateDetect.tvLabel.setText(getString(R.string.lbl_plate_detection));

    binding.includeFacialDetect.tvVal.setText("0");
    binding.includePlateDetect.tvVal.setText("0");
  }

  private void setJobModeView() {
    if (binding == null) {
      return;
    }
    if (jobModeOngoing) {
//            binding.includePendingJob.container.setBackgroundResource(R.drawable.ic_dash_centre_circle);
      binding.includePendingJob.tvLabel.setTextColor(getResources().getColor(R.color.white));
      binding.includePendingJob.tvLabel.setText(R.string.lbl_new_job);
//            binding.includePendingJob.centerCircle.setBackgroundResource(R.drawable.dp_circle4);
      if (!timerStarted)
        startFlasher();
    } else {
//            binding.includePendingJob.container.setBackgroundResource(R.drawable.ic_dash_centre_circle_green);
      binding.includePendingJob.tvLabel.setTextColor(getResources().getColor(R.color.text_unselected_green));
      binding.includePendingJob.tvLabel.setText(R.string.lbl_pending_job);
      if (timerStarted)
        stopFlasher();
      binding.includePendingJob.centerCircle.setBackgroundResource(R.drawable.dp_circle3);
    }
  }

  private void initLiveData() {
    viewModel.getLiveDataJobsCheck().observe(this, new Observer<ApiResponse>() {
      @Override
      public void onChanged(ApiResponse apiResponse) {
        switch (apiResponse.status) {
          case LOADING:
            loading = true;
            break;
          case SUCCESS:
            loading = false;
            pending_job = null;
            pending_job = (BeanJob) apiResponse.data;
            jobModeOngoing = pending_job != null;
            if (pending_job != null && !mp.isPlaying() && !selectedPage.getSelection().equals(Top_selection.jobs)) {
              mp.seekTo(0);
              mp.start();
            }
            //binding.btnPatrolStatus.setClickable(true);
            if (ApiResponse.patrolStatus != null) {
              if (ApiResponse.patrolStatus.toString().equals("available_via_radio"))
                binding.btnPatrolStatus.setText(R.string.available_via_radio);
              else if (ApiResponse.patrolStatus.toString().equals("not_available"))
                binding.btnPatrolStatus.setText(R.string.not_available);
              else if (ApiResponse.patrolStatus.toString().equals("on_the_way"))
                binding.btnPatrolStatus.setText(R.string.on_the_way);
              else
                binding.btnPatrolStatus.setText(ApiResponse.patrolStatus.toString());
            }
            setJobModeView();
            break;
          case ERROR:
            loading = false;
            if (count == 0) {
              Toast.makeText(getContext(), apiResponse.error.getLocalizedMessage(),
                  Toast.LENGTH_SHORT).show();
            }
            count++;
            break;
        }
      }
    });

    viewModel.getLiveDataTime().observe(this, new Observer<ApiResponse>() {
      @Override
      public void onChanged(ApiResponse apiResponse) {
        Log.v("TEST", "Time ticked");
        switch (apiResponse.status) {
          case SUCCESS:
            if (!loading) {
              viewModel.apiCallCheckPendingJob();
            }
            break;
        }
      }
    });
  }

  public void setViewModel(ApiViewModel viewModel) {
    this.viewModel = viewModel;
  }
  @Override
  public void onResume() {
    super.onResume();
    if (!loading) {
      viewModel.apiCallCheckPendingJob();
    }
  }

  public void updateFaceAndPlateCount(int face_count, int plate_count) {
    if (binding != null) {
      binding.includeFacialDetect.tvVal.setText("" + face_count);
      binding.includePlateDetect.tvVal.setText("" + plate_count);
    }
  }
}
