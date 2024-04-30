package com.malex.model.response;

import java.time.LocalDateTime;
import java.util.List;

public record RssSubscriptionResponse(
    String id,
    Long chatId,
    String imageId,
    String templateId,
    String customizationId,
    String rss,
    List<String> filterIds,
    boolean isActive,
    LocalDateTime lastModified,
    LocalDateTime created) {}
