package com.project.imagineair.model.request;

import lombok.Data;

@Data
public class BookingContactRequest {
  private String salutation;
  private String givenName;
  private String surname;
  private String phoneNo;
  private String email;
}
