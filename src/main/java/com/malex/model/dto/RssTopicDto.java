package com.malex.model.dto;

import com.malex.model.entity.SubscriptionEntity;

public record RssTopicDto(
    String subscriptionId,
    Long chatId,
    String templateId,
    String rss,
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
        item.title(),
        item.description(),
        item.link(),
        md5Hash,
        null,
        true);
  }
}
