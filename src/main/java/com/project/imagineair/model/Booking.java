package com.project.imagineair.model;

import com.project.imagineair.BaseMongoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "flight_booking")
public class Booking extends BaseMongoEntity {
  private String bookingCode;
  private int totalSeats;
  private int totalPax;
  private int totalPrice;
  private List<Passenger> passengers;
  private BookingContact bookingContact;
  private String status;
  private String travelClass;
  @DocumentReference
  private Flight outboundFlight;
  @DocumentReference
  private Flight returnFlight;
  private Payment payment;
}
