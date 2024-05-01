package com.malex.model.dto;

import lombok.Builder;

@Builder
public record RssItemDto(String title, String description, String link) {}
