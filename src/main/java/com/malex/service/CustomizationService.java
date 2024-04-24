package com.malex.service;

import com.malex.service.html.HtmlParserService;
import com.malex.service.storage.CustomizationStorageService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomizationService {

  private final HtmlParserService parserService;
  private final CustomizationStorageService storageService;

  public Optional<String> getFullInfo(String url, String customizationId) {
    var customizationOpt = storageService.findById(customizationId);
    if (customizationOpt.isEmpty()) {
      return Optional.empty();
    }
    return parserService.applyCustomization(url, customizationOpt.get());
  }
}
