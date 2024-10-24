package com.project.imagineair.model.response;

import com.project.imagineair.BaseMongoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DestinationResponse extends BaseResponse {
  private String name;
  private String IATACode;
  private int bookingCount;
  private int startsFrom;
}
