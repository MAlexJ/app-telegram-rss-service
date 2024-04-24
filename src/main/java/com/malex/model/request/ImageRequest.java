package com.malex.model.request;

import java.util.List;

public record ImageRequest(
    boolean isActive,
    String defaultImage,
    String attributeClassName,
    List<String> additionalClassAttributes) {}
