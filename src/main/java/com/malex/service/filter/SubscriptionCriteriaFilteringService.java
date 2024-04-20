package com.malex.service.filter;

import static com.malex.model.filter.ConditionType.EXCLUDE;
import static com.malex.model.filter.ConditionType.INCLUDE;

import com.malex.model.entity.FilterEntity;
import com.malex.model.filter.ConditionType;
import com.malex.model.filter.FilterCondition;
import com.malex.service.storage.FilterStorageService;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Subscription criteria (include or exclude keywords) filtering service */
@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionCriteriaFilteringService {

  private final FilterStorageService filterStorageService;

  /** Apply filtering of rss topics by criteria */
  public boolean applyFilterByCriteria(String text, List<String> topicFilterIds) {
    // 1. find all topic filter ids
    if (Objects.isNull(text) || topicFilterIds.isEmpty()) {
      return true;
    }

    // 2. find all filters in db
    List<FilterEntity> dbFilters = filterStorageService.findAllActiveFilters();

    // 3. find all exclude and include filter conditional by type
    Map<ConditionType, List<String>> filterConditions =
        dbFilters.stream()
            // apply subscription criteria
            .filter(filter -> topicFilterIds.contains(filter.getId()))
            .map(FilterEntity::getCondition)
            .collect(
                Collectors.groupingBy(
                    FilterCondition::type,
                    Collectors.flatMapping(fc -> fc.keyWords().stream(), Collectors.toList())));

    // 4. find include matching
    boolean includeAnyMatch =
        filterConditions.entrySet().stream()
            .filter(entry -> INCLUDE == entry.getKey())
            .map(Map.Entry::getValue)
            .flatMap(Collection::stream)
            .anyMatch(key -> findOccurrencePhrase(text, key));

    // 5. find exclude matching
    boolean excludeNoneMatch =
        filterConditions.entrySet().stream()
            .filter(entry -> EXCLUDE == entry.getKey())
            .map(Map.Entry::getValue)
            .flatMap(Collection::stream)
            .noneMatch(key -> findOccurrencePhrase(text, key));

    log.info(
        "Filter: include anyMatch - {}, exclude noneMatch - {}", includeAnyMatch, excludeNoneMatch);

    return includeAnyMatch;
  }

  /** find the occurrence of specific phrase within a text */
  private boolean findOccurrencePhrase(String text, String phrase) {
    return toLowerCase(text).indexOf(toLowerCase(phrase)) >= 1;
  }

  private String toLowerCase(String str) {
    return Optional.ofNullable(str).map(String::toLowerCase).orElse(str);
  }
}
