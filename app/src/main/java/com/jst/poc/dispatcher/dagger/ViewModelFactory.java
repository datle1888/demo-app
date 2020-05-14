package com.jst.poc.dispatcher.dagger;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.jst.poc.dispatcher.rest.Repository;
import com.jst.poc.dispatcher.room_db.DbRepository;
import com.jst.poc.dispatcher.viewmodels.ApiViewModel;
import com.jst.poc.dispatcher.viewmodels.SocketViewModel;

import javax.inject.Inject;

public class ViewModelFactory implements ViewModelProvider.Factory {
  private Repository repository;
  private DbRepository dbRepository;

  @Inject
  public ViewModelFactory(Repository repository, DbRepository dbRepository) {
    this.repository = repository;
    this.dbRepository = dbRepository;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    if (modelClass.isAssignableFrom(ApiViewModel.class)) {
      return (T) new ApiViewModel(repository);
    }
    if (modelClass.isAssignableFrom(SocketViewModel.class)) {
      return (T) new SocketViewModel(dbRepository);
    }
    throw new IllegalArgumentException("Unknown class name");
  }
}
