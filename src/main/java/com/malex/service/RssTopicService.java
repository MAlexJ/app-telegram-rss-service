package com.malex.service;

import com.malex.model.dto.RssItemDto;
import com.malex.model.entity.SubscriptionEntity;
import com.malex.service.customisation.CustomisationService;
import com.malex.service.filter.SubscriptionCriteriaFilteringService;
import com.malex.service.storage.RssTopicStorageService;
import com.malex.webservice.RssReaderWebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RssTopicService {

  private final RssReaderWebService rssWebService;
  private final CustomisationService customisationService;
  private final RssTopicStorageService topicStorageService;
  private final SubscriptionCriteriaFilteringService filteringService;

  public void processingRssSubscriptions(SubscriptionEntity subscription) {
    rssWebService.readRssNews(subscription).stream()
        .filter(this::verifyRssTopiMd5HashIdnDatabase)
        .filter(filteringService::verifyIncludedOrExcludedFilterCriteria)
        .map(customisationService::applyRssTopicCustomization)
        .forEach(topicStorageService::saveNewRssTopic);
  }

  private boolean verifyRssTopiMd5HashIdnDatabase(RssItemDto item) {
    return topicStorageService.isNotExistTopicByMd5Hash(item.md5Hash());
  }
}
