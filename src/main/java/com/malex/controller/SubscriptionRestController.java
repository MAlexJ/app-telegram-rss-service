package com.malex.controller;

import com.malex.model.request.RssSubscriptionRequest;
import com.malex.model.response.RssSubscriptionResponse;
import com.malex.service.storage.SubscriptionStorageService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionRestController {

  private final SubscriptionStorageService service;

  @GetMapping
  public ResponseEntity<List<RssSubscriptionResponse>> findSubscriptions() {
    log.info("HTTP request find all subscription");
    return ResponseEntity.ok(service.findSubscriptions());
  }

  @PostMapping
  public ResponseEntity<RssSubscriptionResponse> subscribe(
      @RequestBody RssSubscriptionRequest request) {
    log.info("HTTP request - {}", request);
    return ResponseEntity.status(HttpStatus.CREATED).body(service.subscribe(request));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<RssSubscriptionResponse> unsubscribe(@PathVariable String id) {
    log.info("HTTP request, unsubscribe by id - {}", id);
    return buildResponse(id, service.unsubscribe(id));
  }

  private ResponseEntity<RssSubscriptionResponse> buildResponse(
      String id, Integer numberOfTopicsUpdated) {
    if (Objects.isNull(numberOfTopicsUpdated)) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return switch (numberOfTopicsUpdated) {
      case 0 -> {
        log.warn("No record was found by id - {}", id);
        yield ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }
      case 1 -> {
        log.info("Unsubscribe topic by id '{}' form RSS", id);
        yield ResponseEntity.status(HttpStatus.NO_CONTENT).build();
      }
      default -> {
        log.warn("Updated more than one subscriptions - {} by id - {}", numberOfTopicsUpdated, id);
        yield ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      }
    };
  }
}
