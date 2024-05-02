package com.malex.model.customization;

import com.malex.utils.MessageFormatUtils;
import java.util.List;
import java.util.Objects;

public record Text(String tag, List<String> exclusionaryPhrases) {

  public Text {
    Objects.requireNonNull(tag, MessageFormatUtils.errorMessage("tag"));
  }
}
