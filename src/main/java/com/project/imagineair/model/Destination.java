package com.project.imagineair.model;

import com.project.imagineair.BaseMongoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "destinations")
public class Destination extends BaseMongoEntity {
  private String name;
  private String IATACode;
  private int bookingCount;
  private int startsFrom;
  private String imageUrl;
}
