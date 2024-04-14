package com.malex.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "errors")
@TypeAlias("ErrorEntity")
public class ErrorEntity implements Persistable<String> {

  @MongoId private String id;

  private String errorMessage;

  private String details;

  @CreatedDate private LocalDateTime created;

  @Override
  @JsonIgnore
  public boolean isNew() {
    return Objects.isNull(getCreated());
  }
}
