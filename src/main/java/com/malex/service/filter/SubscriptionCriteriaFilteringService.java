package com.malex.service.filter;

import static com.malex.model.filter.ConditionType.EXCLUDE;
import static com.malex.model.filter.ConditionType.INCLUDE;

import com.malex.model.dto.RssItemDto;
import com.malex.model.entity.FilterEntity;
import com.malex.model.filter.ConditionType;
import com.malex.model.filter.FilterCondition;
import com.malex.service.storage.FilterStorageService;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** Subscription criteria (include or exclude keywords) filtering service */
@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionCriteriaFilteringService {

  private static final String TEXT_FORMAT = "%s %s";

  @Value("${filter.criteria.title}")
  private boolean filterCriteriaOnlyToTitle;

  private final FilterStorageService filterStorageService;

  /** Apply filtering of rss topics by criteria */
  public boolean applyFilterByCriteria(RssItemDto itemDto, List<String> topicFilterIds) {
    // 1. define text
    String text = defineTextOrDefaultBehaviorToApplyFilters(itemDto);

    // 2. to check filters presence for rss topic
    if (topicFilterIds.isEmpty()) {
      return true;
    }

    // 3. find all filters in db
    List<FilterEntity> dbFilters = filterStorageService.findAllActiveFilters();

    // 4. find all exclude and include filter conditional by type
    Map<ConditionType, List<String>> filterConditions =
        dbFilters.stream()
            // apply subscription criteria
            .filter(filter -> topicFilterIds.contains(filter.getId()))
            .map(FilterEntity::getCondition)
            .collect(
                Collectors.groupingBy(
                    FilterCondition::type,
                    Collectors.flatMapping(fc -> fc.keyWords().stream(), Collectors.toList())));

    // 5. find include matching
    boolean includeAnyMatch =
        filterConditions.entrySet().stream()
            .filter(entry -> INCLUDE == entry.getKey())
            .map(Map.Entry::getValue)
            .flatMap(Collection::stream)
            .anyMatch(key -> findOccurrencePhraseIgnoreCase(text, key));

    // 6. find exclude matching
    boolean excludeNoneMatch =
        filterConditions.entrySet().stream()
            .filter(entry -> EXCLUDE == entry.getKey())
            .map(Map.Entry::getValue)
            .flatMap(Collection::stream)
            .noneMatch(key -> findOccurrencePhraseIgnoreCase(text, key));

    return includeAnyMatch && excludeNoneMatch;
  }

  /** Define text to apply filters */
  private String defineTextOrDefaultBehaviorToApplyFilters(RssItemDto rssItem) {
    var title = rssItem.title();
    // default behavior
    if (filterCriteriaOnlyToTitle) {
      return title;
    }
    var description = rssItem.description();
    return String.format(TEXT_FORMAT, title, description);
  }

  /** find the occurrence of specific phrase within a text */
  private boolean findOccurrencePhraseIgnoreCase(String text, String phrase) {
    return toLowerCase(text).indexOf(toLowerCase(phrase)) >= 1;
  }

  private String toLowerCase(String str) {
    return Optional.ofNullable(str).map(String::toLowerCase).orElse(str);
  }
}
