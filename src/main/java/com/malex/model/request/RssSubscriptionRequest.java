package com.malex.model.request;

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

  private static final String ERROR_MESSAGE_TEMPLATE = "'%s' is a mandatory parameter";

  public RssSubscriptionRequest {
    Objects.requireNonNull(chatId, errorMessage("chatId"));
    Objects.requireNonNull(rss, errorMessage("rss"));
  }

  private static String errorMessage(String parameter) {
    return String.format(ERROR_MESSAGE_TEMPLATE, parameter);
  }
}
