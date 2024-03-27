package com.malex.model.response;

import com.malex.model.filter.RssFilter;
import java.time.LocalDateTime;

public record RssTopicResponse(
    String id,
    Long chatId,
    String subscriptionId,
    String rss,
    RssFilter filter,
    String title,
    String description,
    String link,
    String md5Hash,
    boolean isActive,
    LocalDateTime created) {}
