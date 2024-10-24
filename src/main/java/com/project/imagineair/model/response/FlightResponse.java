package com.project.imagineair.model.response;

import com.project.imagineair.model.dto.FlightPriceDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightResponse extends BaseResponse{
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
  private boolean overnight;
  private FlightPriceDTO flightPrice;
}
