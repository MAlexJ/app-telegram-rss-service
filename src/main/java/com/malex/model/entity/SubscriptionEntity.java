package com.malex.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "subscriptions")
@TypeAlias("SubscriptionEntity")
public class SubscriptionEntity implements Persistable<String> {

  @MongoId private String id;
  private Long chatId;
  private String templateId;
  private String customizationId;
  private String rss;

  /** default image for rss topic message */
  private String image;

  private List<String> filterIds;
  private boolean isActive;

  @LastModifiedDate private LocalDateTime lastModified;
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
