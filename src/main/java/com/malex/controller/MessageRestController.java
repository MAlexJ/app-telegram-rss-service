package com.malex.controller;

import com.malex.exception.TelegramPublisherException;
import com.malex.model.request.MessageRequest;
import com.malex.model.response.MessageResponse;
import com.malex.service.TelegramPublisherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@RestController
@RequestMapping("/v1/messages")
@RequiredArgsConstructor
public class MessageRestController {

  private final TelegramPublisherService service;

  /**
   * Send message to specified chat
   *
   * @param request contains chatId and message text
   * @return Message response with message id and sending date
   * @throws TelegramPublisherException exception
   */
  @PostMapping
  public ResponseEntity<MessageResponse> send(@RequestBody MessageRequest request) {
    log.info("HTTP request - {}", request);
    var chatId = request.chatId();
    var text = request.text();
    return service
        .postMessage(chatId, text)
        .map(this::buildResponse)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  private ResponseEntity<MessageResponse> buildResponse(Message message) {
    var response = new MessageResponse(message);
    log.info("Http response - {}", response);
    return ResponseEntity.ok(response);
  }
}
