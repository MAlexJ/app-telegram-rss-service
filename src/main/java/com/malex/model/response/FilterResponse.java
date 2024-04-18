package com.malex.model.response;

import com.malex.model.filter.FilterCondition;

public record FilterResponse(String id, boolean isActive, FilterCondition condition) {}
