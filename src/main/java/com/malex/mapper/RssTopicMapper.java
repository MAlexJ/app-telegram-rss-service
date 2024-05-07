package com.malex.mapper;

import com.malex.model.dto.RssItemDto;
import com.malex.model.dto.RssTopicDto;
import org.springframework.stereotype.Service;

@Service
public class RssTopicMapper {

  public RssTopicDto mapToDto(RssItemDto rssItem, String image, String description) {
    return RssTopicDto.builder()
        .subscriptionId(rssItem.subscriptionId())
        .chatId(rssItem.chatId())
        .templateId(rssItem.templateId())
        .image(image)
        .title(rssItem.title())
        .description(description)
        .link(rssItem.link())
        .md5Hash(rssItem.md5Hash())
        .messageId(null)
        .isActive(true)
        .build();
  }

  public RssTopicDto mapToDto(RssItemDto rssItem) {
    return RssTopicDto.builder()
        .subscriptionId(rssItem.subscriptionId())
        .chatId(rssItem.chatId())
        .templateId(rssItem.templateId())
        .image(null)
        .title(rssItem.title())
        .description(rssItem.description())
        .link(rssItem.link())
        .md5Hash(rssItem.md5Hash())
        .messageId(null)
        .isActive(true)
        .build();
  }
}
