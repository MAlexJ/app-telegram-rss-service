package com.malex.model.dto;

import com.malex.model.entity.SubscriptionEntity;

public record RssTopicDto(
    String subscriptionId,
    Long chatId,
    String templateId,
    String image,
    String rss,
    String title,
    String description,
    String link,
    String md5Hash,
    Integer messageId,
    boolean isActive) {

  public RssTopicDto(
      SubscriptionEntity subscription,
      RssItemDto item,
      String image,
      String description,
      String md5Hash) {
    this(
        subscription.getId(),
        subscription.getChatId(),
        subscription.getTemplateId(),
        image,
        subscription.getRss(),
        item.title(),
        description,
        item.link(),
        md5Hash,
        null,
        true);
  }
}
