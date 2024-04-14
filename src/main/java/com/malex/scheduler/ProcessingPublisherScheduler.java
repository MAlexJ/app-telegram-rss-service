package com.malex.scheduler;

import com.malex.mapper.JsonMapper;
import com.malex.model.entity.RssTopicEntity;
import com.malex.service.TelegramPublisherService;
import com.malex.service.resolver.TemplateResolverService;
import com.malex.service.storage.ErrorStorageService;
import com.malex.service.storage.RssTopicStorageService;
import com.malex.service.storage.SubscriptionStorageService;
import com.malex.service.storage.TemplateStorageService;
import java.util.ArrayList;
import java.util.Collections;
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

  private final JsonMapper jsonMapper;
  private final RssTopicStorageService rssTopicService;
  private final ErrorStorageService errorStorageService;
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
    var subscriptionIds = subscriptionStorageService.findAllActiveSubscriptionIds();
    randomlyRearrangingIds(subscriptionIds)
        .forEach(
            subscriptionId ->
                rssTopicService
                    .findFirstActiveTopicBySubscriptionIdOrderByCreatedAsc(subscriptionId)
                    .ifPresent(
                        topic -> {
                          var topicId = topic.getId();
                          var chatId = topic.getChatId();
                          var templateId = topic.getTemplateId();
                          handleException(
                              topic,
                              () -> {
                                var placeholder =
                                    templateStorageService.findExistOrDefaultTemplateById(
                                        templateId);
                                templateResolverService
                                    .applyTemplateToRssTopic(placeholder, topic)
                                    .flatMap(
                                        message -> publisherService.postMessage(chatId, message))
                                    .map(Message::getMessageId)
                                    .ifPresent(
                                        messageId ->
                                            rssTopicService.setRssTopicInactivity(
                                                topicId, messageId));
                              });
                        }));
  }

  private List<String> randomlyRearrangingIds(List<String> ids) {
    var list = new ArrayList<>(ids);
    Collections.shuffle(list);
    return list;
  }

  private void handleException(RssTopicEntity topic, Runnable action) {
    try {
      action.run();
    } catch (Exception ex) {
      String topicId = topic.getId();
      log.error("Processing publish topic by id - {}, error message- {}", topicId, ex.getMessage());
      errorStorageService.saveError(ex.getMessage(), jsonMapper.writeValueAsString(topic));
      rssTopicService.setRssTopicInactivity(topicId);
    }
  }
}
