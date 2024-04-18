package com.malex.service.filter;

import static com.malex.model.filter.ConditionType.INCLUDE;

import com.malex.model.dto.RssTopicDto;
import com.malex.model.entity.FilterEntity;
import com.malex.service.storage.FilterStorageService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RssFilterService {

  private final FilterStorageService filterStorageService;

  private static final String EMPTY_STRING = " ";

  /** Apply filtering of rss topics by criteria */
  public boolean applyFilterByCriteria(RssTopicDto topic) {
    var filterIds = topic.filterIds();
    if (filterIds.isEmpty()) {
      return true;
    }
    List<FilterEntity> filters =
        filterStorageService.findAllActiveFilters().stream()
            .filter(filter -> filterIds.contains(filter.getId()))
            .toList();

    List<String> includeKewWords =
        filters.stream()
            .map(FilterEntity::getCondition)
            .filter(condition -> INCLUDE == condition.type())
            .flatMap(condition -> condition.keyWords().stream())
            .toList();

    String title = topic.title();
    return includeKewWords.stream().anyMatch(key -> findOccurrencePhrase(title, key));

    //
    //    return Optional.ofNullable(topic.filter())
    //        .flatMap(
    //            filter ->
    //                Optional.ofNullable(filter.condition())
    //                    .map(
    //                        condition -> {
    //                          var title = topic.title();
    //                          var phraseOrWord = condition.value();
    //                          var type = condition.valueType();
    //                          switch (type) {
    //                            case PHRASE -> {
    //                              return findOccurrencePhrase(title, phraseOrWord);
    //                            }
    //                            case WORD -> {
    //                              return findOccurrenceWord(title, phraseOrWord);
    //                            }
    //                            default -> {
    //                              return false;
    //                            }
    //                          }
    //                        }))
    //        .orElse(true);
  }

  /** find the occurrence of word within text ignoring case */
  private boolean findOccurrenceWord(String text, String word) {
    if (text == null || word == null) {
      return false;
    }
    // exclude punctuation marks from text
    var rawText = text.replaceAll("[\\p{P}\\s]", EMPTY_STRING);
    var wordArray = rawText.split(EMPTY_STRING);
    return Arrays.stream(wordArray) //
        .filter(str -> !str.isBlank())
        .anyMatch(str -> str.equalsIgnoreCase(word));
  }

  /** find the occurrence of specific phrase within a text */
  private boolean findOccurrencePhrase(String text, String phrase) {
    return toLowerCase(text).indexOf(toLowerCase(phrase)) >= 1;
  }

  private String toLowerCase(String str) {
    return Optional.ofNullable(str).map(String::toLowerCase).orElse(str);
  }
}
