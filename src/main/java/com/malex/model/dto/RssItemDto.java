package com.malex.model.dto;

import com.apptasticsoftware.rssreader.Item;
import java.util.List;
import java.util.Optional;

public record RssItemDto(
    String title,
    String description,
    String link,
    String author,
    List<String> categories,
    String guid,
    Boolean isPermaLink,
    String pubDate,
    String comments) {

  private static final String NULL_FRIENDLY_FORMAT = "";

  public RssItemDto(Item item) {
    this(
        readStingValue(item.getTitle()),
        readStingValue(item.getDescription()),
        readStingValue(item.getLink()),
        readStingValue(item.getAuthor()),
        item.getCategories(),
        readStingValue(item.getGuid()),
        readBooleanValue(item.getIsPermaLink()),
        readStingValue(item.getPubDate()),
        readStingValue(item.getComments()));
  }

  private static String readStingValue(Optional<String> value) {
    return value.orElse(NULL_FRIENDLY_FORMAT);
  }

  private static Boolean readBooleanValue(Optional<Boolean> value) {
    return value.orElse(false);
  }
}
