package com.malex.controller;

import com.malex.model.request.CustomizationRequest;
import com.malex.model.response.CustomizationResponse;
import com.malex.service.customisation.CustomisationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/customizations")
@RequiredArgsConstructor
public class CustomizationController {

  private final CustomisationService service;

  @GetMapping
  public ResponseEntity<List<CustomizationResponse>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @PostMapping
  public ResponseEntity<CustomizationResponse> create(@RequestBody CustomizationRequest request) {
    CustomizationResponse response = service.save(request);
    return ResponseEntity.ok(response);
  }
}
