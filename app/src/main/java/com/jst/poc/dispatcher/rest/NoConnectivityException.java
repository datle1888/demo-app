package com.jst.poc.dispatcher.rest;

import java.io.IOException;

public class NoConnectivityException extends IOException {
  String no_internet_msg = "No internet";
  @Override
  public String getMessage() {
    return no_internet_msg;
  }
}
