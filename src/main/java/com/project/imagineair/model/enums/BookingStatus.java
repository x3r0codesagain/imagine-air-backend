package com.project.imagineair.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookingStatus {
  PENDING("pending"),
  CONFIRMED("confirmed"),
  cancelled("cancelled");
  private String name;
}
