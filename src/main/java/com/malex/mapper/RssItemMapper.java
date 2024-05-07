package com.malex.mapper;

import com.apptasticsoftware.rssreader.Item;
import com.malex.model.dto.RssItemDto;
import com.malex.model.entity.SubscriptionEntity;
import com.malex.service.Md5HashService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RssItemMapper {

  private static final String EMPTY_STRING = "";

  private final Md5HashService hashService;

  /** map RSS item to dto */
  public RssItemDto mapItemToDto(Item item) {
    // item info
    var link = readStingValue(item.getLink());
    var title = readStingValue(item.getTitle());
    var description = readStingValue(item.getDescription());
    var md5Hash = hashService.calculateMd5HashByCriteria(link, title, description);
    return RssItemDto.builder()
        .link(link)
        .title(title)
        .description(description)
        .md5Hash(md5Hash)
        .build();
  }

  /** map RSS item to dto with MD5 hash calculation */
  public RssItemDto mapItemToDtoWithMd5Hash(Item item, SubscriptionEntity subscription) {
    // item info
    var link = readStingValue(item.getLink());
    var title = readStingValue(item.getTitle());
    var description = readStingValue(item.getDescription());
    // MD5 hash calculation
    var md5Hash = hashService.calculateMd5HashByCriteria(link, title, description);
    return RssItemDto.builder()
        // item info
        .link(link)
        .title(title)
        .description(description)
        // MD5 hash
        .md5Hash(md5Hash)
        // subscription info
        .subscriptionId(subscription.getId())
        .chatId(subscription.getChatId())
        .templateId(subscription.getTemplateId())
        .customizationId(subscription.getCustomizationId())
        .filterIds(subscription.getFilterIds())
        .build();
  }

  public String readStingValue(Optional<String> value) {
    return value.orElse(EMPTY_STRING);
  }
}
