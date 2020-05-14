package com.jst.poc.dispatcher.viewmodels;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.entraze.rxwebsocket.RxWebSocket;
import com.entraze.rxwebsocket.SocketEventTypeEnum;
import com.entraze.rxwebsocket.entities.SocketClosedEvent;
import com.entraze.rxwebsocket.entities.SocketFailureEvent;
import com.entraze.rxwebsocket.entities.SocketMessageEvent;
import com.entraze.rxwebsocket.entities.SocketOpenEvent;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jst.poc.dispatcher.models.BeanSocketEventObj;
import com.jst.poc.dispatcher.models.BeanSocketObjFace;
import com.jst.poc.dispatcher.models.BeanSocketObjNum;
import com.jst.poc.dispatcher.models.BeanSocketObjPing;
import com.jst.poc.dispatcher.models.BeanSocketObjWeapon;
import com.jst.poc.dispatcher.rest.IpClass;
import com.jst.poc.dispatcher.room_db.DbRepository;
import com.jst.poc.dispatcher.utilities.LocationService;
import com.jst.poc.dispatcher.utilities.Logger;

import org.reactivestreams.Subscription;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SocketViewModel extends ViewModel {

  private static final String TAG = "~~SocketViewModel";

  //Local variables
  private final CompositeDisposable disposables = new CompositeDisposable();
  private Long lastPingTime = System.currentTimeMillis();
  private Gson gson = new Gson();
  private DbRepository dbRepository;
  //Live data init
  private MutableLiveData<BeanSocketEventObj> liveData = new MutableLiveData<>();
  private MutableLiveData<Throwable> liveDataError = new MutableLiveData<>();
  private MutableLiveData<Boolean> liveDataSocketClose = new MutableLiveData<>();
  private RxWebSocket rxWebSocket = new RxWebSocket(IpClass.socketUrl);

  //Constructor
  public SocketViewModel(DbRepository dbRepository) {
    this.dbRepository = dbRepository;
  }

  //Getter
  public LiveData<BeanSocketEventObj> getLiveData() {
    return liveData;
  }

  public Long getLastPingTime() {
    return lastPingTime;
  }

  public LiveData<Throwable> getLiveDataError() {
    return liveDataError;
  }

  public LiveData<Boolean> getLiveDataSocketClose() {
    return liveDataSocketClose;
  }

  public void connectSocket() {
    //Connect only if location data != null
    if (LocationService.BestLocation != null) {
      rxWebSocket.connect();
      onSocketOpen();
      onMessageReceived();
      onSocketFailed();
      onSocketClosed();
    }
  }

  private void onSocketOpen() {
    disposables.add(rxWebSocket.onOpen()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Subscription>() {
          @Override
          public void accept(Subscription subscription) throws Exception {
          }
        })
        .subscribe(new Consumer<SocketOpenEvent>() {
          @Override
          public void accept(SocketOpenEvent socketOpenEvent) throws Exception {
            BeanSocketEventObj obj = new BeanSocketEventObj();
            obj.setObject(socketOpenEvent);
            obj.setTypeEnum(SocketEventTypeEnum.OPEN);
            liveData.setValue(obj);
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            liveDataError.setValue(throwable);
          }
        }));
  }

  private void onSocketClosed() {
    disposables.add(rxWebSocket.onClosed()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Subscription>() {
          @Override
          public void accept(Subscription subscription) throws Exception {
          }
        })
        .subscribe(new Consumer<SocketClosedEvent>() {
          @Override
          public void accept(SocketClosedEvent socketClosedEvent) throws Exception {
            BeanSocketEventObj obj = new BeanSocketEventObj();
            obj.setObject(socketClosedEvent);
            obj.setTypeEnum(SocketEventTypeEnum.CLOSED);
            liveData.setValue(obj);
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            liveDataError.setValue(throwable);
          }
        }));
  }

  private void onSocketFailed() {
    disposables.add(rxWebSocket.onFailure()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Subscription>() {
          @Override
          public void accept(Subscription subscription) throws Exception {
          }
        })
        .subscribe(new Consumer<SocketFailureEvent>() {
          @Override
          public void accept(SocketFailureEvent socketFailureEvent) throws Exception {
            BeanSocketEventObj obj = new BeanSocketEventObj();
            obj.setObject(socketFailureEvent);
            obj.setTypeEnum(SocketEventTypeEnum.FAILURE);
            liveData.setValue(obj);
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            liveDataError.setValue(throwable);
          }
        }));
  }

  private void onMessageReceived() {
    disposables.add(rxWebSocket.onTextMessage()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Subscription>() {
          @Override
          public void accept(Subscription subscription) throws Exception {
          }
        })
        .subscribe(new Consumer<SocketMessageEvent>() {
          @Override
          public void accept(SocketMessageEvent socketMessageEvent) throws Exception {
            try {
              JsonElement element =
                  gson.fromJson(socketMessageEvent.getText(), JsonElement.class);
              if (element != null) {
                if (element instanceof JsonArray) {
                  JsonArray jsonArray = element.getAsJsonArray();
                  for (JsonElement obj : jsonArray) {
                    Object object = returnObjFromJson((JsonObject) obj);
                    if (object != null) {
                      if (object instanceof BeanSocketObjFace) {
                        BeanSocketObjFace objFace = (BeanSocketObjFace) object;
                        if (!TextUtils.isEmpty(ApiViewModel.current_location_address)) {
                          objFace.setArea(ApiViewModel.current_location_address);
                        }
                        Logger.getInstance().verbose_log(TAG, "Face : " + objFace.toString());
                        dbRepository.insertFace(objFace);
                        // Logger.getInstance().verbose_log(TAG, objFace.toString());
                      } else if (object instanceof BeanSocketObjNum) {
                        BeanSocketObjNum objNum = (BeanSocketObjNum) object;
                        Logger.getInstance().verbose_log(TAG, "Num : " + objNum.toString());
                        dbRepository.insertNumberPlate(objNum);
                        // Logger.getInstance().verbose_log(TAG, objNum.toString());
                      } else if (object instanceof BeanSocketObjPing) {
                        lastPingTime = System.currentTimeMillis();
                      } else if (object instanceof BeanSocketObjWeapon) {
                        BeanSocketObjWeapon weapon = (BeanSocketObjWeapon) object;
                        if (!TextUtils.isEmpty(ApiViewModel.current_location_address)) {
                          weapon.setArea(ApiViewModel.current_location_address);
                        }
                        Logger.getInstance().verbose_log(TAG, "Face : " + weapon.toString());
                        dbRepository.insertWeapon(weapon);
                      }
                    }
                  }
                }
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
            BeanSocketEventObj obj = new BeanSocketEventObj();
            obj.setObject(socketMessageEvent);
            obj.setTypeEnum(SocketEventTypeEnum.MESSAGE);
            liveData.setValue(obj);
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            liveDataError.setValue(throwable);
          }
        }));
  }

  public void closeSocket() {
    disposables.add(rxWebSocket.close()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override
          public void accept(Disposable disposable) throws Exception {
          }
        })
        .subscribe(new Consumer<Boolean>() {
          @Override
          public void accept(Boolean aBoolean) throws Exception {
            liveDataSocketClose.setValue(aBoolean);
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            liveDataError.setValue(throwable);
          }
        }));
  }

  private Object returnObjFromJson(JsonObject jsonObject) {
    Object object = null;
    try {
      JsonElement type = jsonObject.get("type");
      String str = type.getAsString();
      switch (str) {
        case "face":
          object = gson.fromJson(jsonObject.toString(), BeanSocketObjFace.class);
          break;
        case "ping":
          object = gson.fromJson(jsonObject.toString(), BeanSocketObjPing.class);
          break;
        case "numer-plate":
          object = gson.fromJson(jsonObject.toString(), BeanSocketObjNum.class);
          break;
        case "weapon":
          object = gson.fromJson(jsonObject.toString(), BeanSocketObjWeapon.class);
          break;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return object;
  }

  @Override
  protected void onCleared() {
    disposables.clear();
    super.onCleared();
  }
}
