package com.malex.scheduler;

import static com.malex.utils.RssTopicUtils.randomlyRearrangingIds;

import com.malex.model.entity.RssTopicEntity;
import com.malex.service.ErrorService;
import com.malex.service.resolver.TemplateResolverService;
import com.malex.service.storage.*;
import com.malex.webservice.TelegramPublisherWebService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RssTopicPublisherScheduler {

  private final ErrorService errorService;
  private final RssTopicStorageService rssTopicService;
  private final TelegramPublisherWebService publisherService;
  private final TemplateResolverService templateResolverService;
  private final SubscriptionStorageService subscriptionStorageService;

  @Value("${rss.publish.topic.limit:30}")
  private Integer limit;

  @Async
  @Transactional
  @Scheduled(cron = "${scheduled.processing.publisher.cron}")
  public void processingRssTopics() {
    log.info("Start processing publish topics");
    var subscriptionIds = subscriptionStorageService.findAllActiveSubscriptionIds();
    randomlyRearrangingIds(subscriptionIds)
        .forEach(
            subscriptionId ->
                rssTopicService
                    .findFirstActiveTopicsBySubscriptionIdOrderByCreatedAsc(subscriptionId, limit)
                    .forEach(
                        topic ->
                            errorService
                                .handleException(topic, () -> executeRssTopicPublication(topic))
                                .ifPresent(rssTopicService::deactivateRssTopics)));
  }

  private void executeRssTopicPublication(RssTopicEntity topic) {
    var topicId = topic.getId();
    var image = topic.getImage();
    var chatId = topic.getChatId();
    var templateId = topic.getTemplateId();
    templateResolverService
        .findTemplateAndApplyToRssTopic(templateId, topic)
        .map(text -> publishTopic(text, image, chatId))
        .ifPresent(messageId -> rssTopicService.deactivateRssTopics(topicId, messageId));
  }

  /** Return telegram message Id */
  private Integer publishTopic(String text, String image, Long chatId) {
    return Objects.isNull(image)
        ? publisherService.sendMessage(chatId, text)
        : publisherService.sendMessage(chatId, image, text);
  }
}
