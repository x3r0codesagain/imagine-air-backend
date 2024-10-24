package com.project.imagineair.model.request;

import com.project.imagineair.BaseMongoEntity;
import com.project.imagineair.model.FlightPrice;
import com.project.imagineair.model.response.BaseResponse;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Data
public class FlightRequest{
  private String number;
  private String origin;
  private String destination;
  private String originCode;
  private String arrivalCode;
  private String departDate;
  private String arriveDate;
  private String departLocalTime;
  private String arriveLocalTime;
  private int flightHours;
  private int flightMinutes;
  private Map<String, Integer> availableSeats;
  private FlightPriceRequest flightPrice;
  private boolean overnight = false;
}
