package com.project.imagineair.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightPriceRequest {
  private int economyPromo;
  private int economyStandard;
  private int economyFlex;
  private int businessPromo;
  private int businessStandard;
  private int businessFlex;
  private int first;
  private boolean ecoPromoSwitch;
  private boolean businessPromoSwitch;
}
