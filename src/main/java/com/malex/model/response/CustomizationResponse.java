package com.malex.model.response;

import com.malex.model.customization.Image;
import com.malex.model.customization.Text;
import java.time.LocalDateTime;

public record CustomizationResponse(
    String id, String defaultImage, Image image, Text text, LocalDateTime created) {}
