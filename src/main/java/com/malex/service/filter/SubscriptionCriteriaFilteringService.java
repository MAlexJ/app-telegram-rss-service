package com.malex.service.filter;

import static com.malex.model.filter.ConditionType.EXCLUDE;
import static com.malex.model.filter.ConditionType.INCLUDE;

import com.malex.model.dto.SubscriptionItemDto;
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
  public boolean applyFilterByCriteria(SubscriptionItemDto subscriptionItem) {
    var filterIds = subscriptionItem.filterIds();
    if (filterIds.isEmpty()) {
      return true;
    }

    var title = subscriptionItem.title();
    var description = subscriptionItem.description();
    var text = defineTextOrDefaultBehaviorToApplyFilters(title, description);

    var filterConditionMap =
        filterStorageService.findAllActiveFilters().stream()
            // apply subscription criteria
            .filter(filter -> filterIds.contains(filter.getId()))
            .map(FilterEntity::getCondition)
            .collect(
                Collectors.groupingBy(
                    FilterCondition::type,
                    Collectors.flatMapping(fc -> fc.keyWords().stream(), Collectors.toList())));

    return hasInclusiveFilterMatchingCondition(filterConditionMap, text)
        && hasExcludingFilterMatchingCondition(filterConditionMap, text);
  }

  private boolean hasExcludingFilterMatchingCondition(
      Map<ConditionType, List<String>> conditions, String text) {
    return findConditionByType(conditions, EXCLUDE).stream()
        .map(Map.Entry::getValue)
        .flatMap(Collection::stream)
        .noneMatch(key -> findOccurrencePhraseIgnoreCase(text, key));
  }

  private boolean hasInclusiveFilterMatchingCondition(
      Map<ConditionType, List<String>> conditions, String text) {
    var conditionMap = findConditionByType(conditions, INCLUDE);
    return conditionMap.isEmpty()
        || conditionMap.stream()
            .map(Map.Entry::getValue)
            .flatMap(Collection::stream)
            .anyMatch(key -> findOccurrencePhraseIgnoreCase(text, key));
  }

  /** There is an excluding condition for matching */
  private List<Map.Entry<ConditionType, List<String>>> findConditionByType(
      Map<ConditionType, List<String>> conditions, ConditionType type) {
    return conditions.entrySet().stream() //
        .filter(entry -> type == entry.getKey())
        .toList();
  }

  /** Define text to apply filters */
  private String defineTextOrDefaultBehaviorToApplyFilters(String title, String description) {
    // default behavior
    if (filterCriteriaOnlyToTitle) {
      return title;
    }
    return String.format(TEXT_FORMAT, title, description);
  }

  /** find the occurrence of specific phrase within a text */
  private boolean findOccurrencePhraseIgnoreCase(String text, String phrase) {
    var textOpt = toLowerCase(text);
    var phraseOpt = toLowerCase(phrase);
    if (textOpt.isEmpty() || phraseOpt.isEmpty()) {
      return false;
    }
    return textOpt.get().indexOf(phraseOpt.get()) >= 1;
  }

  private Optional<String> toLowerCase(String str) {
    return Optional.ofNullable(str).map(String::toLowerCase);
  }
}
