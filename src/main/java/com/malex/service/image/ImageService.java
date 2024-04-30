package com.malex.service.image;

import com.malex.model.dto.ImageDto;
import com.malex.model.request.ImageRequest;
import com.malex.service.html.HtmlParserService;
import com.malex.service.storage.ImageStorageService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

  private final HtmlParserService parserService;
  private final ImageStorageService storageService;

  @Value("${default.topic.image.url}")
  private String defaultImageUrl;

  public Optional<String> findImageUrl(String url, String imageId) {
    return storageService
        .findById(imageId)
        .flatMap(imageDto -> parserService.findImageOrDefaultUrlByCriteria(url, imageDto));
  }

  public String findImageByIdOrProvideDefault(String imageId) {
    return Optional.ofNullable(imageId)
        .flatMap(storageService::findById)
        .map(ImageDto::link)
        .orElseGet(() -> defaultImageUrl);
  }

  public List<ImageDto> findAll() {
    return storageService.findAll();
  }

  public ImageDto save(ImageRequest request) {
    return storageService.save(request);
  }
}
