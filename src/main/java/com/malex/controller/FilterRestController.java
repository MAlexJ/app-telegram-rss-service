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
    List<FilterResponse> filters = service.findAllFilters();
    return ResponseEntity.ok(filters);
  }

  @PostMapping
  public ResponseEntity<FilterResponse> save(@RequestBody FilterRequest request) {
    var response = service.save(request);
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Void> disableFilter(@PathVariable String id) {
    log.info("HTTP request, unsubscribe by id - {}", id);
    return buildResponse(id, service.disableFilterByID(id));
  }

  private ResponseEntity<Void> buildResponse(String id, Integer updatedRecords) {
    return switch (updatedRecords) {
      case 0 -> {
        log.warn("No record was found by id - {}", id);
        yield ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }
      case 1 -> {
        log.info("Filter inactive by id - '{}' form RSS", id);
        yield ResponseEntity.status(HttpStatus.NO_CONTENT).build();
      }
      default -> {
        log.warn("Updated more than one records - '{}' by id - '{}'", updatedRecords, id);
        yield ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      }
    };
  }
}
