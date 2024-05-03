package com.malex.service.filter;

import static org.junit.jupiter.api.Assertions.*;

import com.malex.model.dto.RssItemDto;
import com.malex.model.entity.FilterEntity;
import com.malex.model.filter.ConditionType;
import com.malex.model.filter.FilterCondition;
import com.malex.service.storage.FilterStorageService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubscriptionCriteriaFilteringServiceTest {

  @InjectMocks private SubscriptionCriteriaFilteringService service;

  @Mock private FilterStorageService filterStorageService;

  @Test
  void itemWithNullFiltersTest() {
    // given
    var title = "one two second";
    var description = "three four five";
    List<String> filterIds = null;
    var rssItemDto = buildRssItem(title, description, filterIds);

    // when
    boolean actual = service.applyFilteringCriteriaIncludedOrExcluded(rssItemDto);

    // then
    assertTrue(actual);
  }

  @Test
  void itemWithEmptyFiltersTest() {
    // given
    var title = "one two second";
    var description = "three four five";
    var rssItemDto = buildRssItem(title, description, Collections.emptyList());

    // when
    boolean actual = service.applyFilteringCriteriaIncludedOrExcluded(rssItemDto);

    // then
    assertTrue(actual);
  }

  @Test
  void itemFiltersIdsNotFountInDbTest() {
    var title = "one two second";
    var description = "three four five";
    var filterIds = List.of("123456");
    var rssItemDto = buildRssItem(title, description, filterIds);

    // when
    boolean actual = service.applyFilteringCriteriaIncludedOrExcluded(rssItemDto);

    // then
    assertTrue(actual);
  }

  @Test
  void itemIncludeFilterTest() {
    // given
    var title = "one two second";
    var description = "three four five";
    var filterId = "123456";
    var rssItemDto = buildRssItem(title, description, filterId);
    // and
    var filter = buildFilter(filterId, ConditionType.INCLUDE, List.of("second"));

    // when
    mockFilterBehavior(filter);
    // and
    boolean actual = service.applyFilteringCriteriaIncludedOrExcluded(rssItemDto);

    // then
    assertTrue(actual);
  }

  @Test
  void itemExcludeFilterTest() {
    // given
    var title = "one two second";
    var description = "three four five";
    var filterId = "123456";
    var rssItemDto = buildRssItem(title, description, filterId);
    // and
    var filter = buildFilter(filterId, ConditionType.EXCLUDE, List.of("two"));

    // when
    mockFilterBehavior(filter);
    // and
    boolean actual = service.applyFilteringCriteriaIncludedOrExcluded(rssItemDto);

    // then
    assertFalse(actual);
  }

  @Test
  void itemExcludeFilterFirstOccurrenceTest() {
    // given
    var first = "first";
    // and
    var title = first + " two second";
    var description = "three four five";
    var filterId = "123456";
    var rssItemDto = buildRssItem(title, description, filterId);
    // and
    var filter = buildFilter(filterId, ConditionType.EXCLUDE, List.of(first));

    // when
    mockFilterBehavior(filter);
    // and
    boolean actual = service.applyFilteringCriteriaIncludedOrExcluded(rssItemDto);

    // then
    assertFalse(actual);
  }

  @Test
  void itemExcludeFilterLastOccurrenceTest() {
    // given
    var last = "last";
    // and
    var title = "one two second";
    var description = "three four five" + last;
    var filterId = "123456";
    var rssItemDto = buildRssItem(title, description, filterId);
    // and
    var filter = buildFilter(filterId, ConditionType.EXCLUDE, List.of(last));

    // when
    mockFilterBehavior(filter);
    // and
    boolean actual = service.applyFilteringCriteriaIncludedOrExcluded(rssItemDto);

    // then
    assertFalse(actual);
  }

  private void mockFilterBehavior(FilterEntity filter) {
    Mockito.lenient().when(filterStorageService.findAllActiveFilters()).thenReturn(List.of(filter));
  }

  private static FilterEntity buildFilter(
      String filterId, ConditionType type, List<String> phrases) {
    FilterEntity filter = new FilterEntity();
    filter.setId(filterId);
    filter.setCondition(new FilterCondition(type, phrases));
    filter.setCreated(LocalDateTime.now());
    return filter;
  }

  private RssItemDto buildRssItem(String title, String description, String filterId) {
    return RssItemDto.builder()
        .title(title)
        .description(description)
        .filterIds(List.of(filterId))
        .build();
  }

  private RssItemDto buildRssItem(String title, String description, List<String> filterIds) {
    return RssItemDto.builder().title(title).description(description).filterIds(filterIds).build();
  }
}
