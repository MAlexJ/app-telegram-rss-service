package com.malex.controller;

import com.malex.model.dto.CustomizationDto;
import com.malex.model.request.CustomizationRequest;
import com.malex.service.CustomizationService;
import com.malex.service.storage.CustomizationStorageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/customizations")
@RequiredArgsConstructor
public class CustomizationController {

  private final CustomizationService service;
  private final CustomizationStorageService storageService;

  @GetMapping
  public ResponseEntity<List<CustomizationDto>> findAll() {
    var customizations = storageService.findAll();
    return ResponseEntity.ok(customizations);
  }

  @PostMapping
  public ResponseEntity<CustomizationDto> save(@RequestBody CustomizationRequest request) {
    return ResponseEntity.ok(storageService.save(request));
  }

  @PostMapping("/test")
  public ResponseEntity<String> findText(@RequestBody TestCustomization request) {
    return service
        .getFullInfo(request.url(), request.customizationId())
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  public record TestCustomization(String url, String customizationId) {}
}
