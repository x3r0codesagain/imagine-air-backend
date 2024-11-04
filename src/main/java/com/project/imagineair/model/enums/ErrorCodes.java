package com.project.imagineair.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodes {

  USER_NOT_FOUND("Event is not found."),
  DESTINATIONS_UNAVAILABLE("No destination is available"),
  FLIGHTS_NOT_FOUND("No flights available"),
  SERVER_ERROR("Server has issues processing the data"),
  BAD_REQUEST("Bad Request! Recheck Input Data"),
  BOOKING_FOUND("Booking has been made previously"),
  BOOKING_NOT_FOUND("Booking not found"),
  SEAT_UNAVAILABLE("Seat is no longer available please reopen tab");

  private String message;
}
