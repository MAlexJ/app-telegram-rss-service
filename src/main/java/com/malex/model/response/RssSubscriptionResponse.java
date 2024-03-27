package com.malex.model.response;

import com.malex.model.filter.RssFilter;
import java.time.LocalDateTime;

public record RssSubscriptionResponse(
    String id,
    Long chatId,
    String templateId,
    String rss,
    RssFilter filter,
    boolean isActive,
    LocalDateTime lastModified,
    LocalDateTime created) {}
