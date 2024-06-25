package com.malex.service;

import com.malex.exception.TelegramPublisherException;
import com.malex.exception.TemplateResolverException;
import com.malex.mapper.JsonMapper;
import com.malex.model.entity.RssTopicEntity;
import com.malex.service.storage.ErrorStorageService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ErrorService {

  private final JsonMapper jsonMapper;
  private final ErrorStorageService errorStorageService;

  public Optional<String> handleException(RssTopicEntity topic, Runnable action) {
    try {
      action.run();
      return Optional.empty();
    } catch (TemplateResolverException | TelegramPublisherException ex) {
      var errorMessage = ex.getMessage();
      log.error("Error processing RSS topic, error message- {}", errorMessage);
      /*
       * Telegram API
       * 1. Telegram API will not allow more than 30 messages per second or so
       * 2. Also note that your bot will not be able to send more than 20 messages per minute to the same group.
       */
      if (errorMessage.contains("retry after")) {
        errorStorageService.saveError(
            String.format(
                "Error - %s , text length - %s", errorMessage, topic.getDescription().length()),
            jsonMapper.writeValueAsString(topic));
      }

      /*
       * Handle cases and truncate message ~500, ~700 characters
       */
      if (errorMessage.contains("message caption is too long")) {
        errorStorageService.saveError(
            String.format(
                "Error - %s , text length - %s", errorMessage, topic.getDescription().length()),
            jsonMapper.writeValueAsString(topic));
      } else {
        errorStorageService.saveError(errorMessage, jsonMapper.writeValueAsString(topic));
      }
      return Optional.ofNullable(topic.getId());
    }
  }
}
