package com.malex.service;

import com.malex.model.customisation.RssTopicContentCustomisation;
import com.malex.model.dto.RssItemDto;
import com.malex.model.dto.RssTopicDto;
import com.malex.model.entity.SubscriptionEntity;
import com.malex.service.filter.SubscriptionCriteriaFilteringService;
import com.malex.webservice.RssReaderWebService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RssTopicService {
  private final Md5HashService md5HashService;
  private final RssReaderWebService rssWebService;
  private final SubscriptionCriteriaFilteringService filterService;

  @Value("${calculate.md5Hash.for.link}")
  private boolean calculateMd5HashBaseOnLink;

  /** Convert Rss items from subscription to rss topics */
  public List<RssTopicDto> processingRssTopicsWithFilteringCriteria(
      SubscriptionEntity subscription) {
    var url = subscription.getRss();
    var filterIds = subscription.getFilterIds();
    var imageId = subscription.getImageId();
    return rssWebService.readRssNews(url).stream()
        .filter(rssItem -> filterService.applyFilterByCriteria(rssItem, filterIds))
        .map(
            rssItem -> {
              var md5Hash = calculateMd5HashByCriteria(rssItem);
              var customisationContent = applyCustomisationToDescription(imageId, rssItem);
              return new RssTopicDto(
                  subscription,
                  rssItem,
                  customisationContent.image(),
                  customisationContent.description(),
                  md5Hash);
            })
        .toList();
  }

  private RssTopicContentCustomisation applyCustomisationToDescription(
      String imageId, RssItemDto rssItem) {
    // 1. get description
    var description = rssItem.description();
    // 2. define image
    // todo for test only
    var image = "https://upload.wikimedia.org/wikipedia/ru/7/7f/Habrahabr_logo.png";
    if (Objects.isNull(imageId)) {
      image = "";
    }
    try {
      Document document = Jsoup.parse(description);

      // Attribute attribute =
      // Jsoup.parse(description).selectFirst("img").attribute("src").getValue();
      Optional<String> imageOpt =
          Optional.ofNullable(document.selectFirst("img"))
              .map(el -> el.attribute("src"))
              .map(Attribute::getValue);

      // Element firstParagraph =  Jsoup.parse(description).selectFirst("p").text();
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

  private String calculateMd5HashByCriteria(RssItemDto rssItem) {
    var link = rssItem.link();
    if (calculateMd5HashBaseOnLink) {
      return md5HashService.md5HashCalculation(link);
    }
    var title = rssItem.title();
    var description = rssItem.description();
    return md5HashService.md5HashCalculation(title, description);
  }
}
