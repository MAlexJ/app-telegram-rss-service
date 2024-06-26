package com.malex.model.entity;

import com.malex.model.filter.FilterCondition;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "filters")
@TypeAlias("FilterEntity")
public class FilterEntity {

  @MongoId private String id;

  @NotNull private FilterCondition condition;

  @CreatedDate private LocalDateTime created;
}
