package com.project.imagineair.model;

import com.project.imagineair.model.enums.IDType;
import lombok.Data;

@Data
public class Passenger {
  private int index;
  private String salutation;
  private String givenName;
  private String surname;
  private String birthDate;
  private String gender;
  private String idNumber;
  private String idType;
  private String outboundSeat;
  private String returnSeat;
  private String phoneNo;
  private String email;
}
