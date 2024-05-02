package com.malex.model.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record RssItemDto(
    // item info
    String link,
    String title,
    String description,
    String md5Hash,
    // subscription info
    String subscriptionId,
    Long chatId,
    String templateId,
    String customizationId,
    List<String> filterIds) {}
