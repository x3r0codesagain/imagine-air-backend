package com.project.imagineair.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class BaseResponse {
  private String id;
  private Date createdDate;
  private Date updatedDate;
}
