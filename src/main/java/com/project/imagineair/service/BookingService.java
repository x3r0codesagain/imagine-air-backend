package com.project.imagineair.service;

import com.project.imagineair.model.request.CancelRequest;
import com.project.imagineair.model.request.CreateBookingRequest;
import com.project.imagineair.model.request.PaymentRequest;
import com.project.imagineair.model.request.PendingBookingRequest;
import com.project.imagineair.model.request.SeatAssignRequest;
import com.project.imagineair.model.response.BookingResponse;

public interface BookingService {
  BookingResponse validateBooking(PendingBookingRequest pendingBookingRequest);
  BookingResponse createBooking(CreateBookingRequest request);
  BookingResponse processPayment(PaymentRequest request);
  BookingResponse getBooking(String code);
  BookingResponse findMyBooking(String code, String surname);
  BookingResponse getUnpaidBooking(String code);
  BookingResponse assignSeat(SeatAssignRequest request);
  BookingResponse cancelBooking(CancelRequest request);
}
