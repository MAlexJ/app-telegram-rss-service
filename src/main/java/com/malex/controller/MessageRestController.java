package com.malex.controller;

import com.malex.exception.TelegramPublisherException;
import com.malex.model.request.MessageRequest;
import com.malex.model.response.MessageResponse;
import com.malex.webservice.TelegramPublisherWebService;
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

  private final TelegramPublisherWebService service;

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
    var text = request.text();
    var image = request.image();
    var chatId = request.chatId();
    return buildResponse(service.sendMessage(chatId, image, text));
  }

  private ResponseEntity<MessageResponse> buildResponse(Integer messageId) {
    var response = new MessageResponse(messageId);
    log.info("Http response - {}", response);
    return ResponseEntity.ok(response);
  }
}
