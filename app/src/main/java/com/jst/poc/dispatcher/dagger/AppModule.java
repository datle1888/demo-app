package com.jst.poc.dispatcher.dagger;

import android.content.Context;

import androidx.lifecycle.ViewModelProvider;

import com.jst.poc.dispatcher.rest.ApiClientModule;
import com.jst.poc.dispatcher.rest.Repository;
import com.jst.poc.dispatcher.room_db.DbRepository;
import com.jst.poc.dispatcher.room_db.RoomDBModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ApiClientModule.class, RoomDBModule.class})
public class AppModule {
  private Context context;

  public AppModule(Context context) {
    this.context = context;
  }

  @Provides
  @Singleton
  Context provideContext() {
    return context;
  }

  @Provides
  @Singleton
  ViewModelProvider.Factory getViewModelFactory(Repository repository,
                                                DbRepository dbRepository) {
    return new ViewModelFactory(repository, dbRepository);
  }

}
