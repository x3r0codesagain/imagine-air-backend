package com.project.imagineair.model;

import com.project.imagineair.BaseMongoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCode extends BaseMongoEntity {
  private String code;
}
