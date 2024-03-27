package com.malex.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * 1. @TypeAlias("user") annotation - based type aliases to be used when writing type information
 * for mongo db tutorial: <a href="https://www.youtube.com/watch?v=TZRJlfQZ-Jc">@TypeAlias
 * annotation</a>
 */
@Data
@Document(collection = "users")
@TypeAlias("UserEntity")
public class UserEntity implements Persistable<String> {

  @MongoId private String id;

  @Indexed(unique = true)
  private Long userId;

  private String firstName;
  private String lastName;
  private String username;

  @CreatedDate private LocalDateTime created;

  /**
   * How Spring Data Jdbc determines that the object is new: <br>
   * Info: <a href="https://habr.com/ru/companies/otus/articles/526030/">Spring isNew()</a>
   */
  @Override
  @JsonIgnore
  public boolean isNew() {
    return getCreated() == null;
  }
}
