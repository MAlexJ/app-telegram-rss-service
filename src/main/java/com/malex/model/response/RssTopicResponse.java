package com.malex.model.response;

import java.time.LocalDateTime;
import java.util.List;

public record RssTopicResponse(
    String id,
    Long chatId,
    String subscriptionId,
    String rss,
    List<String> filterIds,
    String title,
    String description,
    String link,
    String md5Hash,
    boolean isActive,
    LocalDateTime created) {}
