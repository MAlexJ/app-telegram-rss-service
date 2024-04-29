package com.malex.model.request;

import static com.malex.utils.MessageFormatUtils.errorMessage;

import java.util.List;
import java.util.Objects;

/** RSS subscription request */
public record RssSubscriptionRequest(
    Long chatId,
    String imageId,
    String templateId,
    String rss,
    List<String> filterIds,
    boolean isActive) {

  public RssSubscriptionRequest {
    Objects.requireNonNull(chatId, errorMessage("chatId"));
    Objects.requireNonNull(rss, errorMessage("rss"));
  }
}
