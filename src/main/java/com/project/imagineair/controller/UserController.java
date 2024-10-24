package com.project.imagineair.controller;

import com.project.imagineair.config.security.UserAuthProvider;
import com.project.imagineair.model.exceptions.AppException;
import com.project.imagineair.model.request.LoginRequest;
import com.project.imagineair.model.request.RegisterRequest;
import com.project.imagineair.model.response.UserResponse;
import com.project.imagineair.rest.response.RestSingleResponse;
import com.project.imagineair.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private UserAuthProvider userAuthProvider;


  @PostMapping("/login")
  public RestSingleResponse<UserResponse> login(@RequestBody LoginRequest loginRequest) {

    try {
      UserResponse userResponse = userService.login(loginRequest);

      userResponse.setToken(userAuthProvider.generateToken(userResponse.getEmail(),
          userResponse.getUserRole().name()));

      return new RestSingleResponse<>(null, null, true, userResponse);
    } catch (AppException appException) {
      return new RestSingleResponse<>(appException.getMessage(), appException.getCode().name(), false,
          null);
    } catch (Exception ex) {
      return new RestSingleResponse<>("Internal Server Error",
          HttpStatus.INTERNAL_SERVER_ERROR.name(), false, null);
    }
  }

  @PostMapping("/register")
  public RestSingleResponse<UserResponse> register(@RequestBody RegisterRequest request) {

    try {
      UserResponse userResponse = userService.register(request);
      userResponse.setToken(userAuthProvider.generateToken(userResponse.getEmail(), userResponse.getUserRole().name()));

      return new RestSingleResponse<>(null, null, true, userResponse);
    } catch (AppException appException) {
      return new RestSingleResponse<>(appException.getMessage(), appException.getCode().name(), false,
          null);
    } catch (Exception ex) {
      return new RestSingleResponse<>(ex.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.name(), false, null);
    }
  }

}
