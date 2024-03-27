package com.malex.model.filter;

public record RssFilterCondition(
    RssFilterValueType valueType,
    String value,
    RssFilterOperationType operationType,
    RssFilterCondition link) {

  public RssFilterCondition(RssFilterValueType valueType, String value) {
    this(valueType, value, null, null);
  }
}
