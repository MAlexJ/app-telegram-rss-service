package com.malex.controller;

import com.malex.service.TelegramPublisherService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/messages")
@RequiredArgsConstructor
public class MessageRestController {

  private final TelegramPublisherService service;

  public record MessageRequest(Long chatId, String topicId, String templateId) {}

  public record MessageResponse(Long messageId, LocalDateTime dateTime) {}

  @PostMapping
  public ResponseEntity<MessageResponse> send(@RequestBody MessageRequest request) {
    log.info("HTTP request - {}", request);
    return ResponseEntity.ok().build();
  }
}
