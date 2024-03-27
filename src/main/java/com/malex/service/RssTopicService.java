package com.malex.service;

import com.malex.model.RssTopic;
import com.malex.model.entity.SubscriptionEntity;
import com.malex.webservice.RssReaderWebService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RssTopicService {

  private final Md5HashService md5HashService;
  private final RssReaderWebService rssWebService;

  /** Convert Rss items from subscription to rss topics */
  public List<RssTopic> processingRssTopics(SubscriptionEntity subscription) {
    var url = subscription.getRss();
    var rssItemList = rssWebService.readRssNews(url);
    return rssItemList.stream()
        .map(
            rssItem ->
                new RssTopic(
                    subscription, rssItem, md5HashService.md5HashCalculation(rssItem.link())))
        .toList();
  }
}
