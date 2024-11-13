package com.project.imagineair.service.impl;

import com.project.imagineair.model.AvailableSeat;
import com.project.imagineair.model.Flight;
import com.project.imagineair.model.enums.ErrorCodes;
import com.project.imagineair.model.exceptions.AppExceptionV2;
import com.project.imagineair.model.request.FlightPriceRequest;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
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
  public List<FlightResponse> getFlightsToAndFrom(GetAvailableFlightRequest request)
      throws ParseException {

    Date now = new Date();

    Date flightDate = DateUtils.parseDate(request.getDepartDate(), "yyyy-MM-dd");

    if (flightDate.before(now)) {
      throw new AppExceptionV2(ErrorCodes.FLIGHTS_NOT_FOUND.getMessage(), ErrorCodes.FLIGHTS_NOT_FOUND);
    }

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
    Flight flightFromDB = flightRepository.findByNumberAndDepartDate(request.getNumber(), request.getDepartDate());
    if (Objects.nonNull(flightFromDB)) {
      throw new AppExceptionV2(ErrorCodes.BAD_REQUEST.getMessage(), ErrorCodes.BAD_REQUEST);
    }
    validateCreateFlightRequest(request);
    Flight flight = mapper.map(request, Flight.class);

    flightRepository.save(flight);

    FlightResponse response = mapper.map(flight, FlightResponse.class);

    return response;
  }

  @Override
  public List<FlightResponse> addFlights(int amount) {
    List<FlightResponse> responses = new ArrayList<>();

    Date now = new Date();
    String date = new SimpleDateFormat("yyyy-MM-dd").format(now);

    for (int i = 0; i < amount; i++) {

      //CGK-SIN
      FlightRequest request = new FlightRequest();
      request.setNumber("IG001");
      request.setOrigin("Jakarta");
      request.setDestination("Singapore");
      request.setOriginCode("CGK");
      request.setArrivalCode("SIN");
      request.setDepartDate(date);
      request.setArriveDate(date);
      request.setDepartLocalTime("07:30");
      request.setArriveLocalTime("10:15");
      request.setFlightHours(1);
      request.setFlightMinutes(45);

      Map<String, Integer> availableSeats = new HashMap<>();
      availableSeats.put("economy", 180);
      availableSeats.put("business", 0);
      availableSeats.put("first", 0);

      request.setAvailableSeats(availableSeats);

      FlightPriceRequest flightPriceRequest = new FlightPriceRequest();
      flightPriceRequest.setEconomyPromo(700000);
      flightPriceRequest.setEconomyStandard(1200000);
      flightPriceRequest.setEconomyFlex(2500000);
      flightPriceRequest.setBusinessPromo(100000000);
      flightPriceRequest.setBusinessStandard(20000000);
      flightPriceRequest.setBusinessFlex(30000000);
      flightPriceRequest.setFirst(1000000000);

      request.setFlightPrice(flightPriceRequest);
      request.setOvernight(false);

      try{
        responses.add(addFlight(request));
      } catch (AppExceptionV2 e) {

      }


      request = new FlightRequest();
      request.setNumber("IG003");
      request.setOrigin("Jakarta");
      request.setDestination("Singapore");
      request.setOriginCode("CGK");
      request.setArrivalCode("SIN");
      request.setDepartDate(date);
      request.setArriveDate(date);
      request.setDepartLocalTime("10:30");
      request.setArriveLocalTime("13:15");
      request.setFlightHours(1);
      request.setFlightMinutes(45);

      availableSeats = new HashMap<>();
      availableSeats.put("economy", 180);
      availableSeats.put("business", 0);
      availableSeats.put("first", 0);

      request.setAvailableSeats(availableSeats);

      flightPriceRequest = new FlightPriceRequest();
      flightPriceRequest.setEconomyPromo(700000);
      flightPriceRequest.setEconomyStandard(1200000);
      flightPriceRequest.setEconomyFlex(2500000);
      flightPriceRequest.setBusinessPromo(100000000);
      flightPriceRequest.setBusinessStandard(20000000);
      flightPriceRequest.setBusinessFlex(30000000);
      flightPriceRequest.setFirst(1000000000);

      request.setFlightPrice(flightPriceRequest);
      request.setOvernight(false);

      try{
        responses.add(addFlight(request));
      } catch (AppExceptionV2 e) {

      }

      request = new FlightRequest();
      request.setNumber("IG005");
      request.setOrigin("Jakarta");
      request.setDestination("Singapore");
      request.setOriginCode("CGK");
      request.setArrivalCode("SIN");
      request.setDepartDate(date);
      request.setArriveDate(date);
      request.setDepartLocalTime("13:30");
      request.setArriveLocalTime("16:15");
      request.setFlightHours(1);
      request.setFlightMinutes(45);

      availableSeats = new HashMap<>();
      availableSeats.put("economy", 180);
      availableSeats.put("business", 0);
      availableSeats.put("first", 0);

      request.setAvailableSeats(availableSeats);

      flightPriceRequest = new FlightPriceRequest();
      flightPriceRequest.setEconomyPromo(700000);
      flightPriceRequest.setEconomyStandard(1200000);
      flightPriceRequest.setEconomyFlex(2500000);
      flightPriceRequest.setBusinessPromo(100000000);
      flightPriceRequest.setBusinessStandard(20000000);
      flightPriceRequest.setBusinessFlex(30000000);
      flightPriceRequest.setFirst(1000000000);

      request.setFlightPrice(flightPriceRequest);
      request.setOvernight(false);

      try{
        responses.add(addFlight(request));
      } catch (AppExceptionV2 e) {

      }

      //SIN-CGK

      request = new FlightRequest();
      request.setNumber("IG002");
      request.setOrigin("Singapore");
      request.setDestination("Jakarta");
      request.setOriginCode("SIN");
      request.setArrivalCode("CGK");
      request.setDepartDate(date);
      request.setArriveDate(date);
      request.setDepartLocalTime("11:30");
      request.setArriveLocalTime("12:15");
      request.setFlightHours(1);
      request.setFlightMinutes(45);

      availableSeats = new HashMap<>();
      availableSeats.put("economy", 180);
      availableSeats.put("business", 0);
      availableSeats.put("first", 0);

      request.setAvailableSeats(availableSeats);

      flightPriceRequest = new FlightPriceRequest();
      flightPriceRequest.setEconomyPromo(700000);
      flightPriceRequest.setEconomyStandard(1200000);
      flightPriceRequest.setEconomyFlex(2500000);
      flightPriceRequest.setBusinessPromo(100000000);
      flightPriceRequest.setBusinessStandard(20000000);
      flightPriceRequest.setBusinessFlex(30000000);
      flightPriceRequest.setFirst(1000000000);

      request.setFlightPrice(flightPriceRequest);
      request.setOvernight(false);

      try{
        responses.add(addFlight(request));
      } catch (AppExceptionV2 e) {

      }

      request = new FlightRequest();
      request.setNumber("IG004");
      request.setOrigin("Singapore");
      request.setDestination("Jakarta");
      request.setOriginCode("SIN");
      request.setArrivalCode("CGK");
      request.setDepartDate(date);
      request.setArriveDate(date);
      request.setDepartLocalTime("14:30");
      request.setArriveLocalTime("15:15");
      request.setFlightHours(1);
      request.setFlightMinutes(45);

      availableSeats = new HashMap<>();
      availableSeats.put("economy", 180);
      availableSeats.put("business", 0);
      availableSeats.put("first", 0);

      request.setAvailableSeats(availableSeats);

      flightPriceRequest = new FlightPriceRequest();
      flightPriceRequest.setEconomyPromo(700000);
      flightPriceRequest.setEconomyStandard(1200000);
      flightPriceRequest.setEconomyFlex(2500000);
      flightPriceRequest.setBusinessPromo(100000000);
      flightPriceRequest.setBusinessStandard(20000000);
      flightPriceRequest.setBusinessFlex(30000000);
      flightPriceRequest.setFirst(1000000000);

      request.setFlightPrice(flightPriceRequest);
      request.setOvernight(false);

      try{
        responses.add(addFlight(request));
      } catch (AppExceptionV2 e) {

      }

      request = new FlightRequest();
      request.setNumber("IG006");
      request.setOrigin("Singapore");
      request.setDestination("Jakarta");
      request.setOriginCode("SIN");
      request.setArrivalCode("CGK");
      request.setDepartDate(date);
      request.setArriveDate(date);
      request.setDepartLocalTime("17:30");
      request.setArriveLocalTime("18:15");
      request.setFlightHours(1);
      request.setFlightMinutes(45);

      availableSeats = new HashMap<>();
      availableSeats.put("economy", 180);
      availableSeats.put("business", 0);
      availableSeats.put("first", 0);

      request.setAvailableSeats(availableSeats);

      flightPriceRequest = new FlightPriceRequest();
      flightPriceRequest.setEconomyPromo(700000);
      flightPriceRequest.setEconomyStandard(1200000);
      flightPriceRequest.setEconomyFlex(2500000);
      flightPriceRequest.setBusinessPromo(100000000);
      flightPriceRequest.setBusinessStandard(20000000);
      flightPriceRequest.setBusinessFlex(30000000);
      flightPriceRequest.setFirst(1000000000);

      request.setFlightPrice(flightPriceRequest);
      request.setOvernight(false);

      try{
        responses.add(addFlight(request));
      } catch (AppExceptionV2 e) {

      }

      //CGK-FUK
      request = new FlightRequest();
      request.setNumber("IG007");
      request.setOrigin("Jakarta");
      request.setDestination("Fukuoka");
      request.setOriginCode("CGK");
      request.setArrivalCode("FUK");
      request.setDepartDate(date);
      request.setArriveDate(date);
      request.setDepartLocalTime("00:10");
      request.setArriveLocalTime("09:10");
      request.setFlightHours(7);
      request.setFlightMinutes(0);

      availableSeats = new HashMap<>();
      availableSeats.put("economy", 180);
      availableSeats.put("business", 0);
      availableSeats.put("first", 0);

      request.setAvailableSeats(availableSeats);

      flightPriceRequest = new FlightPriceRequest();
      flightPriceRequest.setEconomyPromo(1700000);
      flightPriceRequest.setEconomyStandard(2600000);
      flightPriceRequest.setEconomyFlex(5500000);
      flightPriceRequest.setBusinessPromo(100000000);
      flightPriceRequest.setBusinessStandard(20000000);
      flightPriceRequest.setBusinessFlex(30000000);
      flightPriceRequest.setFirst(1000000000);

      request.setFlightPrice(flightPriceRequest);
      request.setOvernight(false);

      try{
        responses.add(addFlight(request));
      } catch (AppExceptionV2 e) {

      }

      //FUK-CGK
      request = new FlightRequest();
      request.setNumber("IG008");
      request.setOrigin("Fukuoka");
      request.setDestination("Jakarta");
      request.setOriginCode("FUK");
      request.setArrivalCode("CGK");
      request.setDepartDate(date);
      request.setArriveDate(date);
      request.setDepartLocalTime("12:00");
      request.setArriveLocalTime("17:10");
      request.setFlightHours(7);
      request.setFlightMinutes(0);

      availableSeats = new HashMap<>();
      availableSeats.put("economy", 180);
      availableSeats.put("business", 0);
      availableSeats.put("first", 0);

      request.setAvailableSeats(availableSeats);

      flightPriceRequest = new FlightPriceRequest();
      flightPriceRequest.setEconomyPromo(1700000);
      flightPriceRequest.setEconomyStandard(2600000);
      flightPriceRequest.setEconomyFlex(5500000);
      flightPriceRequest.setBusinessPromo(100000000);
      flightPriceRequest.setBusinessStandard(20000000);
      flightPriceRequest.setBusinessFlex(30000000);
      flightPriceRequest.setFirst(1000000000);

      request.setFlightPrice(flightPriceRequest);
      request.setOvernight(false);

      try{
        responses.add(addFlight(request));
      } catch (AppExceptionV2 e) {

      }


      now = DateUtils.addDays(now, 1);
      date = new SimpleDateFormat("yyyy-MM-dd").format(now);
    }



    return responses;
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
