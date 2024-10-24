package com.project.imagineair.config.security;

import com.project.imagineair.model.dto.ErrorInfoDTO;
import com.project.imagineair.model.exceptions.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(value = {AppException.class})
  @ResponseBody
  public ResponseEntity<ErrorInfoDTO> handleException(AppException appException) {
    return ResponseEntity.status(appException.getCode()).body(ErrorInfoDTO.builder()
        .errorCode(appException.getMessage())
        .errorMessage(appException.getMessage()).build());
  }
}
