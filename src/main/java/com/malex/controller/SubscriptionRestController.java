package com.malex.controller;

import com.malex.exception.subscription.StatusCode;
import com.malex.exception.subscription.SubscriptionException;
import com.malex.model.request.SubscriptionRequest;
import com.malex.model.response.SubscriptionResponse;
import com.malex.service.cache.SubscriptionCacheService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionRestController {

  private final SubscriptionCacheService subscriptionCacheService;

  @GetMapping
  public ResponseEntity<List<SubscriptionResponse>> findSubscriptions() {
    log.info("HTTP request find all subscription");
    return ResponseEntity.ok(subscriptionCacheService.findSubscriptionsCacheable());
  }

  @PostMapping
  public ResponseEntity<SubscriptionResponse> subscribe(@RequestBody SubscriptionRequest request) {
    log.info("HTTP request, subscribe to rss - {}", request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(subscriptionCacheService.subscribeAndCacheEvict(request));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<SubscriptionResponse> unsubscribe(@PathVariable String id) {
    log.info("HTTP request, unsubscribe by id - {}", id);
    subscriptionCacheService.unsubscribeFromSubscriptionAndEvictCache(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /*
   * Apply Controller-Level @ExceptionHandler
   *
   * link: https://www.baeldung.com/exception-handling-for-rest-with-spring#exceptionhandler
   */
  @ExceptionHandler({SubscriptionException.class})
  public ResponseEntity<Object> handleException(SubscriptionException ex, WebRequest request) {
    var statusCode = ex.getStatusCode();
    var errorMessage = ex.getMessage();
    log.error(errorMessage, ex);
    if (statusCode == StatusCode.MULTIPLE_SUBSCRIPTIONS) {
      throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST, errorMessage);
//      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
    throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, errorMessage);
//    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
  }
}
