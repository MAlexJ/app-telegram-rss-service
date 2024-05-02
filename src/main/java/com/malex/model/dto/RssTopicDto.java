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

  public RssTopicDto(RssItemDto rssItem, String image, String description) {
    this(
        rssItem.subscriptionId(),
        rssItem.chatId(),
        rssItem.templateId(),
        image,
        rssItem.title(),
        description,
        rssItem.link(),
        rssItem.md5Hash(),
        null,
        true);
  }

  public RssTopicDto(RssItemDto rssItem) {
    this(
        rssItem.subscriptionId(),
        rssItem.chatId(),
        rssItem.templateId(),
        null,
        rssItem.title(),
        rssItem.description(),
        rssItem.link(),
        rssItem.md5Hash(),
        null,
        true);
  }
}
