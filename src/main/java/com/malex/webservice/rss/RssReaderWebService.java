package com.malex.webservice.rss;

import com.apptasticsoftware.rssreader.RssReader;
import com.malex.mapper.rss.RssItemMapper;
import com.malex.model.dto.RssItemDto;
import com.malex.model.entity.SubscriptionEntity;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RssReaderWebService {

  private final RssItemMapper mapper;

  /** Read the latest news on Rss url */
  public List<RssItemDto> readRssNews(String url) {
    try {
      return new RssReader().read(url).map(mapper::mapItemToDto).toList();
    } catch (IOException e) {
      return handleError(url, e);
    }
  }

  /** Read the latest news on Rss url */
  public List<RssItemDto> readRssNews(SubscriptionEntity subscription) {
    try {
      var url = subscription.getRss();
      return new RssReader()
          .read(url)
          .map(item -> mapper.mapItemToDtoWithMd5Hash(item, subscription))
          .toList();
    } catch (IOException e) {
      return handleError(subscription, e);
    }
  }

  private List<RssItemDto> handleError(Object obj, IOException e) {
    log.error("Error reading RSS by - {}, error - {}", obj, e.getMessage());
    return Collections.emptyList();
  }
}
