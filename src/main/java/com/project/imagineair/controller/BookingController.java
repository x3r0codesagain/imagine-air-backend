package com.project.imagineair.controller;

import com.project.imagineair.model.exceptions.AppExceptionV2;
import com.project.imagineair.model.request.CancelRequest;
import com.project.imagineair.model.request.CreateBookingRequest;
import com.project.imagineair.model.request.PaymentRequest;
import com.project.imagineair.model.request.PendingBookingRequest;
import com.project.imagineair.model.request.SeatAssignRequest;
import com.project.imagineair.model.response.BookingResponse;
import com.project.imagineair.rest.response.RestSingleResponse;
import com.project.imagineair.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/booking")
public class BookingController {

  @Autowired
  private BookingService bookingService;

  @PostMapping("/public-all-access/validate/pending")
  public RestSingleResponse<BookingResponse> validateSelection(@RequestBody
  PendingBookingRequest request) {
    try {
      BookingResponse response = bookingService.validateBooking(request);
      return new RestSingleResponse<>(null, null, true, response);
    } catch (AppExceptionV2 appExceptionV2) {
      return new RestSingleResponse<>(appExceptionV2.getMessage(),
          appExceptionV2.getCode().name(), false, null);
    } catch (Exception e) {
      return new RestSingleResponse<>("Internal Server Error",
          HttpStatus.INTERNAL_SERVER_ERROR.name(), false, null);
    }
  }

  @PostMapping("/public-all-access/create")
  public RestSingleResponse<BookingResponse> createBooking(@RequestBody CreateBookingRequest request) {
    try {
      BookingResponse response = bookingService.createBooking(request);
      return new RestSingleResponse<>(null, null, true, response);
    } catch (AppExceptionV2 appExceptionV2) {
      return new RestSingleResponse<>(appExceptionV2.getMessage(),
          appExceptionV2.getCode().name(), false, null);
    } catch (Exception e) {
      return new RestSingleResponse<>("Internal Server Error",
          HttpStatus.INTERNAL_SERVER_ERROR.name(), false, null);
    }
  }

  @GetMapping("/public-all-access/getBooking")
  public RestSingleResponse<BookingResponse> getBooking(@RequestParam String code) {
    try {
      BookingResponse response = bookingService.getBooking(code);
      return new RestSingleResponse<>(null, null, true, response);
    } catch (AppExceptionV2 appExceptionV2) {
      return new RestSingleResponse<>(appExceptionV2.getMessage(),
          appExceptionV2.getCode().name(), false, null);
    } catch (Exception e) {
      return new RestSingleResponse<>("Internal Server Error",
          HttpStatus.INTERNAL_SERVER_ERROR.name(), false, null);
    }
  }

  @GetMapping("/public-all-access/getMyBooking")
  public RestSingleResponse<BookingResponse> getMyBooking(@RequestParam String code,
      @RequestParam String surname) {
    try {
      BookingResponse response = bookingService.findMyBooking(code, surname);
      return new RestSingleResponse<>(null, null, true, response);
    } catch (AppExceptionV2 appExceptionV2) {
      return new RestSingleResponse<>(appExceptionV2.getMessage(),
          appExceptionV2.getCode().name(), false, null);
    } catch (Exception e) {
      return new RestSingleResponse<>("Internal Server Error",
          HttpStatus.INTERNAL_SERVER_ERROR.name(), false, null);
    }
  }

  @GetMapping("/public-all-access/getBooking/unpaid")
  public RestSingleResponse<BookingResponse> getUnpaidBooking(@RequestParam String code) {
    try {
      BookingResponse response = bookingService.getUnpaidBooking(code);
      return new RestSingleResponse<>(null, null, true, response);
    } catch (AppExceptionV2 appExceptionV2) {
      return new RestSingleResponse<>(appExceptionV2.getMessage(),
          appExceptionV2.getCode().name(), false, null);
    } catch (Exception e) {
      return new RestSingleResponse<>("Internal Server Error",
          HttpStatus.INTERNAL_SERVER_ERROR.name(), false, null);
    }
  }

  @PostMapping("/public-all-access/seats/assign")
  public RestSingleResponse<BookingResponse> assignSeat(@RequestBody SeatAssignRequest request) {
    try {
      BookingResponse response = bookingService.assignSeat(request);
      return new RestSingleResponse<>(null, null, true, response);
    } catch (AppExceptionV2 appExceptionV2) {
      return new RestSingleResponse<>(appExceptionV2.getMessage(),
          appExceptionV2.getCode().name(), false, null);
    } catch (Exception e) {
      return new RestSingleResponse<>("Internal Server Error",
          HttpStatus.INTERNAL_SERVER_ERROR.name(), false, null);
    }
  }

  @PostMapping("/public-all-access/pay")
  public RestSingleResponse<BookingResponse> pay(@RequestBody PaymentRequest request) {
    try {
      BookingResponse response = bookingService.processPayment(request);
      return new RestSingleResponse<>(null, null, true, response);
    } catch (AppExceptionV2 appExceptionV2) {
      return new RestSingleResponse<>(appExceptionV2.getMessage(),
          appExceptionV2.getCode().name(), false, null);
    } catch (Exception e) {
      return new RestSingleResponse<>("Internal Server Error",
          HttpStatus.INTERNAL_SERVER_ERROR.name(), false, null);
    }
  }

  @PostMapping("/public-all-access/cancel")
  public RestSingleResponse<BookingResponse> cancel(@RequestBody CancelRequest request) {
    try {
      BookingResponse response = bookingService.cancelBooking(request);
      return new RestSingleResponse<>(null, null, true, response);
    } catch (AppExceptionV2 appExceptionV2) {
      return new RestSingleResponse<>(appExceptionV2.getMessage(),
          appExceptionV2.getCode().name(), false, null);
    } catch (Exception e) {
      return new RestSingleResponse<>("Internal Server Error",
          HttpStatus.INTERNAL_SERVER_ERROR.name(), false, null);
    }
  }


}
