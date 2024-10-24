package com.project.imagineair.model.dto;

import com.project.imagineair.BaseMongoEntity;
import com.project.imagineair.model.FlightPrice;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightDTO {
  private String number;
  private String origin;
  private String destination;
  private String originCode;
  private String arrivalCode;
  private String departDate;
  private String arriveDate;
  private String departLocalTime;
  private String arriveLocalTime;
  private int flightHours;
  private int flightMinutes;
  private int totalPax;
}
