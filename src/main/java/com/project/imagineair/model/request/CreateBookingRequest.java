package com.project.imagineair.model.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateBookingRequest {
  private String currentBookingCode;
  private List<PassengerRequest> passengers;
  private BookingContactRequest bookingContact;

}
