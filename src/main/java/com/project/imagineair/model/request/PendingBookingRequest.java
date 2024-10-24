package com.project.imagineair.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PendingBookingRequest {
  private int totalAdultPax;
  private int totalChildPax;
  private int totalInfantPax;
  private String outboundFlightNo;
  private String returnFlightNo;
  private String travelClass;
  private String oneway;
  private String departDate;
  private String returnDate;
  private String outboundFareType;
  private String returnFareType;
  private int outboundPrice;
  private int returnPrice;
}
