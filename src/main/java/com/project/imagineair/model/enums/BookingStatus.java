package com.project.imagineair.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookingStatus {
  PENDING("pending"),
  CONFIRMED("confirmed"),
  CANCELLED("cancelled");
  private String name;
}
