package com.malex.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoryCleanerScheduler {

  @Async
  @Scheduled(cron = "${scheduled.processing.cleaner.cron}")
  public void processing() {
    log.info("Start processing cleaner rss topics");
  }
}
