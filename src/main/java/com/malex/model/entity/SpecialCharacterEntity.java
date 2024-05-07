package com.malex.model.entity;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "special_characters")
@TypeAlias("SpecialCharacterEntity")
public class SpecialCharacterEntity {
  @MongoId private String id;

  private String symbol;
  private String replacement;

  @CreatedDate private LocalDateTime created;
}
