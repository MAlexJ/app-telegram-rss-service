package com.malex.service;

import com.malex.model.dto.RssItemDto;
import com.malex.model.dto.RssTopicDto;
import com.malex.model.entity.SubscriptionEntity;
import com.malex.service.filter.SubscriptionCriteriaFilteringService;
import com.malex.webservice.RssReaderWebService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RssTopicService {

  private static final String TEXT_FORMAT = "%s %s";

  private final Md5HashService md5HashService;
  private final RssReaderWebService rssWebService;
  private final SubscriptionCriteriaFilteringService filterService;

  @Value("${calculate.md5Hash.for.link}")
  private boolean calculateMd5HashBaseOnLink;

  @Value("${filter.criteria.to.title}")
  private boolean filterCriteriaOnlyToTitle;

  /** Convert Rss items from subscription to rss topics */
  public List<RssTopicDto> processingRssTopicsWithFilteringCriteria(
      SubscriptionEntity subscription) {
    var url = subscription.getRss();
    var filterIds = subscription.getFilterIds();
    return rssWebService.readRssNews(url).stream()
        .filter(
            rssItem -> {
              var text = buildTopicRepresentation(rssItem);
              return filterService.applyFilterByCriteria(text, filterIds);
            })
        .map(
            rssItem -> {
              var md5Hash = calculateMd5HashByCriteria(rssItem);
              return new RssTopicDto(subscription, rssItem, md5Hash);
            })
        .toList();
  }

  private String buildTopicRepresentation(RssItemDto rssItem) {
    var title = rssItem.title();
    if (filterCriteriaOnlyToTitle) {
      return title;
    }
    var description = rssItem.description();
    return String.format(TEXT_FORMAT, title, description);
  }

  private String calculateMd5HashByCriteria(RssItemDto rssItem) {
    var link = rssItem.link();
    if (calculateMd5HashBaseOnLink) {
      return md5HashService.md5HashCalculation(link);
    }
    var title = rssItem.title();
    var description = rssItem.description();
    return md5HashService.md5HashCalculation(link, title, description);
  }
}
