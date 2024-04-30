package com.malex.model.response;

import java.time.LocalDateTime;

public record RssTopicResponse(
    String id,
    Long chatId,
    String subscriptionId,
    String image,
    String title,
    String description,
    String link,
    String md5Hash,
    boolean isActive,
    LocalDateTime created) {}
