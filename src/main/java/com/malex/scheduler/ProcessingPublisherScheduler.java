package com.malex.scheduler;

import com.malex.exception.TelegramPublisherException;
import com.malex.exception.TemplateResolverException;
import com.malex.model.entity.RssTopicEntity;
import com.malex.service.TelegramPublisherService;
import com.malex.service.resolver.TemplateResolverService;
import com.malex.service.storage.RssTopicStorageService;
import com.malex.service.storage.SubscriptionStorageService;
import com.malex.service.storage.TemplateStorageService;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessingPublisherScheduler {

  private final RssTopicStorageService rssTopicService;
  private final TelegramPublisherService publisherService;
  private final TemplateStorageService templateStorageService;
  private final TemplateResolverService templateResolverService;
  private final SubscriptionStorageService subscriptionStorageService;

  private final AtomicInteger schedulerProcessNumber = new AtomicInteger(0);

  @Async
  @Transactional
  @Scheduled(cron = "${scheduled.processing.publisher.cron}")
  public void processingPublisherRss() {
    log.info("Start processing publish topics - {}", schedulerProcessNumber.incrementAndGet());

    // test
    List<String> subscriptionIds = subscriptionStorageService.findAllActiveSubscriptionIds();
//    List<String> testTopi = rssTopicService.findAllActiveTest(subscriptionIds);
    // test

    rssTopicService
        .findFirstActiveRssTopicOrderByCreatedDate()
        .ifPresent(
            topic -> {
              var topicId = topic.getId();
              var chatId = topic.getChatId();
              var templateId = topic.getTemplateId();
              handleException(
                  topic,
                  () -> {
                    var placeholder =
                        templateStorageService.findExistOrDefaultTemplateById(templateId);
                    templateResolverService
                        .applyTemplateToRssTopic(placeholder, topic)
                        .flatMap(message -> publisherService.postMessage(chatId, message))
                        .map(Message::getMessageId)
                        .ifPresent(
                            messageId -> rssTopicService.setRssTopicInactivity(topicId, messageId));
                  });
            });
  }

  private void handleException(RssTopicEntity topic, Runnable action) {
    try {
      action.run();
    } catch (TemplateResolverException | TelegramPublisherException ex) {
      String topicId = topic.getId();
      log.error("Processing publish topic by id - {}, error message- {}", topicId, ex.getMessage());
      // todo : save error about topic and error cause
      /*
      1. RSS topic - boolean hasError -> true
      2. entity class Error{
      String message;
      LocalDate date;
      }
      3. Topic record:
       */
      rssTopicService.setRssTopicInactivity(topicId);
    }
  }
}
