package com.malex.exception.telegram;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class TelegramPublisherException extends RuntimeException {

  public TelegramPublisherException(Throwable cause) {
    super(cause);
  }
}
