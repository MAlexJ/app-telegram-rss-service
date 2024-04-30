package com.malex.webservice;

import com.apptasticsoftware.rssreader.Item;
import com.apptasticsoftware.rssreader.RssReader;
import com.malex.model.dto.RssItemDto;
import com.malex.model.dto.SubscriptionItemDto;
import com.malex.model.entity.SubscriptionEntity;
import com.malex.service.Md5HashService;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RssReaderWebService {

  private static final String NULL_FRIENDLY_FORMAT = "";

  private final Md5HashService hashService;

  /** Read the latest news on Rss url */
  public List<RssItemDto> readRssNews(String url) {
    try {
      return new RssReader().read(url).map(this::mapping).toList();
    } catch (IOException e) {
      log.error("Error reading RSS by url - {}, error - {}", url, e.getMessage());
      return Collections.emptyList();
    }
  }

  /** Read the latest news on Rss url */
  public List<SubscriptionItemDto> readRssNews(SubscriptionEntity subscription) {
    try {
      var url = subscription.getRss();
      return new RssReader().read(url).map(item -> mapping(subscription, item)).toList();
    } catch (IOException e) {
      log.error("Error reading RSS by subscription - {}, error - {}", subscription, e.getMessage());
      return Collections.emptyList();
    }
  }

  /** Apply mapping and calculate MD5 hash */
  private RssItemDto mapping(Item item) {
    var link = readStingValue(item.getLink());
    var title = readStingValue(item.getTitle());
    var description = readStingValue(item.getDescription());
    // MD5 hash calculation
    var md5Hash = hashService.calculateMd5HashByCriteria(link, title, description);
    return new RssItemDto(title, description, link, md5Hash);
  }

  /** Apply mapping and calculate MD5 hash */
  private SubscriptionItemDto mapping(SubscriptionEntity subscription, Item item) {
    // subscription info
    var subscriptionId = subscription.getId();
    var chatId = subscription.getChatId();
    var templateId = subscription.getTemplateId();
    var imageId = subscription.getImageId();
    var filterIds = subscription.getFilterIds();
    // item info
    var link = readStingValue(item.getLink());
    var title = readStingValue(item.getTitle());
    var description = readStingValue(item.getDescription());
    // MD5 hash calculation
    var md5Hash = hashService.calculateMd5HashByCriteria(link, title, description);
    return new SubscriptionItemDto(
        title, description, link, md5Hash, subscriptionId, chatId, templateId, imageId, filterIds);
  }

  private String readStingValue(Optional<String> value) {
    return value.orElse(NULL_FRIENDLY_FORMAT);
  }
}
