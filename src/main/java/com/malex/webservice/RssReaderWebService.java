package com.malex.webservice;

import com.apptasticsoftware.rssreader.RssReader;
import com.malex.mapper.RssItemMapper;
import com.malex.model.dto.RssItemDto;
import com.malex.model.dto.SubscriptionItemDto;
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
      log.error("Error reading RSS by url - {}, error - {}", url, e.getMessage());
      return Collections.emptyList();
    }
  }

  /** Read the latest news on Rss url */
  public List<SubscriptionItemDto> readRssNews(SubscriptionEntity subscription) {
    try {
      var url = subscription.getRss();
      return new RssReader()
          .read(url)
          .map(item -> mapper.mapItemToDtoWithMd5Hash(item, subscription))
          .toList();
    } catch (IOException e) {
      log.error("Error reading RSS by subscription - {}, error - {}", subscription, e.getMessage());
      return Collections.emptyList();
    }
  }
}
