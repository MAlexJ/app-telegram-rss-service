package com.malex.service;

import com.malex.service.html.HtmlParserService;
import com.malex.service.storage.ImageStorageService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

  private final HtmlParserService parserService;
  private final ImageStorageService storageService;

  public Optional<String> findImageUrl(String url, String imageId) {
    return storageService
        .findById(imageId)
        .flatMap(imageDto -> parserService.findImageOrDefaultUrlByCriteria(url, imageDto));
  }
}
