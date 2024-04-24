package com.malex.model.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ImageDto(
    String id,
    boolean isActive,
    String defaultImage,
    String attributeClassName,
    List<String> additionalClassAttributes,
    LocalDateTime created) {}
