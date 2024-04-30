package com.malex.service;

import com.malex.model.customisation.RssTopicContentCustomisation;
import com.malex.model.dto.RssTopicDto;
import com.malex.model.dto.SubscriptionItemDto;
import com.malex.model.entity.SubscriptionEntity;
import com.malex.service.filter.SubscriptionCriteriaFilteringService;
import com.malex.service.image.ImageService;
import com.malex.service.storage.RssTopicStorageService;
import com.malex.webservice.RssReaderWebService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RssTopicService {

  private final ImageService imageService;
  private final RssReaderWebService rssWebService;
  private final RssTopicStorageService topicStorageService;
  private final SubscriptionCriteriaFilteringService filterService;

  /** Convert Rss items from subscription to rss topics */
  public List<SubscriptionItemDto> readRssTopics(SubscriptionEntity subscription) {
    return rssWebService.readRssNews(subscription).stream().toList();
  }

  public boolean isNotExistTopicByMd5Hash(SubscriptionItemDto item) {
    var md5Hash = item.md5Hash();
    return topicStorageService.isNotExistTopicByMd5Hash(md5Hash);
  }

  /** check whether the filter criteria are included or excluded. */
  public boolean verifyIncludedOrExcludedFilterCriteria(SubscriptionItemDto item) {
    return filterService.applyFilterByCriteria(item);
  }

  /** Apply Rss Topic customisation */
  public RssTopicDto applyRssTopicCustomization(SubscriptionItemDto item) {
    var imageId = item.imageId();
    var description = item.description();
    var customisationContent = applyCustomisationToDescription(imageId, description);
    return new RssTopicDto(item, customisationContent.image(), customisationContent.description());
  }

  public void saveNewRssTopic(RssTopicDto rssTopic) {
    topicStorageService.saveNewRssTopic(rssTopic);
  }

  private RssTopicContentCustomisation applyCustomisationToDescription(
      String imageId, String description) {
    // 1. define image url
    var image = imageService.findById(imageId);
    // 2. parse description
    try {
      Document document = Jsoup.parse(description);
      Optional<String> imageOpt =
          Optional.ofNullable(document.selectFirst("img"))
              .map(el -> el.attribute("src"))
              .map(Attribute::getValue);

      Optional<String> firstParagraph =
          Optional.ofNullable(document.selectFirst("p")).map(Element::text);

      if (imageOpt.isPresent() && firstParagraph.isPresent()) {
        return new RssTopicContentCustomisation(imageOpt.get(), firstParagraph.get());
      }

      if (firstParagraph.isPresent()) {
        return new RssTopicContentCustomisation(image, firstParagraph.get());
      }

    } catch (Exception ex) {
      log.error(ex.getMessage());
    }
    return new RssTopicContentCustomisation(image, description);
  }
}
