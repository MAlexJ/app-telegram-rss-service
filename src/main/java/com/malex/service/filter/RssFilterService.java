package com.malex.service.filter;

import com.malex.model.dto.RssTopicDto;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RssFilterService {

  private static final String EMPTY_STRING = " ";

  /** Apply filtering of rss topics by criteria */
  public boolean applyFilterByCriteria(RssTopicDto topic) {
    throw new UnsupportedOperationException();
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
}
