package com.malex.model.type;

import java.util.Arrays;
import java.util.Objects;
import lombok.Getter;

/**
 * Link: <a href="https://core.telegram.org/bots/api#chat">chat info</a> <br>
 * Type of chat, can be either “private”, “group”, “supergroup” or “channel”
 */
@Getter
public enum ChatType {
  PRIVATE("private"),
  GROUP("group"),
  SUPERGROUP("supergroup"),
  CHANNEL("channel");

  private final String type;

  ChatType(String type) {
    this.type = type;
  }

  public static ChatType findType(String type) {
    Objects.requireNonNull(type);
    return Arrays.stream(values())
        .filter(v -> v.getType().equals(type))
        .findAny()
        .orElseThrow(() -> new IllegalStateException("Unexpected chat type value: " + type));
  }
}
