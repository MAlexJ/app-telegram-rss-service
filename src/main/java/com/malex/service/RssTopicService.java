package com.malex.service;

import com.malex.model.dto.RssItemDto;
import com.malex.model.dto.RssTopicDto;
import com.malex.model.entity.SubscriptionEntity;
import com.malex.webservice.RssReaderWebService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RssTopicService {

  private final Md5HashService md5HashService;
  private final RssReaderWebService rssWebService;

  /** Convert Rss items from subscription to rss topics */
  public List<RssTopicDto> processingRssTopics(SubscriptionEntity subscription) {
    var url = subscription.getRss();
    var rssItemList = rssWebService.readRssNews(url);
    return rssItemList.stream()
        .filter(
            rssItem -> {
              String title = rssItem.title();
              String description = rssItem.description();
              // apply filter by criteria
              return true;
            })
        .map(rssItem -> buildRssTopicDto(subscription, rssItem))
        .toList();
  }

  private RssTopicDto buildRssTopicDto(SubscriptionEntity subscription, RssItemDto rssItem) {
    var md5Hash = applyMd5HaseCalculation(rssItem);
    return new RssTopicDto(subscription, rssItem, md5Hash);
  }

  private String applyMd5HaseCalculation(RssItemDto rssItem) {
    var link = rssItem.link();
    return md5HashService.md5HashCalculation(link);
  }
}
