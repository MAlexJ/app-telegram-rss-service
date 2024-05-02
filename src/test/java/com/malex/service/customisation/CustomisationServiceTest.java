package com.malex.service.customisation;

import static org.junit.jupiter.api.Assertions.*;

import com.malex.model.customization.Image;
import com.malex.model.customization.Text;
import com.malex.model.dto.RssItemDto;
import com.malex.model.entity.CustomizationEntity;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomisationServiceTest {

  private final String defaultImage = "https://image.jpeg";

  @InjectMocks CustomisationService customisationService;

  @Test
  void customisationDescriptionHtmlTest() {
    // given
    var description = "<p>first</p>" + "<p>second</p>" + "<p>last</p>";
    var rssItemDto = buildRssItem(description);

    // when
    var content = customisationService.applyCustomization(buildCustomization(), rssItemDto);

    // then
    assertEquals(defaultImage, content.image());
    assertEquals("first second last", content.description());
  }

  @Test
  void customisationDescriptionHtmlWithTextTest() {
    // given
    var description = "hi! <p>first</p> hello" + " java <p>second</p>" + " word <p>last</p>";
    var subscriptionItemDto = buildRssItem(description);

    // when
    var content =
        customisationService.applyCustomization(buildCustomization(), subscriptionItemDto);

    // then
    assertEquals(defaultImage, content.image());
    assertEquals("first second last", content.description());
  }

  @Test
  void customisationDescriptionOnlyTextImageBottomPositionTest() {
    // given
    var description =
        "<h2>first</h2><br>\n"
            + "second<br>\n"
            + "last"
            + "<img src=\""
            + defaultImage
            + "\" width=\"400\"><br> <a href=\"https://medium=rss&amp;utm_campaign=811435#habracut\"></a>";
    var subscriptionItemDto = buildRssItem(description);

    // when
    var content =
        customisationService.applyCustomization(buildCustomization(), subscriptionItemDto);

    // then
    assertEquals(defaultImage, content.image());
    assertEquals("first second last", content.description());
  }

  @Test
  void customisationDescriptionOnlyTextImageBottomMiddleTest() {
    // given
    var description =
        "first <br><br><a href=\"https://icles/810449/\">"
            + "<img src=\""
            + defaultImage
            + "\"></a><br>"
            + "second <a href=\"https://utm_source=gn=810449#habracut\">last</a>";
    var subscriptionItemDto = buildRssItem(description);

    // when
    var content =
        customisationService.applyCustomization(buildCustomization(), subscriptionItemDto);

    // then
    assertEquals(defaultImage, content.image());
    assertEquals("first second last", content.description());
  }

  @Test
  void customisationDescriptionOnlyTextImageBottomTopTest() {
    // given
    var description =
        "<img src=\""
            + defaultImage
            + "\"></a><br>first <br><br><a href=\"https://rticles/810449/\">"
            + "second <a href=\"https://utm_source=hdium=rssgn=810449#\">last</a>";
    var subscriptionItemDto = buildRssItem(description);

    // when
    var content =
        customisationService.applyCustomization(buildCustomization(), subscriptionItemDto);

    // then
    assertEquals(defaultImage, content.image());
    assertEquals("first second last", content.description());
  }

  private CustomizationEntity buildCustomization() {
    var customization = new CustomizationEntity();
    customization.setDefaultImage(defaultImage);
    customization.setImage(new Image("img", "src"));
    customization.setText(
        new Text("p", List.of("Читать дальше →", "Читать далее", "Читать дальше")));
    return customization;
  }

  private RssItemDto buildRssItem(String description) {
    return RssItemDto.builder()
        .description(description)
        .md5Hash(UUID.randomUUID().toString())
        .build();
  }
}
