package com.malex.controller;

import com.malex.model.request.TemplateRequest;
import com.malex.model.request.UpdateMessageTemplateRequest;
import com.malex.model.response.TemplateResponse;
import com.malex.service.storage.TemplateStorageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/templates")
@RequiredArgsConstructor
public class TemplateRestController {

  private final TemplateStorageService service;

  @GetMapping
  public ResponseEntity<List<TemplateResponse>> findAllTemplates() {
    log.info("HTTP request find all templates");
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<TemplateResponse> findTemplateById(@PathVariable String id) {
    log.info("HTTP request, find by id - {}", id);
    return service
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @PostMapping
  public ResponseEntity<TemplateResponse> createTemplate(@RequestBody TemplateRequest request) {
    log.info("HTTP request, create template - {}", request);
    return service
        .save(request)
        .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
        .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
  }

  @PutMapping
  public ResponseEntity<Void> updateTemplate(@RequestBody UpdateMessageTemplateRequest request) {
    log.info("HTTP request, update template - {}", request);
    service.update(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTemplateById(@PathVariable String id) {
    log.info("HTTP request, delete template by id - {}", id);
    service.deleteBuId(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
