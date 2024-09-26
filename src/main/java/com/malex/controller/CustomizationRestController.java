package com.malex.controller;

import com.malex.model.request.CustomizationRequest;
import com.malex.model.response.CustomizationResponse;
import com.malex.service.customisation.CustomisationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/customizations")
@RequiredArgsConstructor
public class CustomizationRestController {

  private final CustomisationService service;

  @GetMapping
  public ResponseEntity<Page<CustomizationResponse>> findAll(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    log.info("HTTP request find all customization, page - {}, size - {}", page, size);
    return ResponseEntity.ok(service.findAll(page, size));
  }

  @PostMapping
  public ResponseEntity<CustomizationResponse> create(@RequestBody CustomizationRequest request) {
    log.info("HTTP request create customization - {}", request);
    var response = service.save(request);
    return ResponseEntity.ok(response);
  }
}
