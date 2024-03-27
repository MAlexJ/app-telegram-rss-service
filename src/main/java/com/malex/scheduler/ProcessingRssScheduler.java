package com.malex.scheduler;

import com.malex.mapper.ObjectMapper;
import com.malex.service.RssFilterService;
import com.malex.service.RssTopicService;
import com.malex.service.storage.RssTopicStorageService;
import com.malex.service.storage.SubscriptionStorageService;
import java.util.*;
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
public class ProcessingRssScheduler {

  private final ObjectMapper mapper;
  private final RssTopicService topicService;
  private final RssFilterService filterService;
  private final RssTopicStorageService topicStorageService;
  private final SubscriptionStorageService subscriptionService;

  private final AtomicInteger schedulerProcessNumber = new AtomicInteger(0);

  /**
   * Find all active subscriptions, apply user filters and determine whether such a record in
   * database or not. <br>
   * If no record is found, then save rss topic to the database.
   */
  @Async
  @Transactional
  @Scheduled(cron = "${scheduled.processing.rss.cron}")
  public void processingRssSubscriptions() {
    log.info("Start processing RSS subscriptions - {}", schedulerProcessNumber.incrementAndGet());
    var md5HashSet = topicStorageService.findAllMd5Hash();
    subscriptionService.findAllActiveSubscriptions().stream()
        .map(topicService::processingRssTopics)
        .flatMap(Collection::stream)
        .filter(filterService.applyTopicsFilteringByMd5Hash(md5HashSet))
        .filter(filterService::applyTopicsFilteringByCriteria)
        .map(mapper::dtoToEntity)
        .forEach(topicStorageService::saveNewRssTopic);
  }
}
