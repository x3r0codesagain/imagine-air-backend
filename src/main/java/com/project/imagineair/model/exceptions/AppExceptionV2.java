package com.project.imagineair.model.exceptions;

import com.project.imagineair.model.enums.ErrorCodes;
import lombok.Data;

@Data
public class AppExceptionV2 extends RuntimeException{

  private final ErrorCodes code;

  public AppExceptionV2(String message, ErrorCodes code) {
    super(message);
    this.code = code;
  }
}
