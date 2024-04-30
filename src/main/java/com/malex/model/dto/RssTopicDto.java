package com.malex.model.dto;

public record RssTopicDto(
    String subscriptionId,
    Long chatId,
    String templateId,
    String image,
    String title,
    String description,
    String link,
    String md5Hash,
    Integer messageId,
    boolean isActive) {

  public RssTopicDto(SubscriptionItemDto subscription, String image, String description) {
    this(
        subscription.subscriptionId(),
        subscription.chatId(),
        subscription.templateId(),
        image,
        subscription.title(),
        description,
        subscription.link(),
        subscription.md5Hash(),
        null,
        true);
  }

  public RssTopicDto(SubscriptionItemDto subscription, String image) {
    this(
        subscription.subscriptionId(),
        subscription.chatId(),
        subscription.templateId(),
        image,
        subscription.title(),
        subscription.description(),
        subscription.link(),
        subscription.md5Hash(),
        null,
        true);
  }
}
