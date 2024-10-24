package com.project.imagineair.model;

import com.project.imagineair.BaseMongoEntity;
import com.project.imagineair.model.dto.FareDTO;
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
@Document(collection = "pending_booking")
public class PendingBooking extends BaseMongoEntity {
  private String bookingCode;
  private int totalSeats;
  private int totalPax;
  private int totalPrice;
  private Fare outboundPrice;
  private Fare returnPrice;
  private String travelClass;
  @DocumentReference
  private Flight outboundFlight;
  @DocumentReference
  private Flight returnFlight;
}
