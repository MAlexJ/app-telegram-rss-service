package com.malex.model.request;

import java.time.LocalDateTime;

public record MessageTemplateRequest(
    String id, String description, String template, boolean isActive, LocalDateTime created) {}
