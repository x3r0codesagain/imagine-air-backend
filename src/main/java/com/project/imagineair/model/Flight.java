package com.project.imagineair.model;

import com.project.imagineair.BaseMongoEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "flights")
public class Flight extends BaseMongoEntity {
  @NotBlank(message = "Flight should not be blank")
  private String number;
  @NotBlank(message = "Origin should not be blank")
  private String origin;
  @NotBlank(message = "Destination should not be blank")
  private String destination;
  @NotBlank(message = "Origin code should not be blank")
  private String originCode;
  @NotBlank(message = "Arrival code should not be blank")
  private String arrivalCode;
  @NotNull(message = "Departure date should not be null")
  private String departDate;
  @NotNull(message = "Arrival date should not be null")
  private String arriveDate;
  @NotBlank(message = "Departure time should not be blank")
  private String departLocalTime;
  @NotBlank(message = "Arrival time should not be blank")
  private String arriveLocalTime;
  private int flightHours;
  private int flightMinutes;
  private Map<String, Integer> availableSeats;
  private boolean overnight;
  private FlightPrice flightPrice;
  private int totalPax;
  private Map<String, Boolean> seatsAvailability = new HashMap<>();
}
