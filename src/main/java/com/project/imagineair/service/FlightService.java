package com.project.imagineair.service;

import com.project.imagineair.model.request.FlightRequest;
import com.project.imagineair.model.request.GetAvailableFlightRequest;
import com.project.imagineair.model.response.FlightResponse;
import com.project.imagineair.model.response.OccupiedSeatResponse;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface FlightService {
  List<FlightResponse> getFlightsToAndFrom(GetAvailableFlightRequest request) throws ParseException;

  FlightResponse addFlight(FlightRequest request);
  List<FlightResponse> addFlights(int amount);
  OccupiedSeatResponse getOccupiedSeats(String flNumber, String departDate);
}
