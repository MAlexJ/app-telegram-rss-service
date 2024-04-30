package com.malex.model.dto;

import java.util.List;

public record SubscriptionItemDto(
    // item info
    String title,
    String description,
    String link,
    String md5Hash,
    // subscription info
    String subscriptionId,
    Long chatId,
    String templateId,
    String imageId,
    List<String> filterIds) {}
