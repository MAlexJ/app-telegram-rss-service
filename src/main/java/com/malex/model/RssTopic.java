package com.malex.model;

import com.malex.model.entity.SubscriptionEntity;
import com.malex.model.filter.RssFilter;
import java.util.Optional;

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
        removeZeroWidthSpace(item.title()),
        removeZeroWidthSpace(item.description()),
        item.link(),
        md5Hash,
        true);
  }

  /**
   * Remove 'Zero Width Space'
   *
   * <p>https://symbl.cc/ru/200B/
   *
   * <p>link:
   * https://stackoverflow.com/questions/42960282/how-to-remove-u200b-zero-length-whitespace-unicode-character-from-string-in-j
   */
  private static String removeZeroWidthSpace(String str) {
    return Optional.ofNullable(str) //
        .map(desc -> desc.replaceAll("[\\p{Cf}]", ""))
        .map(desc -> desc.replaceAll("&quot;", "\""))
        .map(desc -> desc.replaceAll("&#39;", "`"))
        .orElse(null);
  }
}
