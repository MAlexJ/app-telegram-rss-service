package com.malex.model.entity;

import com.malex.model.filter.FilterCondition;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "filters")
@TypeAlias("FilterEntity")
public class FilterEntity {

  @MongoId private String id;

  @Field(name = "active")
  private boolean isActive;

  @NotNull private FilterCondition condition;
}
