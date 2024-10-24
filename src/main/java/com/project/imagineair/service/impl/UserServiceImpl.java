package com.project.imagineair.service.impl;

import com.project.imagineair.mapper.UserMapper;
import com.project.imagineair.model.User;
import com.project.imagineair.model.enums.ErrorCodes;
import com.project.imagineair.model.enums.UserRole;
import com.project.imagineair.model.exceptions.AppException;
import com.project.imagineair.model.request.LoginRequest;
import com.project.imagineair.model.request.RegisterRequest;
import com.project.imagineair.model.response.UserResponse;
import com.project.imagineair.repository.UserRepository;
import com.project.imagineair.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder; //to avoid password saved in plain text - hashed
  private final UserMapper userMapper;
  private final Mapper mapper;


  @Override
  public UserResponse findByEmail(String email) {
    User user = userRepository.findByEmail(email).orElseThrow(()
        -> new AppException(ErrorCodes.USER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND));
    return userMapper.toUserResponse(user);
  }

  @Override
  public UserResponse login(LoginRequest loginRequest) {
    User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()
        -> new AppException(ErrorCodes.USER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND));


    if(passwordEncoder.matches(
        CharBuffer.wrap(loginRequest.getPassword()), user.getPassword())){
      UserResponse userResponse = userMapper.toUserResponse(user);
      return userResponse;
    }

    throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
  }

  @Override
  public UserResponse register(RegisterRequest registerRequest) {
    User userFromDB = userRepository.findByEmail(registerRequest.getEmail())
        .orElseGet(() -> null);

    if(Objects.nonNull(userFromDB)) {
      throw new AppException("Account Exists", HttpStatus.BAD_REQUEST);
    }

    if (StringUtils.isBlank(registerRequest.getGender())){
      throw new AppException("Please select the appropriate gender", HttpStatus.BAD_REQUEST);
    }

    User user = userMapper.registerToUser(registerRequest);
    setAdditionalDataToUser(user, UserRole.ROLE_USER, registerRequest.getPhoneNo());


    user.setPassword(passwordEncoder.encode(CharBuffer.wrap(registerRequest.getPassword()))); //Store in hashed

    userRepository.save(user);

    UserResponse userResponse = mapper.map(user, UserResponse.class);
    userResponse.setPhoneNo(registerRequest.getPhoneNo());
    return userResponse;
  }

  private void setAdditionalDataToUser(User user, UserRole roleUser, String phoneNo) {
    user.setUserRole(roleUser);

    Date now = new Date();
    user.setCreatedDate(now);
    user.setUpdatedDate(now);
  }
}
