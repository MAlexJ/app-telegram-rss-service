package com.malex.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Todo: test controller */
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class TelegramPublisherException extends RuntimeException {

  public TelegramPublisherException(Throwable cause) {
    super(cause);
  }
}
