package com.malex.exception.subscription;

import lombok.Getter;

@Getter
public class SubscriptionException extends RuntimeException {

  private final StatusCode statusCode;

  public SubscriptionException(StatusCode statusCode, String message) {
    super(message);
    this.statusCode = statusCode;
  }
}
