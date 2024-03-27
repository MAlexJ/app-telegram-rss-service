package com.malex.model.request;

import com.malex.model.filter.RssFilter;
import java.util.Objects;

/**
 * text contains the word
 *
 * <p>the text has string occurrences
 */
public record RssSubscriptionRequest(
    Long chatId, String templateId, String rss, RssFilter filter, boolean isActive) {

  private static final String ERROR_MESSAGE_TEMPLATE = "'%s' is a mandatory parameter";

  public RssSubscriptionRequest {
    Objects.requireNonNull(chatId, errorMessage("chatId"));
    Objects.requireNonNull(templateId, errorMessage("templateId"));
    Objects.requireNonNull(rss, errorMessage("rss"));
  }

  private static String errorMessage(String parameter) {
    return String.format(ERROR_MESSAGE_TEMPLATE, parameter);
  }
}
