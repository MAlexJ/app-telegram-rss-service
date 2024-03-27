package com.malex.model;

import com.malex.model.entity.SubscriptionEntity;
import com.malex.model.filter.RssFilter;

public record RssTopic(
    String subscriptionId,
    Long chatId,
    String templateId,
    String rss,
    RssFilter filter,
    String title,
    String description,
    String link,
    String md5Hash,
    boolean isActive) {

  public RssTopic(SubscriptionEntity entity, RssItem item, String md5Hash) {
    this(
        entity.getId(),
        entity.getChatId(),
        entity.getTemplateId(),
        entity.getRss(),
        entity.getFilter(),
        item.title(),
        item.description(),
        item.link(),
        md5Hash,
        true);
  }
}
