package com.project.imagineair.model.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TravelClass {
  Y("Economy Class"),
  J("BusinessClass"),
  F("Economy Class");
  private String name;
}
