package com.jst.poc.dispatcher.fragments_sub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jst.poc.dispatcher.R;
import com.jst.poc.dispatcher.databinding.BottomShtJobCompletedBinding;
import com.jst.poc.dispatcher.interfaces.DialogDismissListener;

public class BottomSheetJobNotes extends BottomSheetDialogFragment {
  BottomShtJobCompletedBinding binding;
  DialogDismissListener listener;

  public void setListener(DialogDismissListener listener) {
    this.listener = listener;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sht_job_completed, container, false);
    binding.tvSelectType.setVisibility(View.GONE);
    binding.frmSelectType.setVisibility(View.GONE);
    binding.tvCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dismiss();
      }
    });
    binding.btnDone.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dismiss();
        if (listener != null) {
          listener.onDismiss(null);
        }
      }
    });
    return binding.getRoot();
  }
}
