package com.malex.service;

import com.malex.model.RssTopic;
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
  public Predicate<RssTopic> applyTopicsFilteringByMd5Hash(Set<String> md5HashSet) {
    return rssTopic -> !md5HashSet.contains(rssTopic.md5Hash());
  }

  /** Apply filtering of rss topics by criteria */
  public boolean applyTopicsFilteringByCriteria(RssTopic rssTopic) {
    var filter = rssTopic.filter();
    if (Objects.isNull(filter)) {
      return true;
    }
    return Optional.ofNullable(filter.condition())
        .map(
            condition -> {
              var topicTitle = rssTopic.title();
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
  public boolean findOccurrenceWord(String text, String word) {
    if (text == null || word == null) {
      return false;
    }
    var excludePunctuationMarksWormText = text.replaceAll("[\\p{P}\\s]", EMPTY_STRING);
    String[] wordList = excludePunctuationMarksWormText.split(EMPTY_STRING);
    return Arrays.stream(wordList) //
        .filter(s -> !s.isBlank())
        .anyMatch(w -> w.equalsIgnoreCase(word));
  }

  /** find the occurrence of specific phrase within a text */
  public boolean findOccurrencePhrase(String text, String phrase) {
    return text.indexOf(phrase) >= 1;
  }
}
