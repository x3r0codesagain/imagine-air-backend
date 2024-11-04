package com.project.imagineair.service.impl;

import com.project.imagineair.model.Flight;
import com.project.imagineair.model.enums.ErrorCodes;
import com.project.imagineair.model.exceptions.AppExceptionV2;
import com.project.imagineair.model.request.FlightRequest;
import com.project.imagineair.model.request.GetAvailableFlightRequest;
import com.project.imagineair.model.response.FlightResponse;
import com.project.imagineair.model.response.OccupiedSeatResponse;
import com.project.imagineair.repository.FlightRepository;
import com.project.imagineair.service.FlightService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService {

  @Autowired
  private FlightRepository flightRepository;

  @Autowired
  private Mapper mapper;

  @Override
  public List<FlightResponse> getFlightsToAndFrom(GetAvailableFlightRequest request) {
    List<Flight> flights =
        flightRepository.findAllByOriginCodeAndArrivalCodeAndDepartDate(request.getOriginCode(),
            request.getArrivalCode(), request.getDepartDate()).stream()
            .filter(fl -> fl.getAvailableSeats().get(request.getTravelClass().toLowerCase()) >= request.getTotalSeats())
            .sorted(Comparator.comparing(f -> f.getDepartLocalTime()))
            .collect(Collectors.toList());
    if (CollectionUtils.isEmpty(flights)) {
      throw new AppExceptionV2(ErrorCodes.FLIGHTS_NOT_FOUND.getMessage(), ErrorCodes.FLIGHTS_NOT_FOUND);
    }
    List<FlightResponse> responses =
        flights.stream().map(flight -> mapper.map(flight, FlightResponse.class))
            .collect(Collectors.toList());
    return responses;
  }

  @Override
  public FlightResponse addFlight(FlightRequest request) {
    validateCreateFlightRequest(request);
    Flight flight = mapper.map(request, Flight.class);

    flightRepository.save(flight);

    FlightResponse response = mapper.map(flight, FlightResponse.class);

    return response;
  }

  @Override
  public OccupiedSeatResponse getOccupiedSeats(String flNumber, String departDate) {

    Flight flight = flightRepository.findByNumberAndDepartDate(flNumber, departDate);

    if (Objects.isNull(flight)) {
      throw new AppExceptionV2(ErrorCodes.FLIGHTS_NOT_FOUND.getMessage(), ErrorCodes.FLIGHTS_NOT_FOUND);
    }

    List<String> seats = flight.getSeatsAvailability().entrySet().stream().filter(e -> e.getValue() == false)
        .map(Map.Entry::getKey).collect(
        Collectors.toList());

    OccupiedSeatResponse response = OccupiedSeatResponse.builder().seats(seats).build();

    return response;
  }

  private Date getConvertedTime(String date, String time) {
    try {
      return DateUtils.parseDate(date + " " + time);
    } catch (Exception e) {
      throw new AppExceptionV2(ErrorCodes.SERVER_ERROR.getMessage(), ErrorCodes.SERVER_ERROR);
    }
  }

  private void validateCreateFlightRequest(FlightRequest request) {
    if (Objects.nonNull(flightRepository.findByNumberAndDepartDate(request.getNumber(),
        request.getDepartDate()))) {
      throw new AppExceptionV2(ErrorCodes.BAD_REQUEST.getMessage(), ErrorCodes.BAD_REQUEST);
    }
    if (StringUtils.isBlank(request.getNumber())) {
      throw new AppExceptionV2(ErrorCodes.BAD_REQUEST.getMessage(), ErrorCodes.BAD_REQUEST);
    }
    if (StringUtils.isBlank(request.getOrigin())) {
      throw new AppExceptionV2(ErrorCodes.BAD_REQUEST.getMessage(), ErrorCodes.BAD_REQUEST);
    }
    if (StringUtils.isBlank(request.getDestination())) {
      throw new AppExceptionV2(ErrorCodes.BAD_REQUEST.getMessage(), ErrorCodes.BAD_REQUEST);
    }
    if (StringUtils.isBlank(request.getOriginCode())) {
      throw new AppExceptionV2(ErrorCodes.BAD_REQUEST.getMessage(), ErrorCodes.BAD_REQUEST);
    }
    if (StringUtils.isBlank(request.getArrivalCode())) {
      throw new AppExceptionV2(ErrorCodes.BAD_REQUEST.getMessage(), ErrorCodes.BAD_REQUEST);
    }
    if (request.getFlightHours() == 0 && request.getFlightMinutes() == 0) {
      throw new AppExceptionV2(ErrorCodes.BAD_REQUEST.getMessage(), ErrorCodes.BAD_REQUEST);
    }
    if (Objects.isNull(request.getDepartDate()) || Objects.isNull(request.getArrivalCode())) {
      throw new AppExceptionV2(ErrorCodes.BAD_REQUEST.getMessage(), ErrorCodes.BAD_REQUEST);
    }

    if (request.getAvailableSeats().isEmpty() || Objects.isNull(request.getFlightPrice())) {
      throw new AppExceptionV2(ErrorCodes.BAD_REQUEST.getMessage(), ErrorCodes.BAD_REQUEST);
    }

    if (Objects.isNull(request.getFlightPrice().isEcoPromoSwitch())) {
      throw new AppExceptionV2(ErrorCodes.BAD_REQUEST.getMessage(), ErrorCodes.BAD_REQUEST);
    }

    if (Objects.isNull(request.getFlightPrice().isBusinessPromoSwitch())) {
      throw new AppExceptionV2(ErrorCodes.BAD_REQUEST.getMessage(), ErrorCodes.BAD_REQUEST);
    }
  }
}
