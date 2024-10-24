package com.project.imagineair.rest.response;

import com.project.imagineair.model.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestListResponse<T extends BaseResponse> extends BaseRestResponse{
  private static final long serialVersionUID = 8821236660403254370L;

  private List<T> value;

  public RestListResponse(String errorMessage, String errorCode, boolean success, List<T> value) {
    super(errorMessage, errorCode, success);
    this.value = value;
  }
}
