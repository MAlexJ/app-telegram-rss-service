package com.malex.utils;

import java.util.Optional;

public class MessageFormatUtils {

  private static final String SHORT_MASSAGE_FORMAT = "%s ...";
  private static final String ERROR_MESSAGE_TEMPLATE = "'%s' is a mandatory parameter";

  private MessageFormatUtils() {
    // not use
  }

  public static String errorMessage(String parameter) {
    return String.format(ERROR_MESSAGE_TEMPLATE, parameter);
  }

  public static String shortMessage(String text) {
    return Optional.ofNullable(text)
        .filter(message -> message.length() > 60)
        .map(message -> String.format(SHORT_MASSAGE_FORMAT, message.substring(0, 50)))
        .orElse("");
  }
}
