package com.malex.model.response;

import com.malex.model.filter.FilterCondition;
import java.time.LocalDateTime;

public record FilterResponse(String id, FilterCondition condition, LocalDateTime created) {}
