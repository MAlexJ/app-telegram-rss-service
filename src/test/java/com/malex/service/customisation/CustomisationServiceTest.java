package com.malex.service.customisation;

import com.malex.mapper.rss.RssTopicMapper;
import com.malex.model.customization.Image;
import com.malex.model.customization.Text;
import com.malex.model.dto.RssItemDto;
import com.malex.model.entity.CustomizationEntity;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

@ExtendWith(MockitoExtension.class)
@Import(RssTopicMapper.class)
class CustomisationServiceTest {

  private final String defaultImage = "https://image.jpeg";

  @InjectMocks private CustomisationService customisationService;

  @Mock private RssTopicMapper topicMapper;

  @BeforeEach
  public void init() {}

  @Test
  void customisationDescriptionHtmlTest() {
    // given
    var description = "<p>first</p>" + "<p>second</p>" + "<p>last</p>";
    var rssItemDto = buildRssItem(description);

    // when
    customisationService.applyCustomization(buildCustomizationWithDefaultImage(), rssItemDto);

    Mockito.verify(topicMapper, Mockito.times(1))
        .mapToDto(rssItemDto, defaultImage, "first second last");
  }

  @Test
  void customisationDescriptionHtmlWithTextTest() {
    // given
    var description = "hi! <p>first</p> hello" + " java <p>second</p>" + " word <p>last</p>";
    var rssItemDto = buildRssItem(description);

    // when
    customisationService.applyCustomization(buildCustomizationWithDefaultImage(), rssItemDto);

    // then
    Mockito.verify(topicMapper, Mockito.times(1))
        .mapToDto(rssItemDto, defaultImage, "first second last");
  }

  @Test
  void customisationDescriptionOnlyTextImageBottomPositionTest() {
    // given
    var image = "https://new_image.jpeg";
    // and
    var description =
        "<h2>first</h2><br>\n"
            + "second<br>\n"
            + "last"
            + "<img src=\""
            + image
            + "\" width=\"400\"><br> <a href=\"https://medium=rss&amp;utm_campaign=811435#habracut\"></a>";
    var rssItemDto = buildRssItem(description);

    // when
    customisationService.applyCustomization(buildCustomizationWithDefaultImage(), rssItemDto);

    // then
    Mockito.verify(topicMapper, Mockito.times(1)).mapToDto(rssItemDto, image, "first second last");
  }

  @Test
  void customisationDescriptionOnlyTextImageBottomMiddleTest() {
    // given
    var image = "https://new_image.jpeg";
    // and
    var description =
        "first <br><br><a href=\"https://icles/810449/\">"
            + "<img src=\""
            + image
            + "\"></a><br>"
            + "second <a href=\"https://utm_source=gn=810449#habracut\">last</a>";
    var rssItemDto = buildRssItem(description);

    // when
    customisationService.applyCustomization(buildCustomizationWithDefaultImage(), rssItemDto);

    // then
    Mockito.verify(topicMapper, Mockito.times(1)).mapToDto(rssItemDto, image, "first second last");
  }

  @Test
  void customisationDescriptionOnlyTextImageBottomTopTest() {
    // given
    var image = "https://new_image.jpeg";
    // and
    var description =
        "<img src=\""
            + image
            + "\"></a><br>first <br><br><a href=\"https://rticles/810449/\">"
            + "second <a href=\"https://utm_source=hdium=rssgn=810449#\">last</a>";
    var rssItemDto = buildRssItem(description);

    // when
    customisationService.applyCustomization(buildCustomizationWithDefaultImage(), rssItemDto);

    // then
    Mockito.verify(topicMapper, Mockito.times(1)).mapToDto(rssItemDto, image, "first second last");
  }

  private CustomizationEntity buildCustomizationWithDefaultImage() {
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
