package com.project.imagineair.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingContact {
  private String salutation;
  private String givenName;
  private String surname;
  private String phoneNo;
  private String email;
}
