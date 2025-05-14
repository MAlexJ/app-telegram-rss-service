package com.malex.scheduler;

import com.malex.service.storage.RssTopicStorageService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoryCleanerScheduler {

  private static final LocalDateTime CLEAN_DATE_TIME = LocalDateTime.now().minusMonths(1);

  private final RssTopicStorageService service;

  @Async("virtualThreadExecutor")
  @Scheduled(cron = "${scheduled.processing.cleaner.cron}")
  @Transactional
  public void processing() {
    try {
      log.info("Start processing cleaner rss topics");
      var topicIds = service.findAllTopicIdsBeforeDate(CLEAN_DATE_TIME);
      if (!topicIds.isEmpty()) {
        log.info("Removed topics by ids - {}", topicIds);
      }
      topicIds.forEach(service::removeTopicById);
    } catch (Exception e) {
      log.error("Cleaner scheduler error - {}", e.getMessage());
    }
  }
}
