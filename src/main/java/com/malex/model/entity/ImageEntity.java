package com.malex.model.entity;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "images")
@TypeAlias("ImageEntity")
public class ImageEntity {

  @MongoId private String id;

  @Field(name = "active")
  private boolean isActive;

  /** http link to image */
  private String link;

  /** image in base 64 format */
  private String image;

  @CreatedDate private LocalDateTime created;
}
