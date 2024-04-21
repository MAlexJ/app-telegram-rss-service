package com.malex.controller;

import com.malex.model.dto.CustomizationDto;
import com.malex.model.request.CustomizationRequest;
import com.malex.service.storage.CustomizationStorageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/customizations")
@RequiredArgsConstructor
public class CustomizationController {

  private final CustomizationStorageService service;

  @GetMapping
  public ResponseEntity<List<CustomizationDto>> findAll() {
    var customizations = service.findAll();
    return ResponseEntity.ok(customizations);
  }

  @PostMapping
  public ResponseEntity<CustomizationDto> save(@RequestBody CustomizationRequest request) {
    return ResponseEntity.ok(service.save(request));
  }
}
