package com.jst.poc.dispatcher.rest;

public class IpClass {

  //Test
//    public static final String BASE_URL = "http://192.168.1.123:3001/";
//    public static final String BASE_URL = "http://10.0.2.2:3001/";
//    public static final String BASE_URL = "http://172.20.10.4:3001/";
//    public static final String BASE_URL = "http://172.17.172.35:3001/";
  public static final String BASE_URL = "http://173.249.34.254:3021/";

  //Check for pending job
  public static final String check_pending_job = "fetchJobs";

  //Check for pending job
  public static final String check_new_plates = "fetchPlates";

  //Change job status
  public static final String update_job_status = "updateJobStatus";

  //Change patrol status
  public static final String update_patrol_status = "updatePatrolStatus";

  //Fetch completed jobs
  public static final String fetch_completed_jobs = "fetchCompletedJobs";

  //Google directions api
  public static final String google_directions = "https://maps.googleapis.com/maps/api/directions/json?units=metric";

  //Geo code api
  public static final String google_geo_code_api = "https://maps.googleapis.com/maps/api/geocode/json";

  //Face detection web view url
  public static final String face_detection_url = "http://192.168.1.203:9001/face";

  //Plate detection web view url
  public static final String plate_detection_url = "http://192.168.1.203:9001/iframe";

  //-------------------------------------------------------------------------------------
  //                              Camera Live stream
  //---------------------------------------------------------------------------------------


  //Cam1
  public static final String camera1 = "rtsp://144.91.79.136:8554/live.sdp";

  //Cam2
  public static final String camera2 = "rtsp://170.93.143.139/rtplive/470011e600ef003a004ee33696235daa";

  //Cam1
  public static final String CarCh1 = "http://185.173.32.12:12060/live.flv?devid=009800012C&chl=1&svrid=127.0.0.1&svrport=17891&st=0&audio=1";
  //Cam2
  public static final String CarCh2 = "http://185.173.32.12:12060/live.flv?devid=009800012C&chl=2&svrid=127.0.0.1&svrport=17891&st=0&audio=1";
  //Cam4
  public static final String CarCh4 = "http://185.173.32.12:12060/live.flv?devid=009800012C&chl=4&svrid=127.0.0.1&svrport=17891&st=0&audio=1";
  //Cam7
  public static final String CarCh7 = "http://185.173.32.12:12060/live.flv?devid=009800012C&chl=7&svrid=127.0.0.1&svrport=17891&st=0&audio=1";
  //Cam8
  public static final String CarCh8 = "http://185.173.32.12:12060/live.flv?devid=009800012C&chl=8&svrid=127.0.0.1&svrport=17891&st=0&audio=1";


  //Live streaming Main Path Production
  //public static final String camera1 = "rtsp://192.168.1.203:8553/ds-test";

  //public static final String camera2 = "rtsp://192.168.1.204:8553/ds-test";


  //Live streaming Main Path Testing
  //public static final String camera1 = "rtsp://192.168.68.150:8553/ds-test";

  //public static final String camera2 = "rtsp://192.168.68.151:8553/ds-test";

  //-------------------------------------------------------------------------------------
  //                              Camera Live stream End here
  //---------------------------------------------------------------------------------------


  //-------------------------------------------------------------------------------------
  //                              Socket url start here
  //---------------------------------------------------------------------------------------


  //Socket url normal
  public static final String socketUrl = "ws://144.91.79.136:9000/get-notifications/";

  //Socket url production
  //public static final String socketUrl = "ws://192.168.1.203:9000/get-notifications/";

  //Socket url testing
  //public static final String socketUrl = "ws://192.168.68.150:9000/get-notifications/";

  //-------------------------------------------------------------------------------------
  //                              Socket url end here
  //---------------------------------------------------------------------------------------
}
