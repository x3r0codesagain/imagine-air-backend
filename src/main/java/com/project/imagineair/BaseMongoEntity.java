package com.project.imagineair;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseMongoEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @CreatedDate
  @Field(value = "created_date")
  private Date createdDate;

  @LastModifiedDate
  @Field(value = "updated_date")
  private Date updatedDate;

  @Version
  private Long version;
}
