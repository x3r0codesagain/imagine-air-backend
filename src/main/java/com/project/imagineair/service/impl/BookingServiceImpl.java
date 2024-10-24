package com.project.imagineair.service.impl;

import com.project.imagineair.model.Booking;
import com.project.imagineair.model.BookingCode;
import com.project.imagineair.model.BookingContact;
import com.project.imagineair.model.Fare;
import com.project.imagineair.model.Flight;
import com.project.imagineair.model.Passenger;
import com.project.imagineair.model.Payment;
import com.project.imagineair.model.PendingBooking;
import com.project.imagineair.model.enums.BookingStatus;
import com.project.imagineair.model.enums.ErrorCodes;
import com.project.imagineair.model.exceptions.AppExceptionV2;
import com.project.imagineair.model.request.CreateBookingRequest;
import com.project.imagineair.model.request.PassengerRequest;
import com.project.imagineair.model.request.PaymentRequest;
import com.project.imagineair.model.request.PendingBookingRequest;
import com.project.imagineair.model.response.BookingResponse;
import com.project.imagineair.repository.BookingCodeRepository;
import com.project.imagineair.repository.BookingRepository;
import com.project.imagineair.repository.FlightRepository;
import com.project.imagineair.repository.PendingBookingRepository;
import com.project.imagineair.service.BookingService;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

  @Autowired
  private PendingBookingRepository pendingBookingRepository;

  @Autowired
  private BookingCodeRepository codeRepository;

  @Autowired
  private FlightRepository flightRepository;

  @Autowired
  private Mapper mapper;

  @Autowired
  private BookingRepository bookingRepository;

  @Override
  public BookingResponse validateBooking(PendingBookingRequest pendingBookingRequest) {
    Flight flight = flightRepository.findByNumberAndDepartDate(pendingBookingRequest.getOutboundFlightNo(), pendingBookingRequest.getDepartDate());
    if (Objects.isNull(flight)) {
      throw new AppExceptionV2(ErrorCodes.FLIGHTS_NOT_FOUND.getMessage(), ErrorCodes.FLIGHTS_NOT_FOUND);
    }
    Flight returnFlight = null;
    Fare returnFare = null;
    if (pendingBookingRequest.getOneway().equals("false")) {
      returnFlight = flightRepository.findByNumberAndDepartDate(pendingBookingRequest.getReturnFlightNo(), pendingBookingRequest.getReturnDate());
      if (Objects.isNull(returnFlight)) {
        throw new AppExceptionV2(ErrorCodes.FLIGHTS_NOT_FOUND.getMessage(), ErrorCodes.FLIGHTS_NOT_FOUND);
      }
      updateFlight(pendingBookingRequest, returnFlight);
      returnFare = new Fare(pendingBookingRequest.getReturnPrice(),
          pendingBookingRequest.getReturnFareType());
    }

    updateFlight(pendingBookingRequest, flight);

    int totalSeats = getTotalSeats(pendingBookingRequest.getTotalAdultPax(), pendingBookingRequest.getTotalChildPax());
    String bookingCode = generateBookingCode();
    saveBookingCode(bookingCode, new Date());

    int totalPrice = getTotalPrice(pendingBookingRequest, totalSeats);
    PendingBooking pendingBooking = PendingBooking
        .builder()
        .bookingCode(bookingCode)
        .outboundFlight(flight)
        .returnFlight(returnFlight)
        .totalPax(
            pendingBookingRequest.getTotalAdultPax() + pendingBookingRequest.getTotalChildPax() + pendingBookingRequest.getTotalInfantPax())
        .totalSeats(totalSeats)
        .outboundPrice(new Fare(pendingBookingRequest.getOutboundPrice(), pendingBookingRequest.getOutboundFareType()))
        .returnPrice(returnFare)
        .totalPrice(totalPrice)
        .travelClass(pendingBookingRequest.getTravelClass())
        .build();

    Date now = new Date();
    pendingBooking.setCreatedDate(now);
    pendingBooking.setUpdatedDate(now);
    pendingBookingRepository.save(pendingBooking);

    BookingResponse response = mapper.map(pendingBooking, BookingResponse.class);

//    response.setTotalAdultPax(pendingBookingRequest.getTotalAdultPax());
//    response.setTotalChildPax(pendingBookingRequest.getTotalChildPax());
//    response.setTotalInfantPax(pendingBookingRequest.getTotalInfantPax());
//    response.setOutboundFlightNo(pendingBookingRequest.getOutboundFlightNo());
//    response.setReturnFlightNo(pendingBookingRequest.getReturnFlightNo());

    return response;
  }

  private static int getTotalSeats(int adult, int child) {
    return adult + child;
  }

  private int getTotalPrice(PendingBookingRequest pendingBookingRequest, int totalSeats) {
    int totalPrice = (pendingBookingRequest.getOutboundPrice() + pendingBookingRequest.getReturnPrice()) * totalSeats;
    if (pendingBookingRequest.getTotalInfantPax() > 0) {
      totalPrice += ((pendingBookingRequest.getOutboundPrice() * 0.5 * pendingBookingRequest.getTotalInfantPax()) +
          (pendingBookingRequest.getReturnPrice() * 0.5 * pendingBookingRequest.getTotalInfantPax()));
    }
    return totalPrice;
  }

  @Override
  public BookingResponse createBooking(CreateBookingRequest request) {

    PendingBooking pendingBooking = pendingBookingRepository.findByBookingCode(
        request.getCurrentBookingCode());

    if (Objects.isNull(pendingBooking)) {
      throw new AppExceptionV2(ErrorCodes.BOOKING_NOT_FOUND.getMessage(), ErrorCodes.BOOKING_NOT_FOUND);
    }

    List<Passenger> passengers =
        request.getPassengers().stream().map(passenger -> mapper.map(passenger, Passenger.class))
            .collect(Collectors.toList());
    BookingContact contact = mapper.map(request.getBookingContact(), BookingContact.class);

    Flight outboundFl = pendingBooking.getOutboundFlight();
    Flight returnFl = null;
    if (pendingBooking.getReturnFlight() != null) {
      returnFl = pendingBooking.getReturnFlight();
    }
    int totalSeats = pendingBooking.getTotalSeats();
    int totalPrice = pendingBooking.getTotalPrice();
    int totalPax = pendingBooking.getTotalPax();
    Date now = new Date();

    Booking booking = Booking.builder()
        .bookingCode(request.getCurrentBookingCode())
        .status(BookingStatus.PENDING.getName())
        .passengers(passengers)
        .outboundFlight(outboundFl)
        .returnFlight(returnFl)
        .totalPrice(totalPrice)
        .totalSeats(totalSeats)
        .travelClass(pendingBooking.getTravelClass())
        .totalPax(totalPax)
        .bookingContact(contact)
        .build();

    booking.setCreatedDate(now);
    booking.setUpdatedDate(now);

    bookingRepository.save(booking);

    BookingResponse response = mapper.map(booking, BookingResponse.class);
//    response.setOutboundFlightNo(outboundFl.getNumber());
//    response.setReturnFlightNo(returnFl.getNumber());

    pendingBookingRepository.deleteByBookingCode(request.getCurrentBookingCode());


    return response;
  }

  @Override
  public BookingResponse processPayment(PaymentRequest request) {
    Booking booking = bookingRepository.findByBookingCode(request.getBookingCode());

    if (Objects.isNull(booking) || !BookingStatus.PENDING.getName().equals(booking.getStatus())) {
      throw new AppExceptionV2(ErrorCodes.BOOKING_NOT_FOUND.getMessage(), ErrorCodes.BOOKING_NOT_FOUND);
    }
    Date now = new Date();
    Payment payment = Payment.builder()
        .cardNo(request.getCardNo())
        .method(request.getMethod())
        .payDate(now)
        .success(true)
        .build();

    booking.setPayment(payment);
    booking.setStatus(BookingStatus.CONFIRMED.getName());
    booking.setUpdatedDate(now);

    bookingRepository.save(booking);

    BookingResponse response = mapper.map(booking, BookingResponse.class);
//    response.setOutboundFlightNo(booking.getOutboundFlight().getNumber());
//    response.setReturnFlightNo(booking.getReturnFlight().getNumber());
    return response;
  }

  @Override
  public BookingResponse getBooking(String code) {
    Booking booking = bookingRepository.findByBookingCode(code);

    if (Objects.isNull(booking) || !BookingStatus.CONFIRMED.getName().equals(booking.getStatus())) {
      throw new AppExceptionV2(ErrorCodes.BOOKING_NOT_FOUND.getMessage(), ErrorCodes.BOOKING_NOT_FOUND);
    }
    BookingResponse response = mapper.map(booking, BookingResponse.class);
//    response.setOutboundFlightNo(booking.getOutboundFlight().getNumber());
//    response.setReturnFlightNo(booking.getReturnFlight().getNumber());
//    response.setOrigin(booking.getOutboundFlight().getOrigin() + " (" + booking.getOutboundFlight().getOriginCode() + ")");
//    response.setDestination(booking.getOutboundFlight().getDestination() + " (" + booking.getOutboundFlight().getArrivalCode() + ")");
    return response;
  }

  private void updateFlight(PendingBookingRequest pendingBookingRequest, Flight flight) {
    int totalPax = flight.getTotalPax() + pendingBookingRequest.getTotalAdultPax() + pendingBookingRequest.getTotalChildPax() + pendingBookingRequest.getTotalInfantPax();
    int totalSeats = getTotalSeats(pendingBookingRequest.getTotalAdultPax(), pendingBookingRequest.getTotalChildPax());

    String travelClass = pendingBookingRequest.getTravelClass().toLowerCase();
    Map<String, Integer> availability = flight.getAvailableSeats();
    if (availability.containsKey(travelClass)) {
      int newSeatsNumber = availability.get(travelClass) - totalSeats;
      availability.put(travelClass, newSeatsNumber);
    } else {
      throw new AppExceptionV2(ErrorCodes.BAD_REQUEST.getMessage(), ErrorCodes.BAD_REQUEST);
    }

    flight.setTotalPax(totalPax);
    flightRepository.save(flight);
  }

  private void saveBookingCode(String code, Date now) {
    BookingCode bookingCode = BookingCode.builder().code(code).build();
    bookingCode.setCreatedDate(now);
    bookingCode.setUpdatedDate(now);

    codeRepository.save(bookingCode);
  }

  private String generateBookingCode() {
    List<String> existingCodes =
        codeRepository.findAll().stream().map(bookingCode -> bookingCode.getCode())
            .collect(Collectors.toList());
    String registrationCode;
    do {
      registrationCode = UUID.randomUUID().toString().substring(0, 7).toUpperCase();
    } while (existingCodes.contains(registrationCode));
    return registrationCode;
  }
}
