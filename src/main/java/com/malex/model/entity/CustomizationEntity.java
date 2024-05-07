package com.malex.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.malex.model.customization.Image;
import com.malex.model.customization.Text;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "customizations")
@TypeAlias("CustomizationEntity")
public class CustomizationEntity implements Persistable<String> {

  @MongoId private String id;
  private String defaultImage;
  private Image image;
  private Text text;
  @CreatedDate private LocalDateTime created;

  @Override
  @JsonIgnore
  public boolean isNew() {
    return Objects.isNull(getCreated());
  }
}
