package com.malex.utils;

import java.util.Optional;

public class MessageFormatUtils {

  private MessageFormatUtils() {
    // not use
  }

  private static final String SHORT_MASSAGE_FORMAT = "%s ...";

  public static String shortMessage(String text) {
    return Optional.ofNullable(text)
        .filter(message -> message.length() > 60)
        .map(message -> String.format(SHORT_MASSAGE_FORMAT, message.substring(0, 50)))
        .orElse("");
  }
}
