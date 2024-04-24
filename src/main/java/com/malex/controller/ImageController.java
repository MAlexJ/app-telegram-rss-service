package com.malex.controller;

import com.malex.model.dto.ImageDto;
import com.malex.model.request.ImageRequest;
import com.malex.service.ImageService;
import com.malex.service.storage.ImageStorageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/images")
@RequiredArgsConstructor
public class ImageController {

  private final ImageService service;
  private final ImageStorageService storageService;

  @GetMapping
  public ResponseEntity<List<ImageDto>> findAll() {
    var customizations = storageService.findAll();
    return ResponseEntity.ok(customizations);
  }

  @PostMapping
  public ResponseEntity<ImageDto> save(@RequestBody ImageRequest request) {
    return ResponseEntity.ok(storageService.save(request));
  }

  @PostMapping("/test")
  public ResponseEntity<String> findImageUrl(@RequestBody ImageSearchRequest request) {
    return service
        .findImageUrl(request.url(), request.imageId())
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  public record ImageSearchRequest(String url, String imageId) {}
}
