package com.project.imagineair.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableSeat {
  private int economy;
  private int business;
  private int first;
}
