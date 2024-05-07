package com.malex.model.dto;

import lombok.Builder;

@Builder
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
    boolean isActive) {}
