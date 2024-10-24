package com.project.imagineair.service;

import com.project.imagineair.model.request.LoginRequest;
import com.project.imagineair.model.request.RegisterRequest;
import com.project.imagineair.model.response.UserResponse;

public interface UserService {
  UserResponse findByEmail(String email);
  UserResponse register(RegisterRequest registerRequest);
  UserResponse login(LoginRequest loginRequest);
}
