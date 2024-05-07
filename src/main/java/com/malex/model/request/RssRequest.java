package com.malex.model.request;

import static com.malex.utils.MessageFormatUtils.errorMessage;

import java.util.Objects;

public record RssRequest(String url) {

  public RssRequest {
    Objects.requireNonNull(url, errorMessage("chatId"));
  }
}
