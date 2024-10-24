package com.project.imagineair.model.response;


import com.project.imagineair.model.BookingContact;
import com.project.imagineair.model.Passenger;
import com.project.imagineair.model.dto.BookingContactDTO;
import com.project.imagineair.model.dto.FareDTO;
import com.project.imagineair.model.dto.FlightDTO;
import com.project.imagineair.model.dto.PassengerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse extends BaseResponse {
  private String bookingCode;
//  private int totalAdultPax;
//  private int totalChildPax;
//  private int totalInfantPax;
//  private String outboundFlightNo;
//  private String returnFlightNo;
  private String travelClass;
  private FareDTO outboundPrice;
  private FareDTO returnPrice;
  private int totalPrice;
  private int totalPax;
//  private String origin;
//  private String destination;
  private FlightDTO outboundFlight;
  private FlightDTO returnFlight;
  private List<PassengerDTO> passengers;
  private BookingContactDTO bookingContact;
}
