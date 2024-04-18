package com.malex.model.filter;

import com.malex.model.entity.FilterEntity;

import java.util.List;

public record FilterCondition(ConditionType type,  List<String> keyWords) {}
