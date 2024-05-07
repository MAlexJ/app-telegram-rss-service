package com.malex.model.customization;

import com.malex.utils.MessageFormatUtils;
import java.util.Objects;

public record Image(String tag, String attribute) {

  public Image {
    Objects.requireNonNull(tag, MessageFormatUtils.errorMessage("tag"));
    Objects.requireNonNull(tag, MessageFormatUtils.errorMessage("attribute"));
  }
}
