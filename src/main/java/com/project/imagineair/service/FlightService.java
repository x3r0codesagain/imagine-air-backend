package com.project.imagineair.service;

import com.project.imagineair.model.request.FlightRequest;
import com.project.imagineair.model.request.GetAvailableFlightRequest;
import com.project.imagineair.model.response.FlightResponse;
import com.project.imagineair.model.response.OccupiedSeatResponse;

import java.util.Date;
import java.util.List;

public interface FlightService {
  List<FlightResponse> getFlightsToAndFrom(GetAvailableFlightRequest request);

  FlightResponse addFlight(FlightRequest request);
  OccupiedSeatResponse getOccupiedSeats(String flNumber, String departDate);
}
