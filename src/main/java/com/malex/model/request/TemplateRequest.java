package com.malex.model.request;

import java.time.LocalDateTime;

public record TemplateRequest(
    String id, String description, String template, boolean isActive, LocalDateTime created) {}
