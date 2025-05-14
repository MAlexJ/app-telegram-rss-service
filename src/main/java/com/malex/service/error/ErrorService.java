package com.malex.service.error;

import com.malex.exception.telegram.TelegramPublisherException;
import com.malex.exception.template.TemplateResolverException;
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

      if (errorMessage.contains("retry after")) {
        /*
         * Telegram API
         * 1. Telegram API will not allow more than 30 messages per second or so
         * 2. Also note that your bot will not be able to send more than 20 messages per minute to the same group.
         */
      }

      if (errorMessage.contains("message caption is too long")) {
        /*
         * Handle cases and truncate message ~500, ~700 characters
         */
      }

      // todo: Working with Date Parameters in Spring
      //  link: https://www.baeldung.com/spring-date-parameters
      errorStorageService.saveError(errorMessage, jsonMapper.writeValueAsString(topic));
      return Optional.ofNullable(topic.getId());
    }
  }
}
