package com.project.imagineair.rest.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseRestResponse implements Serializable {
  private static final long serialVersionUID = 1414161646635029173L;
  private String errorMessage;
  private String errorCode;
  private boolean success;
}
