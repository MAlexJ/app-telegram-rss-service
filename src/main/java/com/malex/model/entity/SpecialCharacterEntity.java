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
@Document(collection = "special_characters")
@TypeAlias("SpecialCharacterEntity")
public class SpecialCharacterEntity implements Persistable<String> {
  @MongoId private String id;

  private String symbol;
  private String replacement;

  @CreatedDate private LocalDateTime created;

  /**
   * How Spring Data Jdbc determines that the object is new: <br>
   * Info: <a href="https://habr.com/ru/companies/otus/articles/526030/">Spring isNew()</a>
   */
  @Override
  @JsonIgnore
  public boolean isNew() {
    return Objects.isNull(getCreated());
  }
}
