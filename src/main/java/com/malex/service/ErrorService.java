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
