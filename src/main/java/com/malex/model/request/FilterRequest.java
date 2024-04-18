package com.malex.model.request;

import com.malex.model.filter.FilterCondition;

public record FilterRequest(boolean isActive, FilterCondition condition) {

  public FilterRequest(FilterCondition condition) {
    this(true, condition);
  }
}
