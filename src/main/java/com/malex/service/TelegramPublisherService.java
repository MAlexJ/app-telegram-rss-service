package com.malex.service;

import com.malex.model.entity.RssTopicEntity;
import com.malex.service.resolver.TemplateResolverService;
import com.malex.service.storage.TemplateStorageService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramPublisherService {

  private final TemplateResolverService templateResolverService;
  private final TemplateStorageService templateStorageService;
  private final DefaultAbsSender sender;

  public Optional<RssTopicEntity> publishRssTopic(RssTopicEntity topic) {
    try {
      var chatId = topic.getChatId();
      var text = generateTextFromTemplate(topic);
      var message =
          sender.execute(
              SendMessage.builder()
                  .protectContent(true)
                  .chatId(chatId)
                  .parseMode(ParseMode.MARKDOWN)
                  .text(text)
                  .build());
      log.info("Publish RSS topic to telegram - {}", message);
    } catch (Exception ex) {
      log.error("Telegram Api error - {}", ex.getMessage());
    }
    return Optional.of(topic);
  }

  private String generateTextFromTemplate(RssTopicEntity topic) {
    var template = templateStorageService.findTemplateById(topic.getTemplateId());
    return templateResolverService.applyMessageTemplate(template, topic);
  }
}
