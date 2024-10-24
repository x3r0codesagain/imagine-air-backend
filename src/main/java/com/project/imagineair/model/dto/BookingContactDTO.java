package com.project.imagineair.model.dto;

import lombok.Data;

@Data
public class BookingContactDTO {
  private String salutation;
  private String givenName;
  private String surname;
  private String phoneNo;
  private String email;
}
