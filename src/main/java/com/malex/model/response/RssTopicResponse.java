package com.malex.model.response;

import java.time.LocalDateTime;

public record RssTopicResponse(
    String id,
    Long chatId,
    String subscriptionId,
    String rss,
    String title,
    String description,
    String link,
    String image,
    String md5Hash,
    boolean isActive,
    LocalDateTime created) {}
