package com.malex.model.dto;

import com.malex.model.entity.SubscriptionEntity;
import java.util.List;

public record RssTopicDto(
    String subscriptionId,
    Long chatId,
    String templateId,
    String rss,
    List<String> filterIds,
    String title,
    String description,
    String link,
    String md5Hash,
    Integer messageId,
    boolean isActive) {

  public RssTopicDto(SubscriptionEntity entity, RssItemDto item, String md5Hash) {
    this(
        entity.getId(),
        entity.getChatId(),
        entity.getTemplateId(),
        entity.getRss(),
        entity.getFilterIds(),
        item.title(),
        item.description(),
        item.link(),
        md5Hash,
        null,
        true);
  }
}
