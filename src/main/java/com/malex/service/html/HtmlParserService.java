package com.malex.service.html;

import com.malex.model.dto.ImageDto;
import com.malex.webservice.JsoupWebService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HtmlParserService {

  private final JsoupWebService jsoupWebService;

  public Optional<String> findImageOrDefaultUrlByCriteria(String url, ImageDto image) {
    var imageClassName = image.attributeClassName();
    var keywords = image.additionalClassAttributes();
    return jsoupWebService
        .readHtml(url)
        .flatMap(
            document ->
                findSrcAttributeByClassName(document, imageClassName, keywords)
                    .or(() -> Optional.of(image.defaultImage())));
  }

  private Optional<String> findSrcAttributeByClassName(
      Document document, String imageClassName, List<String> keywords) {
    return document.getAllElements().stream()
        .map(Element::className)
        .filter(className -> className.contains(imageClassName))
        .filter(className -> isMatchingClassKeyWords(keywords, className))
        .map(
            fullClassName -> {
              Elements elements = document.getElementsByClass(fullClassName);
              return elements.text();
            })
        .findFirst();
  }

  private boolean isMatchingClassKeyWords(List<String> keywords, String className) {
    if (keywords.isEmpty()) {
      return true;
    }
    return keywords.stream().anyMatch(attr -> findOccurrencePhrase(className, attr));
  }

  /** find the occurrence of specific phrase within a text */
  private boolean findOccurrencePhrase(String text, String phrase) {
    return toLowerCase(text).indexOf(toLowerCase(phrase)) >= 1;
  }

  private String toLowerCase(String str) {
    return Optional.ofNullable(str).map(String::toLowerCase).orElse(str);
  }
}
