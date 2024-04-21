package com.malex.model.dto;

import java.time.LocalDateTime;

public record CustomizationDto(
    String id,
    boolean isActive,
    String imageClass,
    String titleClass,
    String descriptionClass,
    LocalDateTime created) {}
