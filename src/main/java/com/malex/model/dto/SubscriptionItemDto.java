package com.malex.model.dto;

import static com.malex.utils.MessageFormatUtils.errorMessage;

import java.util.List;
import java.util.Objects;

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
    String customizationId,
    List<String> filterIds) {

  public SubscriptionItemDto {
    Objects.requireNonNull(md5Hash, errorMessage(md5Hash));
  }
}
