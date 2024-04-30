package com.malex.service.customisation;

import com.malex.model.customisation.RssTopicContentCustomisation;
import com.malex.model.dto.RssTopicDto;
import com.malex.model.dto.SubscriptionItemDto;
import com.malex.service.image.ImageService;
import java.util.Objects;
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
public class CustomisationService {

  private final ImageService imageService;

  /** Apply Rss Topic customisation */
  public RssTopicDto applyRssTopicCustomization(SubscriptionItemDto item) {
    var image = imageService.findImageByIdOrProvideDefault(item.imageId());
    var customizationId = item.customizationId();
    // todo >>> find customisation from db and apply
    return Objects.isNull(customizationId)
        ? new RssTopicDto(item, image)
        : applyCustomization(item, image);
  }

  private RssTopicDto applyCustomization(SubscriptionItemDto item, String image) {
    var description = item.description();
    var customisationContent = applyCustomisationToDescription(image, description);
    String customisationImage = customisationContent.image();
    String customisationDescriptions = customisationContent.description();
    return new RssTopicDto(item, customisationImage, customisationDescriptions);
  }

  private RssTopicContentCustomisation applyCustomisationToDescription(
      String image, String description) {
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
