package com.malex.model.entity;

import lombok.Data;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "customizations")
@TypeAlias("CustomizationEntity")
public class CustomizationEntity {

  @MongoId private String id;

  @Field(name = "active")
  private boolean isActive;

  /** Html classes tags for title, description and image */
  private String imageClass;

  private String titleClass;
  private String descriptionClass;
}
