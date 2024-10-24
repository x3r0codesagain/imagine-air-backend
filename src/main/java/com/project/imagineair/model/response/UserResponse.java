package com.project.imagineair.model.response;

import com.project.imagineair.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse extends BaseResponse{

  private String firstName;
  private String lastName;
  private String email;
  private UserRole userRole;
  private String gender;
  private String token;
  private String phoneNo;
  private String imageUrl;
}
