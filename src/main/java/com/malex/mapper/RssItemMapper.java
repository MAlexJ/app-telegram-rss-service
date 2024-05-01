package com.malex.mapper;

import com.apptasticsoftware.rssreader.Item;
import com.malex.model.dto.RssItemDto;
import com.malex.model.dto.SubscriptionItemDto;
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
    return RssItemDto.builder()
        .link(readStingValue(item.getLink()))
        .title(readStingValue(item.getTitle()))
        .description(readStingValue(item.getDescription()))
        .build();
  }

  /** map RSS item to dto with MD5 hash calculation */
  public SubscriptionItemDto mapItemToDtoWithMd5Hash(Item item, SubscriptionEntity subscription) {
    // item info
    var link = readStingValue(item.getLink());
    var title = readStingValue(item.getTitle());
    var description = readStingValue(item.getDescription());
    // MD5 hash calculation
    var md5Hash = hashService.calculateMd5HashByCriteria(link, title, description);
    return SubscriptionItemDto.builder()
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
        .imageId(subscription.getImageId())
        .customizationId(subscription.getCustomizationId())
        .filterIds(subscription.getFilterIds())
        .build();
  }

  public String readStingValue(Optional<String> value) {
    return value.orElse(EMPTY_STRING);
  }
}
