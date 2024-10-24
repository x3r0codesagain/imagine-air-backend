package com.project.imagineair.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

  private String firstName;
  private String lastName;
  private String email;
  private char[] password;
  private String gender;
  private String phoneNo;
  private boolean contributor = false;
}
