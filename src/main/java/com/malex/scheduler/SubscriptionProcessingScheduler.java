package com.malex.scheduler;

import com.malex.service.RssTopicService;
import com.malex.service.cache.SubscriptionCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionProcessingScheduler {

  private final RssTopicService topicService;
  private final SubscriptionCacheService subscriptionService;

  /**
   * Find all active subscriptions, apply user filters and determine whether such a record in
   * database or not. <br>
   * If no record is found, then save rss topic to the database.
   */
  @Async
  @Transactional
  @Scheduled(cron = "${scheduled.processing.rss.cron}")
  public void processingRssSubscriptions() {
    log.info("Start processing RSS subscriptions");
    subscriptionService
        .findAllActiveSubscriptionsCacheable()
        .forEach(topicService::processingRssSubscriptions);
  }
}
