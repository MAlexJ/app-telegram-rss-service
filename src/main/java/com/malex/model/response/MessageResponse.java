package com.malex.model.response;


import java.time.LocalDateTime;

public record MessageResponse(Integer messageId, LocalDateTime date) {

  public MessageResponse(Integer messageId) {
    this(messageId, LocalDateTime.now());
  }
}
