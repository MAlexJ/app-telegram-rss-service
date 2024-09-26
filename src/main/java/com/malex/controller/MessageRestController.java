package com.malex.controller;

import com.malex.exception.telegram.TelegramPublisherException;
import com.malex.model.request.MessageRequest;
import com.malex.model.response.MessageResponse;
import com.malex.service.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@RestController
@RequestMapping("/v1/messages")
@RequiredArgsConstructor
public class MessageRestController {

  private final TelegramService telegramService;

  /**
   * Send message to specified chat
   *
   * @param request contains chatId and message text
   * @return Message response with message id and sending date
   */
  @PostMapping
  public ResponseEntity<MessageResponse> send(@RequestBody MessageRequest request) {
    log.info("HTTP request, message - {}", request);
    var response = telegramService.sendMessage(request);
    log.info("HTTP response, message - {}", response);
    return ResponseEntity.ok(response);
  }

  @ExceptionHandler({TelegramPublisherException.class, TelegramApiException.class})
  public ResponseEntity<Object> handleTelegramPublisherException(Exception exception) {
    var errorMessage = exception.getMessage();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
  }
}
