package com.jst.poc.dispatcher.rest_responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RespGeoReverseCode {

  @SerializedName("results")
  @Expose
  private List<Result> results = null;
  @SerializedName("status")
  @Expose
  private String status;


  public List<Result> getResults() {
    return results;
  }

  public void setResults(List<Result> results) {
    this.results = results;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  //************ Class Result *********************
  public class Result {


    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;
    @SerializedName("place_id")
    @Expose
    private String placeId;


    public String getFormattedAddress() {
      return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
      this.formattedAddress = formattedAddress;
    }

    public String getPlaceId() {
      return placeId;
    }

    public void setPlaceId(String placeId) {
      this.placeId = placeId;
    }
  }

}
