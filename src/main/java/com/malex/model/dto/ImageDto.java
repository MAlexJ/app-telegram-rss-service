package com.malex.model.dto;

import java.time.LocalDateTime;

public record ImageDto(
    String id, boolean isActive, String link, String image, LocalDateTime created) {

  public ImageDto(String link) {
    this(null, true, link, null, null);
  }
}
