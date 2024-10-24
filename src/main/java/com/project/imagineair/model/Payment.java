package com.project.imagineair.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
  private String method;
  private String cardNo;
  private Date payDate;
  private boolean success;
}
