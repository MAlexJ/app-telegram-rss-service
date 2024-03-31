package com.malex.model.response;

import org.telegram.telegrambots.meta.api.objects.Message;

public record MessageResponse(Integer messageId, Integer date) {

  public MessageResponse(Message message) {
    this(message.getMessageId(), message.getDate());
  }
}
