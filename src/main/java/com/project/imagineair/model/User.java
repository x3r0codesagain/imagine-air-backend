package com.project.imagineair.model;

import com.project.imagineair.BaseMongoEntity;
import com.project.imagineair.model.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User extends BaseMongoEntity {

  @NotNull
  @Field(value = "first_name")
  private String firstName;

  @NotNull
  @Field(value = "last_name")
  private String lastName;

  @NotNull
  private String email;

  @NotNull
  private String password;

  @NotNull
  private UserRole userRole;

  @NotNull
  private String gender;

  @NotNull
  private String phoneNo;

  private String imageUrl;
}
