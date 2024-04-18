package com.malex.scheduler;

import static com.malex.utils.RssTopicUtils.randomlyRearrangingIds;

import com.malex.model.entity.RssTopicEntity;
import com.malex.service.ErrorService;
import com.malex.service.TelegramPublisherService;
import com.malex.service.resolver.TemplateResolverService;
import com.malex.service.storage.*;
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

  private final ErrorService errorService;
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
    var subscriptionIds = subscriptionStorageService.findAllActiveSubscriptionIds();
    randomlyRearrangingIds(subscriptionIds)
        .forEach(
            subscriptionId ->
                rssTopicService
                    .findFirstActiveTopicBySubscriptionIdOrderByCreatedAsc(subscriptionId)
                    .flatMap(
                        topic ->
                            errorService.handleException(
                                topic, () -> processPublishRssTopic(topic)))
                    .ifPresent(rssTopicService::setRssTopicInactivity));
  }

  private void processPublishRssTopic(RssTopicEntity topic) {
    var topicId = topic.getId();
    var chatId = topic.getChatId();
    var templateId = topic.getTemplateId();

    // todo refactor template storage and template resolver services
    var placeholder = templateStorageService.findExistOrDefaultTemplateById(templateId);
    templateResolverService
        .applyTemplateToRssTopic(placeholder, topic)
        .flatMap(message -> publisherService.postMessage(chatId, message))
        .map(Message::getMessageId)
        .ifPresent(messageId -> rssTopicService.setRssTopicInactivity(topicId, messageId));
  }
}
