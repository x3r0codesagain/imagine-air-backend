package com.project.imagineair.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class PaymentRequest {
  private String bookingCode;
  private String method;
  private String cardNo;
}
