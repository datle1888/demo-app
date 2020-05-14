package com.jst.poc.dispatcher.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.entraze.rxwebsocket.entities.SocketClosedEvent;
import com.entraze.rxwebsocket.entities.SocketFailureEvent;
import com.entraze.rxwebsocket.entities.SocketMessageEvent;
import com.entraze.rxwebsocket.entities.SocketOpenEvent;
import com.jst.poc.dispatcher.R;
import com.jst.poc.dispatcher.adapters.ViewPagerAdapter;
import com.jst.poc.dispatcher.dagger.MyApplication;
import com.jst.poc.dispatcher.dagger.ViewModelFactory;
import com.jst.poc.dispatcher.databinding.ActivityMainContainerBinding;
import com.jst.poc.dispatcher.fragments_main.FragAlerts;
import com.jst.poc.dispatcher.fragments_main.FragDashBoard;
import com.jst.poc.dispatcher.fragments_main.FragDubaiPoliceWebsite;
import com.jst.poc.dispatcher.fragments_main.FragJobs;
import com.jst.poc.dispatcher.fragments_main.FragVehicles;
import com.jst.poc.dispatcher.interfaces.LocationServiceListener;
import com.jst.poc.dispatcher.interfaces.SwitchFragListener;
import com.jst.poc.dispatcher.models.BeanSelectedPage;
import com.jst.poc.dispatcher.models.BeanSocketEventObj;
import com.jst.poc.dispatcher.models.Top_selection;
import com.jst.poc.dispatcher.rest.ApiResponse;
import com.jst.poc.dispatcher.rest.PatrolStatus;
import com.jst.poc.dispatcher.rest.Status;
import com.jst.poc.dispatcher.room_db.DbRepository;
import com.jst.poc.dispatcher.utilities.Constants;
import com.jst.poc.dispatcher.utilities.LocationService;
import com.jst.poc.dispatcher.utilities.Logger;
import com.jst.poc.dispatcher.utilities.NonSwipeableViewPager;
import com.jst.poc.dispatcher.utilities.PreferanceClass;
import com.jst.poc.dispatcher.viewmodels.ApiViewModel;
import com.jst.poc.dispatcher.viewmodels.SocketViewModel;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements LocationServiceListener, SwitchFragListener {

  private static final String TAG = "~~MainActivity";

  private Intent locationServiceIntent;
  private LocationService locationService;

  int time_count = 0;

  ActivityMainContainerBinding binding;

  Context context;
  NonSwipeableViewPager viewPager;
  FragJobs fragJobs = new FragJobs();
  FragDashBoard fragDashBoard = new FragDashBoard();
  //    FragCameras fragCameras = new FragCameras();
  FragAlerts fragAlerts = new FragAlerts();
  FragVehicles fragVehicles = new FragVehicles();
  FragDubaiPoliceWebsite fragDubaiPoliceWebsite = new FragDubaiPoliceWebsite();
  ViewPagerAdapter pagerAdapter;

  BeanSelectedPage selectedPage = new BeanSelectedPage();

  @Inject
  ViewModelFactory viewModelFactory;

  ApiViewModel viewModel;

  SocketViewModel socketViewModel;

  @Inject
  DbRepository dbRepository;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main_container);
    context = this;
    viewPager = binding.viewPager;
    pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    MyApplication.getInstance().getAppComponent().doInjection(this);
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(ApiViewModel.class);
    socketViewModel = ViewModelProviders.of(this, viewModelFactory).get(SocketViewModel.class);
    initLiveData();
    initViewPager();
    socketViewModel.connectSocket();
    viewModel.timeCheck();
  }

  @Override
  public void onLocationUpdation(Location location) {
    // do some thing
  }

  private void initViewPager() {
    selectedPage.setActivity(this);
    selectedPage.setViewPager(viewPager);

    binding.topPanel.setDat(selectedPage);
    binding.bottomPanel.setDat(selectedPage);
    binding.bottomPanel.btnArrived.setVisibility(View.GONE);
    binding.bottomPanel.btnCompleted.setVisibility(View.GONE);

    selectedPage.setSelection(Top_selection.dashboard);

    binding.topPanel.notifyChange();
    binding.bottomPanel.notifyChange();
//        disableAllSelect();
//        setSelection(binding.topPanel.hgDash, binding.topPanel.tvDash, true);

    binding.notifyChange();

    fragAlerts.setViewModel(viewModel);
    fragAlerts.setDbRepository(dbRepository);

    fragDashBoard.setViewModel(viewModel);
    fragDashBoard.setSelectedPage(selectedPage);
    fragDashBoard.setListener(this);

    fragJobs.setViewModel(viewModel);
    fragJobs.setLocationServiceListener(this);
    fragJobs.setMainContainerBinding(binding);
    fragJobs.setSelectedPage(selectedPage);
    fragJobs.setSwitchFragListener(this);
//        fragCameras.setDbRepository(dbRepository);
    pagerAdapter.addFragment(fragDashBoard, "DashBoard");
    pagerAdapter.addFragment(fragJobs, "Jobs");
    pagerAdapter.addFragment(fragVehicles, "Vehicle");
    pagerAdapter.addFragment(fragAlerts, "Alerts");
    pagerAdapter.addFragment(fragDubaiPoliceWebsite, "DPWebsite");

    viewPager.setAdapter(pagerAdapter);
    viewPager.setOffscreenPageLimit(5);

    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // do some thing
      }

      @Override
      public void onPageSelected(int position) {
        Log.d("onPageSelected", String.valueOf(position));
        switch (position) {
          case 0:
            binding.bottomPanel.btnArrived.setVisibility(View.GONE);
            binding.bottomPanel.btnCompleted.setVisibility(View.GONE);
            selectedPage.setSelection(Top_selection.dashboard);
            binding.topPanel.notifyChange();
            binding.bottomPanel.notifyChange();
//                        disableAllSelect();
//                        setSelection(binding.topPanel.hgDash, binding.topPanel.tvDash, true);
            binding.notifyChange();
            break;
          case 1:
            selectedPage.setSelection(Top_selection.jobs);
            binding.topPanel.notifyChange();
            binding.bottomPanel.notifyChange();
//                        disableAllSelect();
//                        setSelection(binding.topPanel.hgJobs, binding.topPanel.tvJobs, true);
            binding.notifyChange();
            break;
          case 2:
            binding.bottomPanel.btnArrived.setVisibility(View.GONE);
            binding.bottomPanel.btnCompleted.setVisibility(View.GONE);
            selectedPage.setSelection(Top_selection.cameras);
            binding.topPanel.notifyChange();
            binding.bottomPanel.notifyChange();
//                        disableAllSelect();
//                        setSelection(binding.topPanel.hgCameras, binding.topPanel.tvCameras, true);
            binding.notifyChange();
            break;
          case 3:
            binding.bottomPanel.btnArrived.setVisibility(View.GONE);
            binding.bottomPanel.btnCompleted.setVisibility(View.GONE);
            selectedPage.setSelection(Top_selection.vehicle);
            binding.topPanel.notifyChange();
            binding.bottomPanel.notifyChange();
//                        disableAllSelect();
//                        setSelection(binding.topPanel.hgVehicle, binding.topPanel.tvVehicles, true);
            binding.notifyChange();
            break;
          case 4:
            binding.bottomPanel.btnArrived.setVisibility(View.GONE);
            binding.bottomPanel.btnCompleted.setVisibility(View.GONE);
            selectedPage.setSelection(Top_selection.application);
            binding.topPanel.notifyChange();
            binding.bottomPanel.notifyChange();
//                        disableAllSelect();
            binding.notifyChange();
            break;
        }
      }

      @Override
      public void onPageScrollStateChanged(int state) {
        // do some thing
      }
    });

    //Initial selection
    viewPager.setCurrentItem(0);
    binding.topPanel.linDash.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        viewPager.setCurrentItem(0);
      }
    });

    binding.topPanel.linLogout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        try {
          PreferanceClass.RemoveAll(context);

          viewModel.apiCallUpdatePatrolStatus(PatrolStatus.not_available);
          Intent intent = new Intent(context, LoginActivity.class);
          finish();
          startActivity(intent);
          overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        } catch (Exception ex) {
          Logger.getInstance().error_log(TAG, ex.getMessage(), ex.fillInStackTrace());
        }
      }
    });

    binding.topPanel.linJobs.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        viewPager.setCurrentItem(1);
      }
    });

    binding.topPanel.linVehicle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        viewPager.setCurrentItem(2);
      }
    });

    binding.topPanel.linCameras.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        viewPager.setCurrentItem(3);
      }
    });

    binding.bottomPanel.linbtnapp1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//                fragDubaiPoliceWebsite.setWebUrl("");
        viewPager.setCurrentItem(0);
      }
    });

    binding.bottomPanel.linbtnapp2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//                fragDubaiPoliceWebsite.setWebUrl("");
        viewPager.setCurrentItem(4);
      }
    });

    binding.bottomPanel.linbtnapp3.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//                fragDubaiPoliceWebsite.setWebUrl("");
        viewPager.setCurrentItem(4);
      }
    });

    binding.bottomPanel.linbtnapp4.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//                fragDubaiPoliceWebsite.setWebUrl("");
        viewPager.setCurrentItem(4);
      }
    });

    binding.bottomPanel.linbtnapp5.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//                fragDubaiPoliceWebsite.setWebUrl("");
        viewPager.setCurrentItem(4);
      }
    });

    binding.bottomPanel.linbtnapp6.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//                fragDubaiPoliceWebsite.setWebUrl("");
        viewPager.setCurrentItem(4);
      }
    });
  }

  private void initLiveData() {
    socketViewModel.getLiveData().observe(this, new Observer<BeanSocketEventObj>() {
      @Override
      public void onChanged(BeanSocketEventObj beanSocketEventObj) {
        switch (beanSocketEventObj.getTypeEnum()) {
          case OPEN:
            SocketOpenEvent socketOpenEvent = (SocketOpenEvent) beanSocketEventObj.getObject();
            Logger.getInstance().verbose_log(TAG, "Opened");
            break;
          case MESSAGE:
            SocketMessageEvent socketMessageEvent = (SocketMessageEvent) beanSocketEventObj.getObject();
            //Logger.getInstance().verbose_log(TAG, "message " + socketMessageEvent.getText());
            break;
          case CLOSED:
            SocketClosedEvent socketClosedEvent = (SocketClosedEvent) beanSocketEventObj.getObject();
            Logger.getInstance().verbose_log(TAG, "closed ");
            break;
          case FAILURE:
            SocketFailureEvent socketFailureEvent = (SocketFailureEvent) beanSocketEventObj.getObject();
            Logger.getInstance().verbose_log(TAG, "failed " + socketFailureEvent.getException().getMessage());
            break;
        }
      }
    });

    socketViewModel.getLiveDataError().observe(this, new Observer<Throwable>() {
      @Override
      public void onChanged(Throwable throwable) {
        Logger.getInstance().error_log(TAG, " error ", throwable);
      }
    });

    viewModel.getLiveDataTime().observe(this, new Observer<ApiResponse>() {
      @Override
      public void onChanged(ApiResponse apiResponse) {
        if (time_count == 60) {
          time_count = 0;
        } else {
          time_count = time_count + 2;
        }
        if (apiResponse.status == Status.SUCCESS) {
          long difference = System.currentTimeMillis() - socketViewModel.getLastPingTime();
          if (difference > Constants.socket_connection_time_out) {
            //Reconnect
            socketViewModel.connectSocket();
          }
          fragDashBoard.updateFaceAndPlateCount(dbRepository.getFaceDetectedCount(),
              dbRepository.getDetectedPlateCount());
          if (TextUtils.isEmpty(ApiViewModel.current_location_address)) {
            viewModel.apiCallGeoCode();
          } else {
            //Call in every 60 second
            if (time_count == 0) {
              viewModel.apiCallGeoCode();
            }
          }
        }
      }
    });
  }

  @Override
  public void onSwitchFragment(int item, int sub_item) {
    viewPager.setCurrentItem(item);
//        if (sub_item != -1){
//            //Check item == cameras
//            if (item == 2){
//                fragCameras.setFragSelected(sub_item);
//            }
//        }
  }
}
