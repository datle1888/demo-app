package com.jst.poc.dispatcher.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiClientModule {
  private static int REQUEST_TIMEOUT = 60;
  NetworkAvailability networkAvailability;

  public ApiClientModule(NetworkAvailability networkAvailability) {
    this.networkAvailability = networkAvailability;
  }

  @Provides
  @Singleton
  Gson provideGson() {
    Gson gson = new GsonBuilder()
        .setLenient()
        .create();
    return gson;
  }


  @Provides
  @Singleton
  Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(IpClass.BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();
    return retrofit;
  }

  @Provides
  @Singleton
  OkHttpClient getOkHttpClient() {
    OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
        .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);
    HttpLoggingInterceptor login_interceptor = new HttpLoggingInterceptor();
    login_interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    httpClient.addInterceptor(login_interceptor);
    httpClient.addInterceptor(new Interceptor() {
      @Override
      public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        //Header declaration
        Request.Builder requestBulider = original.newBuilder();
//                        .addHeader("Accept", "application/json");
        //Adding authorization token
//                if (!TextUtils.isEmpty(sessionManager.getToken())){
//                    requestBulider.addHeader("Authorization","Bearer " + sessionManager.getToken());
//                }
        //Network availability check
        if (!networkAvailability.isNetworkAvailable()) {
          throw new NoConnectivityException();
        }
        Request request = requestBulider.build();
        return chain.proceed(request);
      }
    });
    OkHttpClient client = httpClient.build();
    return client;
  }

  @Provides
  @Singleton
  ApiService getApiServiceInterface(Retrofit retrofit) {
    return retrofit.create(ApiService.class);
  }

  @Provides
  @Singleton
  Repository getRepository(ApiService apiService) {
    return new Repository(apiService);
  }
}
