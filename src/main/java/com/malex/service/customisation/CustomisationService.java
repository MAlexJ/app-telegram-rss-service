package com.malex.service.customisation;

import com.malex.mapper.rss.RssTopicMapper;
import com.malex.model.customization.Text;
import com.malex.model.dto.RssItemDto;
import com.malex.model.dto.RssTopicDto;
import com.malex.model.entity.CustomizationEntity;
import com.malex.model.request.CustomizationRequest;
import com.malex.model.response.CustomizationResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomisationService {

  private final RssTopicMapper topicMapper;
  private final CustomisationCacheService customisationCacheService;

  public CustomizationResponse save(CustomizationRequest request) {
    return customisationCacheService.saveAndEvictCache(request);
  }

  public Page<CustomizationResponse> findAll(int page, int size) {
    var pageable = PageRequest.of(page, size);
    return customisationCacheService.findAllCacheable(pageable);
  }

  /** Apply Rss Topic customisation */
  public RssTopicDto applyRssTopicCustomization(RssItemDto rssItem) {
    return Optional.ofNullable(rssItem.customizationId())
        .flatMap(
            customizationId ->
                customisationCacheService
                    .findByIdCacheable(customizationId)
                    .map(customization -> applyCustomization(customization, rssItem)))
        .orElseGet(() -> topicMapper.mapToDto(rssItem));
  }

  protected RssTopicDto applyCustomization(CustomizationEntity customisation, RssItemDto rssItem) {
    // rss item description representation
    var document = Jsoup.parse(rssItem.description());
    var image = extractCustomisationImage(document, customisation);
    var text = extractCustomisationText(document, customisation.getText());
    return topicMapper.mapToDto(rssItem, image, text);
  }

  /** Extract image tag from rss item description or provide default */
  private String extractCustomisationImage(Document document, CustomizationEntity customisation) {
    var defaultImage = customisation.getDefaultImage();
    var image = customisation.getImage();
    var tag = image.tag();
    var attribute = image.attribute();
    return Optional.ofNullable(document.selectFirst(tag))
        .map(el -> el.attribute(attribute))
        .map(Attribute::getValue)
        .orElse(defaultImage);
  }

  /**
   * Extract text from rss item description
   *
   * @param document - rss item description representation
   * @param customisation - ?
   */
  private String extractCustomisationText(Document document, Text customisation) {
    var tag = customisation.tag();
    var description =
        document.select(tag).stream()
            .map(Element::text)
            .filter(t -> !t.isBlank())
            .collect(Collectors.joining(" "));
    return extractText(document, description, customisation.exclusionaryPhrases());
  }

  private String extractText(Document document, String description, List<String> excludePhrases) {
    return Optional.of(description)
        .filter(text -> !text.isBlank())
        .or(
            () ->
                Optional.of(document.text())
                    .map(
                        text -> {
                          var result = text;
                          for (String phrase : excludePhrases) {
                            result = result.replace(phrase, "");
                          }
                          return result;
                        }))
        .orElseThrow();
  }
}
