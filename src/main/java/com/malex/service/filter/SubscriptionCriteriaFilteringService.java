package com.malex.service.filter;

import static com.malex.model.filter.ConditionType.INCLUDE;

import com.malex.model.dto.RssTopicDto;
import com.malex.model.entity.FilterEntity;
import com.malex.model.filter.ConditionType;
import com.malex.model.filter.FilterCondition;
import com.malex.service.storage.FilterStorageService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Subscription criteria (include or exclude keywords) filtering service */
@Service
@RequiredArgsConstructor
public class SubscriptionCriteriaFilteringService {

  private final FilterStorageService filterStorageService;

  /** Apply filtering of rss topics by criteria */
  public boolean applyFilterByCriteria(RssTopicDto topic) {

    // 1. find all topic filter ids
    var topicFilterIds = topic.filterIds();
    if (topicFilterIds.isEmpty()) {
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

    // 4. extract topic title
    String title = topic.title();

    // 5. find include matching
    boolean includeAnyMatch =
        filterConditions.get(INCLUDE).stream().anyMatch(key -> findOccurrencePhrase(title, key));

    // 6. find exclude matching

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
