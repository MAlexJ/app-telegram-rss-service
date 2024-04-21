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
    String image,
    String md5Hash,
    Integer messageId,
    boolean isActive) {

  public RssTopicDto(SubscriptionEntity subscription, RssItemDto item, String md5Hash) {
    this(
        subscription.getId(),
        subscription.getChatId(),
        subscription.getTemplateId(),
        subscription.getRss(),
        item.title(),
        item.description(),
        item.link(),
        subscription.getImage(),
        md5Hash,
        null,
        true);
  }
}
