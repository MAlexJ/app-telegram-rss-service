package com.malex.model.request;

import com.malex.model.customization.Image;
import com.malex.model.customization.Text;

public record CustomizationRequest(String defaultImage, Image image, Text text) {}
