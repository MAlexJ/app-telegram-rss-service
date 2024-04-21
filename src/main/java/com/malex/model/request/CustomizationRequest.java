package com.malex.model.request;

public record CustomizationRequest(
    boolean isActive, String imageClass, String titleClass, String descriptionClass) {}
