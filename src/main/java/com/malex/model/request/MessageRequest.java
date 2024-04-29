package com.malex.model.request;

import static com.malex.utils.MessageFormatUtils.errorMessage;

import java.util.Objects;

public record MessageRequest(Long chatId, String image, String text) {

  public MessageRequest {
    Objects.requireNonNull(chatId, errorMessage("chatId"));
    Objects.requireNonNull(text, errorMessage("text"));
  }
}
