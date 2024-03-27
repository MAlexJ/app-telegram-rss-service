package com.malex.scheduler;

import com.malex.service.TelegramPublisherService;
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

  private final RssTopicStorageService rssTopicService;
  private final TelegramPublisherService publisherService;

  private final AtomicInteger schedulerProcessNumber = new AtomicInteger(0);

  @Async
  @Transactional
  @Scheduled(cron = "${scheduled.processing.publisher.cron}")
  public void processingPublisherRss() {
    log.info("Start processing publish topics - {}", schedulerProcessNumber.incrementAndGet());

    rssTopicService
        .findFirstActiveRssTopic()
        .flatMap(publisherService::publishRssTopic)
        .ifPresent(entity -> rssTopicService.setTopicInactivity(entity.getId()));
  }
}
