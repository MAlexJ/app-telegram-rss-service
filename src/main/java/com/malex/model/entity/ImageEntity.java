package com.malex.model.entity;

import java.time.LocalDateTime;
import java.util.List;
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

  /** default image for rss news */
  private String defaultImage;

  private String attributeClassName;
  // Todo: additional attributes for searching
  private List<String> additionalClassAttributes;

  @CreatedDate private LocalDateTime created;
}
