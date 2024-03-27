package com.malex.scheduler;

import com.malex.service.TelegramPublisherService;
import com.malex.service.resolver.TemplateResolverService;
import com.malex.service.storage.RssTopicStorageService;
import com.malex.service.storage.TemplateStorageService;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessingPublisherScheduler {

  private static final String DEFAULT_TEMPLATE = "{{title}} \n {{link}}";

  private final RssTopicStorageService rssTopicService;
  private final TelegramPublisherService publisherService;
  private final TemplateStorageService templateStorageService;
  private final TemplateResolverService templateResolverService;

  private final AtomicInteger schedulerProcessNumber = new AtomicInteger(0);

  @Async
  @Transactional
  @Scheduled(cron = "${scheduled.processing.publisher.cron}")
  public void processingPublisherRss() {
    log.info("Start processing publish topics - {}", schedulerProcessNumber.incrementAndGet());
    rssTopicService
        .findFirstActiveRssTopicOrderByCreatedDate()
        .ifPresent(
            topic -> {
              var topicId = topic.getId();
              var chatId = topic.getChatId();
              var templateId = topic.getTemplateId();
              var template =
                  templateStorageService
                      .findMessageTemplateById(templateId)
                      .orElse(DEFAULT_TEMPLATE);
              var message = templateResolverService.applyTemplateToRssTopic(template, topic);
              publisherService.postMessageToTelegram(chatId, message);
              rssTopicService.setRssTopicInactivity(topicId);
            });
  }
}
