package com.jst.poc.dispatcher.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jst.poc.dispatcher.R;
import com.jst.poc.dispatcher.dagger.GlideApp;
import com.jst.poc.dispatcher.databinding.RecycleItemPlateBinding;
import com.jst.poc.dispatcher.interfaces.RecyclerClickListener;
import com.jst.poc.dispatcher.models.BeanSocketObjNum;
import com.jst.poc.dispatcher.utilities.Logger;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class AdapterRecyclerPlateNums extends RecyclerView.Adapter<AdapterRecyclerPlateNums.ViewHolder> {
  List<BeanSocketObjNum> list;
  Context context;
  LayoutInflater inflater;
  RecyclerClickListener listener;

  public AdapterRecyclerPlateNums(List<BeanSocketObjNum> list, Context context, RecyclerClickListener listener) {
    this.list = list;
    this.context = context;
    this.listener = listener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (inflater == null) {
      inflater = LayoutInflater.from(parent.getContext());
    }
    RecycleItemPlateBinding binding = DataBindingUtil.inflate(inflater, R.layout.recycle_item_plate,
        parent, false);
    return new ViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    try {
      BeanSocketObjNum num = list.get(position);
      Logger.getInstance().verbose_log(null, "Time ago " + num.getTimeAgo());
      holder.binding.setDat(num);
      loadImageBase64(num.getImageBase64(), holder.binding.cameraImg);
      holder.binding.btnDismiss.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          listener.onItemClick(num, position);
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setHasStableIds(boolean hasStableIds) {
    super.setHasStableIds(true);
  }

  @Override
  public long getItemId(int position) {
    BeanSocketObjNum objNum = list.get(position);
    return objNum.getTime_of_insertion();
  }

  @Override
  public int getItemCount() {
    return list == null ? 0 : list.size();
  }

  private void loadImageBase64(String strBase64, AppCompatImageView imageView) {
    byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    imageView.setImageBitmap(decodedByte);
  }

  private void loadImage(String url, AppCompatImageView imageView) {
    if (TextUtils.isEmpty(url)) {
      return;
    }
    // imageView.invalidate();
    GlideApp.with(context)
        .load(url)
        .transition(withCrossFade())
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .into(imageView);
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    RecycleItemPlateBinding binding;
    public ViewHolder(@NonNull RecycleItemPlateBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}
