package com.malex.controller;

import com.malex.model.request.FilterRequest;
import com.malex.model.response.FilterResponse;
import com.malex.service.storage.FilterStorageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/filters")
@RequiredArgsConstructor
public class FilterRestController {

  private final FilterStorageService service;

  @GetMapping
  public ResponseEntity<List<FilterResponse>> findAll() {
    log.info("HTTP request find all filters");
    return ResponseEntity.ok(service.findAll());
  }

  @PostMapping
  public ResponseEntity<FilterResponse> save(@RequestBody FilterRequest request) {
    log.info("HTTP request - {}", request);
    return ResponseEntity.ok(service.save(request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable String id) {
    log.info("HTTP request, delete filter by id - {}", id);
    service.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
