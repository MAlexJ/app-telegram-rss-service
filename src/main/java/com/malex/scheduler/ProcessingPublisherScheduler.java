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

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessingPublisherScheduler {

  private final ErrorService errorService;
  private final RssTopicStorageService rssTopicService;
  private final TelegramPublisherService publisherService;
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
                                topic, () -> executeRssTopicPublication(topic)))
                    .ifPresent(rssTopicService::setRssTopicInactivity));
  }

  private void executeRssTopicPublication(RssTopicEntity topic) {
    var topicId = topic.getId();
    var image = topic.getImage();
    var chatId = topic.getChatId();
    var templateId = topic.getTemplateId();
    templateResolverService
        .findTemplateAndApplyToRssTopic(templateId, topic)
        .map(text -> publisherService.sendMessage(chatId, image, text))
        .ifPresent(messageId -> rssTopicService.setRssTopicInactivity(topicId, messageId));
  }
}
