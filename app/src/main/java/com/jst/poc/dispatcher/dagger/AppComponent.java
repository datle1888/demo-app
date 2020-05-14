package com.jst.poc.dispatcher.dagger;

import com.jst.poc.dispatcher.activities.MainActivity;
import com.jst.poc.dispatcher.rest.ApiClientModule;
import com.jst.poc.dispatcher.rest.Repository;
import com.jst.poc.dispatcher.room_db.RoomDBModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, ApiClientModule.class, RoomDBModule.class})
@Singleton
public interface AppComponent {
  //    void doInjection(DashBoard dashBoard);
//    void doInjection(Jobs jobs);
  void doInjection(Repository repository);

  void doInjection(MainActivity mainContainer);
}
