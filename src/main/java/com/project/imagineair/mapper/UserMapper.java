package com.project.imagineair.mapper;

import com.project.imagineair.model.User;
import com.project.imagineair.model.request.RegisterRequest;
import com.project.imagineair.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserResponse toUserResponse(User user);

  @Mapping(target = "password", ignore = true)
  User registerToUser(RegisterRequest registerRequest);
}
