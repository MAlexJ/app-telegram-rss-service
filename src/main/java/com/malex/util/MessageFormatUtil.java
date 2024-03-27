package com.malex.util;

import java.util.Optional;

public class MessageFormatUtil {

  private MessageFormatUtil() {
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
