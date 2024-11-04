package com.project.imagineair.model.request;

import lombok.Data;

@Data
public class CancelRequest {
  private String bookingCode;
  private boolean cancelAll;
  private String surname;
  private String givenName;
  private String paxIndex;
}
