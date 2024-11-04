package com.project.imagineair.model.request;

import lombok.Data;

@Data
public class SeatAssignRequest {
  private int index;
  private String bookingCode;
  private String seat;
  private String departDate;
  private String flight;
}
