package com.malex.service.topic;

import com.malex.model.dto.RssItemDto;
import com.malex.model.entity.SubscriptionEntity;
import com.malex.service.customisation.CustomisationService;
import com.malex.service.filter.SubscriptionCriteriaFilteringService;
import com.malex.service.storage.RssTopicStorageService;
import com.malex.webservice.rss.RssReaderWebService;
import java.util.function.Predicate;
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
        .filter(excludeDuplicatesAndRepetitionsByMd5Hash())
        .filter(excludeBasedOnExclusionOrInclusionFilteringCriteria())
        // topicFormater::apply
        // todo: SendPhoto query: [400] Bad Request: message caption is too long
        .map(customisationService::applyRssTopicCustomization)
        .forEach(topicStorageService::saveRssTopic);
  }

  private Predicate<RssItemDto> excludeDuplicatesAndRepetitionsByMd5Hash() {
    return item -> {
      var md5Hash = item.md5Hash();
      return topicStorageService.isNotExistTopicByMd5Hash(md5Hash);
    };
  }

  private Predicate<RssItemDto> excludeBasedOnExclusionOrInclusionFilteringCriteria() {
    return filteringService::applyFilteringCriteriaIncludedOrExcluded;
  }
}
