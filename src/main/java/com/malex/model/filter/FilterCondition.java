package com.malex.model.filter;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record FilterCondition(@NotNull ConditionType type, @NotNull List<String> keyWords) {}
