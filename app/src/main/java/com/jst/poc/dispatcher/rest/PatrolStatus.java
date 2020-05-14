package com.jst.poc.dispatcher.rest;

public enum PatrolStatus {
  available_via_radio,
  available_at_station,
  pause_via_radio,
  pause_at_station,
  alarmed_via_radio,
  alarmed_at_sattion,
  reserved_via_radio,
  reserved_at_station,
  finished_via_radio,
  on_the_way,
  arrived,
  not_available,
  loading
}
