package com.project.imagineair.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAvailableFlightRequest {
  private String originCode;
  private String arrivalCode;
  private String departDate;
  private String travelClass;
  private int totalSeats;
}
