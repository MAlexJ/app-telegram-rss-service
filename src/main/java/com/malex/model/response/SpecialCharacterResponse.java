package com.malex.model.response;

import java.time.LocalDateTime;

public record SpecialCharacterResponse(
    String id, String symbol, String replacement, LocalDateTime created) {}
