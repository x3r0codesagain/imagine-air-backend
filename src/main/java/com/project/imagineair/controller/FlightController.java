package com.project.imagineair.controller;

import com.project.imagineair.model.exceptions.AppExceptionV2;
import com.project.imagineair.model.request.FlightRequest;
import com.project.imagineair.model.request.GetAvailableFlightRequest;
import com.project.imagineair.model.response.FlightResponse;
import com.project.imagineair.model.response.OccupiedSeatResponse;
import com.project.imagineair.rest.response.RestListResponse;
import com.project.imagineair.rest.response.RestSingleResponse;
import com.project.imagineair.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightController {
  @Autowired
  private FlightService flightService;

  @PostMapping("/public-all-access/get-flights-to-from")
  public RestListResponse<FlightResponse> getFlightsToAndFrom(@RequestBody
      GetAvailableFlightRequest request) {
    try {
      List<FlightResponse> responses = flightService.getFlightsToAndFrom(request);
      return new RestListResponse<>(null, null, true, responses);
    } catch (AppExceptionV2 appExceptionV2) {
      return new RestListResponse<>(appExceptionV2.getMessage(),
          appExceptionV2.getCode().name(), false, null);
    } catch (Exception e) {
      return new RestListResponse<>("Internal Server Error",
          HttpStatus.INTERNAL_SERVER_ERROR.name(), false, null);
    }
  }

  @GetMapping("/public-all-access/get-occupied-seats")
  public RestSingleResponse<OccupiedSeatResponse> getOccupiedSeats(@RequestParam String flNumber,
      @RequestParam String departDate) {
    try {
      OccupiedSeatResponse response = flightService.getOccupiedSeats(flNumber, departDate);
      return new RestSingleResponse<>(null, null, true, response);
    } catch (AppExceptionV2 appExceptionV2) {
      return new RestSingleResponse<>(appExceptionV2.getMessage(),
          appExceptionV2.getCode().name(), false, null);
    } catch (Exception e) {
      return new RestSingleResponse<>("Internal Server Error",
          HttpStatus.INTERNAL_SERVER_ERROR.name(), false, null);
    }
  }

  @PostMapping("/admin/create")
  public RestSingleResponse<FlightResponse> createFlight(@RequestBody FlightRequest request) {
    try {
      FlightResponse response = flightService.addFlight(request);
      return new RestSingleResponse<>(null, null, true, response);
    } catch (AppExceptionV2 appExceptionV2) {
      return new RestSingleResponse<>(appExceptionV2.getMessage(),
          appExceptionV2.getCode().name(), false, null);
    } catch (Exception e) {
      return new RestSingleResponse<>("Internal Server Error",
          HttpStatus.INTERNAL_SERVER_ERROR.name(), false, null);
    }
  }

  @PostMapping("/admin/create/bulk")
  public RestListResponse<FlightResponse> createFlights(@RequestParam int days) {
    try {
      List<FlightResponse> responses = flightService.addFlights(days);
      return new RestListResponse<>(null, null, true, responses);
    } catch (AppExceptionV2 appExceptionV2) {
      return new RestListResponse<>(appExceptionV2.getMessage(),
          appExceptionV2.getCode().name(), false, null);
    } catch (Exception e) {
      return new RestListResponse<>("Internal Server Error",
          HttpStatus.INTERNAL_SERVER_ERROR.name(), false, null);
    }
  }
}
