package com.malex.service;

import com.malex.model.dto.RssTopicDto;
import com.malex.model.filter.RssFilterValueType;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import org.springframework.stereotype.Service;

@Service
public class RssFilterService {

  private static final String EMPTY_STRING = " ";

  /** Apply filtering of rss topics by md5 hash */
  public Predicate<RssTopicDto> applyTopicsFilteringByMd5Hash(Set<String> md5HashSet) {
    return topic -> !md5HashSet.contains(topic.md5Hash());
  }

  /** Apply filtering of rss topics by criteria */
  public boolean applyTopicsFilteringByCriteria(RssTopicDto topic) {
    var filter = topic.filter();
    if (Objects.isNull(filter)) {
      return true;
    }
    return Optional.ofNullable(filter.condition())
        .map(
            condition -> {
              var topicTitle = topic.title();
              if (RssFilterValueType.PHRASE == condition.valueType()) {
                var phrase = condition.value();
                return findOccurrencePhrase(topicTitle, phrase);
              }
              var word = condition.value();
              return findOccurrenceWord(topicTitle, word);
            })
        .orElse(true);
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
    return text.indexOf(phrase) >= 1;
  }
}
