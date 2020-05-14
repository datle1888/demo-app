package com.jst.poc.dispatcher.fragments_sub;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.jst.poc.dispatcher.R;
import com.jst.poc.dispatcher.adapters.AdapterRecyclerPlateNums;
import com.jst.poc.dispatcher.databinding.PlateWebViewFragLayoutBinding;
import com.jst.poc.dispatcher.interfaces.RecyclerClickListener;
import com.jst.poc.dispatcher.models.BeanSocketObjNum;
import com.jst.poc.dispatcher.rest.ApiResponse;
import com.jst.poc.dispatcher.room_db.DbRepository;
import com.jst.poc.dispatcher.viewmodels.ApiViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class NumPlateListFrag extends Fragment {
  PlateWebViewFragLayoutBinding binding;
  ApiViewModel viewModel;
  boolean loading1 = false;
  private RecyclerView recyclerView;
  private List<BeanSocketObjNum> list = new ArrayList<>();
  private AdapterRecyclerPlateNums adapter;
  private DbRepository dbRepository;
  private int offset = list.size() + 1;

  public void setDbRepository(DbRepository dbRepository) {
    this.dbRepository = dbRepository;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    adapter = new AdapterRecyclerPlateNums(list, getContext(), new RecyclerClickListener() {
      @Override
      public void onItemClick(Object obj, int pos) {
        try {
          BeanSocketObjNum num = (BeanSocketObjNum) obj;
          dbRepository.dismissPlate(num.getId());
          list.remove(pos);
          adapter.notifyItemRemoved(pos);
          setRecyclerVisibilty(false);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.plate_web_view_frag_layout, container, false);
    WebView webView = binding.webView;
    webView.setVisibility(View.GONE);
    recyclerView = binding.recyclerView;
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(adapter);
    RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
    if (animator instanceof SimpleItemAnimator) {
      ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
    }
    if (list.isEmpty())
      init_data();
    iniT_live_data();
    return binding.getRoot();
  }

  private void init_data() {
    setRecyclerVisibilty(true);
    list.clear();
    list.addAll(dbRepository.getAllNumberPlate(offset));
    if (recyclerView != null) {
      adapter.notifyDataSetChanged();
    }
    setRecyclerVisibilty(false);
  }

  private void iniT_live_data() {
    viewModel.getLiveDataPlates().observe(this, new Observer<ApiResponse>() {
      @Override
      public void onChanged(ApiResponse apiResponse) {
        switch (apiResponse.status) {
          case LOADING:
            loading1 = true;
            Log.v("TESTPLATE", "Checking New plates...");
            break;
          case SUCCESS:
            loading1 = false;
            Log.v("TESTPLATE", "New plates found");
            List<BeanSocketObjNum> newPlates = null;
            newPlates = (List<BeanSocketObjNum>) apiResponse.data;
            for (BeanSocketObjNum plate : newPlates) {
              Log.v("TESTPLATE", "New plate found: " + plate.getPlateNumber());
              dbRepository.insertNumberPlate(plate);
            }
            break;
          case ERROR:
            loading1 = false;
            Log.e("TESTPLATE", apiResponse.error.getLocalizedMessage());
            break;
        }
      }
    });

    viewModel.getLiveDataTime().observe(this, new Observer<ApiResponse>() {
      @Override
      public void onChanged(ApiResponse apiResponse) {
        switch (apiResponse.status) {
          case SUCCESS:
            if (!loading1) {
              Log.v("TESTPLATE", "Plates check requested");
              viewModel.apiCallCheckDetectedPlates();
            }
            break;
        }
      }
    });

    dbRepository.getLiveDataInsertedNumData().observe(this, new Observer<BeanSocketObjNum>() {
      @Override
      public void onChanged(BeanSocketObjNum num) {
        if (num != null) {
          int i = 0;
          for (BeanSocketObjNum nu : list) {
            if (nu.getId().equals(num.getId())) {
              list.remove(i);
              adapter.notifyItemRemoved(i);
              break;
            }
            i++;
          }
          list.add(0, num);
          adapter.notifyItemChanged(0);
        }
        setRecyclerVisibilty(false);
      }
    });
  }

  private Predicate<BeanSocketObjNum> isIdSame(BeanSocketObjNum obj) {
    return p -> p.getId().equals(obj.getId());
  }

  private void setRecyclerVisibilty(boolean loading) {
    if (binding == null) {
      return;
    }
    if (list.isEmpty()) {
      binding.recyclerView.setVisibility(View.GONE);
      binding.tvLoadingNumPlate.setText(getString(R.string.lbl_empty_num_plate));
      binding.tvLoadingNumPlate.setVisibility(View.VISIBLE);
    } else {
      if (loading) {
        binding.recyclerView.setVisibility(View.GONE);
        binding.tvLoadingNumPlate.setText(getString(R.string.lbl_loading_num_plate));
        binding.tvLoadingNumPlate.setVisibility(View.VISIBLE);
      } else {
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.tvLoadingNumPlate.setVisibility(View.GONE);
      }
    }
  }

  public void setViewModel(ApiViewModel viewModel) {
    this.viewModel = viewModel;
  }


}
