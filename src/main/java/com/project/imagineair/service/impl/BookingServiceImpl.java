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
import com.project.imagineair.model.request.CancelRequest;
import com.project.imagineair.model.request.CreateBookingRequest;
import com.project.imagineair.model.request.PaymentRequest;
import com.project.imagineair.model.request.PendingBookingRequest;
import com.project.imagineair.model.request.SeatAssignRequest;
import com.project.imagineair.model.response.BookingResponse;
import com.project.imagineair.repository.BookingCodeRepository;
import com.project.imagineair.repository.BookingRepository;
import com.project.imagineair.repository.FlightRepository;
import com.project.imagineair.repository.PendingBookingRepository;
import com.project.imagineair.service.BookingService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
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
        request.getPassengers().stream()
            .map(passenger -> mapper.map(passenger, Passenger.class))
            .collect(Collectors.toList());

    int index = 1;

    for (Passenger passenger : passengers) {
      passenger.setIndex(index);
      index++;
    }

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
    return response;
  }

  @Override
  public BookingResponse getBooking(String code) {
    Booking booking = bookingRepository.findByBookingCode(code);

    if (Objects.isNull(booking) || !BookingStatus.CONFIRMED.getName().equals(booking.getStatus())) {
      throw new AppExceptionV2(ErrorCodes.BOOKING_NOT_FOUND.getMessage(), ErrorCodes.BOOKING_NOT_FOUND);
    }
    BookingResponse response = mapper.map(booking, BookingResponse.class);
    return response;
  }

  @Override
  public BookingResponse findMyBooking(String code, String surname) {
    Booking booking = bookingRepository.findByBookingCode(code);

    if (Objects.isNull(booking) || !BookingStatus.CONFIRMED.getName().equals(booking.getStatus()) ||
        !booking.getBookingContact().getSurname().equals(surname)) {
      throw new AppExceptionV2(ErrorCodes.BOOKING_NOT_FOUND.getMessage(), ErrorCodes.BOOKING_NOT_FOUND);
    }
    BookingResponse response = mapper.map(booking, BookingResponse.class);
    return response;
  }

  @Override
  public BookingResponse getUnpaidBooking(String code) {
    Booking booking = bookingRepository.findByBookingCode(code);

    if (Objects.isNull(booking) || !BookingStatus.PENDING.getName().equals(booking.getStatus())) {
      throw new AppExceptionV2(ErrorCodes.BOOKING_NOT_FOUND.getMessage(), ErrorCodes.BOOKING_NOT_FOUND);
    }
    BookingResponse response = mapper.map(booking, BookingResponse.class);
    return response;
  }

  @Override
  public BookingResponse assignSeat(SeatAssignRequest request) {
    Booking booking = bookingRepository.findByBookingCode(request.getBookingCode());


    if (Objects.isNull(booking)) {
      throw new AppExceptionV2(ErrorCodes.BOOKING_NOT_FOUND.getMessage(), ErrorCodes.BOOKING_NOT_FOUND);
    }

    Flight flight = flightRepository.findByNumberAndDepartDate(request.getFlight(), request.getDepartDate());

    if (Objects.isNull(flight)) {
      throw new AppExceptionV2(ErrorCodes.FLIGHTS_NOT_FOUND.getMessage(), ErrorCodes.FLIGHTS_NOT_FOUND);
    }

    boolean outbound = false;
    if (booking.getOutboundFlight().getNumber().equals(flight.getNumber())) {
      outbound = true;
    }
    else if (booking.getReturnFlight().getNumber().equals(flight.getNumber())) {
      outbound = false;
    }

    Map<String, Boolean> availablility = flight.getSeatsAvailability();
    if (flight.getSeatsAvailability().containsKey(request.getSeat())) {
      if (!flight.getSeatsAvailability().get(request.getSeat())) {
        throw new AppExceptionV2(ErrorCodes.SEAT_UNAVAILABLE.getMessage(), ErrorCodes.SEAT_UNAVAILABLE);
      }
    }
    Passenger passenger = booking.getPassengers().stream()
        .filter(pax -> pax.getIndex() == request.getIndex()).findFirst().get();

    if (outbound && !"-".equals(passenger.getOutboundSeat())) {
      availablility.put(passenger.getOutboundSeat(), true);
    }
    else if (!outbound && !"-".equals(passenger.getReturnSeat())) {
      availablility.put(passenger.getReturnSeat(), true);
    }
    availablility.put(request.getSeat(), false);

    if (outbound) {
      booking.getPassengers().stream().filter(pax ->
          pax.getIndex() == request.getIndex()).findFirst().get().setOutboundSeat(request.getSeat());
    }
    else if (!outbound) {
      booking.getPassengers().stream().filter(pax ->
          pax.getIndex() == request.getIndex()).findFirst().get().setReturnSeat(request.getSeat());
    }

    flight.setSeatsAvailability(availablility);

    Date now = new Date();
    flight.setUpdatedDate(now);
    booking.setUpdatedDate(now);

    flightRepository.save(flight);
    bookingRepository.save(booking);

    BookingResponse response = mapper.map(booking, BookingResponse.class);

    return response;
  }

  @Override
  public BookingResponse cancelBooking(CancelRequest request) {
    Booking booking = bookingRepository.findByBookingCode(request.getBookingCode());
    Date now = new Date();


    if (Objects.isNull(booking) || BookingStatus.CANCELLED.getName().equals(booking.getStatus())) {
      throw new AppExceptionV2(ErrorCodes.BOOKING_NOT_FOUND.getMessage(),
          ErrorCodes.BOOKING_NOT_FOUND);
    }

    Flight outboundFlight =
        flightRepository.findByNumberAndDepartDate(booking.getOutboundFlight().getNumber(),
            booking.getOutboundFlight().getDepartDate());

    if (Objects.isNull(outboundFlight)) {
      throw new AppExceptionV2(ErrorCodes.FLIGHTS_NOT_FOUND.getMessage(),
          ErrorCodes.FLIGHTS_NOT_FOUND);
    }

    Map<String, Boolean> outboundFlightSeatsAvailability = outboundFlight.getSeatsAvailability();

    booking.setStatus(BookingStatus.CANCELLED.getName());

    booking.getPassengers().forEach(passenger -> {
      String currSeat = passenger.getOutboundSeat();

      if (!"-".equals(currSeat)) {
        outboundFlightSeatsAvailability.put(currSeat, true);
      }
    });

    outboundFlight.setSeatsAvailability(outboundFlightSeatsAvailability);

    Map<String, Integer> seatCount = outboundFlight.getAvailableSeats();
    int newSeats = seatCount.get(booking.getTravelClass().toLowerCase());
    newSeats = newSeats + booking.getTotalPax();
    seatCount.put(booking.getTravelClass().toLowerCase(), newSeats);
    outboundFlight.setAvailableSeats(seatCount);
    outboundFlight.setUpdatedDate(now);


    if (Objects.nonNull(booking.getReturnFlight())) {
      Flight returnFlight =
          flightRepository.findByNumberAndDepartDate(booking.getReturnFlight().getNumber(),
              booking.getReturnFlight().getDepartDate());

      if (Objects.isNull(returnFlight)) {
        throw new AppExceptionV2(ErrorCodes.FLIGHTS_NOT_FOUND.getMessage(),
            ErrorCodes.FLIGHTS_NOT_FOUND);
      }

      Map<String, Boolean> returnFlightSeatsAvailability = returnFlight.getSeatsAvailability();
      booking.getPassengers().forEach(passenger -> {
        String currSeat = passenger.getReturnSeat();

        if (!"-".equals(currSeat)) {
          returnFlightSeatsAvailability.put(currSeat, true);
        }
      });

      returnFlight.setSeatsAvailability(returnFlightSeatsAvailability);

      Map<String, Integer> returnSeats = returnFlight.getAvailableSeats();
      int seatsCount = returnSeats.get(booking.getTravelClass().toLowerCase());
      seatsCount = seatsCount + booking.getTotalPax();
      returnSeats.put(booking.getTravelClass().toLowerCase(), seatsCount);

      returnFlight.setAvailableSeats(returnSeats);
      returnFlight.setUpdatedDate(now);

      flightRepository.save(returnFlight);
    }

    booking.setUpdatedDate(now);


    bookingRepository.save(booking);
    flightRepository.save(outboundFlight);

    BookingResponse response = mapper.map(booking, BookingResponse.class);
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
