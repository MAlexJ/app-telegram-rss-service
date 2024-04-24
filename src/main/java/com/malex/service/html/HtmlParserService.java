package com.malex.service.html;

import com.malex.model.dto.CustomizationDto;
import com.malex.webservice.JsoupWebService;
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

  public Optional<String> applyCustomization(String url, String htmlClass) {
    Optional<Document> document = jsoupWebService.readHtml(url);
    return findElementTextByClass(document, htmlClass);
  }

  private Optional<String> findElementTextByClass(
      Optional<Document> documentOpt, String htmlClass) {
    return documentOpt.stream()
        .flatMap(
            document ->
                document.getAllElements().stream()
                    .map(Element::className)
                    .filter(elementClassName -> elementClassName.contains(htmlClass))
                    .map(fullClassName -> findElementsByClass(document, fullClassName)))
        .findFirst();
  }

  private String findElementsByClass(Document document, String fullClassName) {
    Elements elements = document.getElementsByClass(fullClassName);
    return elements.text();
  }

  public Optional<String> applyCustomization(String url, CustomizationDto customizationDto) {
    String titleClass = customizationDto.titleClass();
    String descriptionClass = customizationDto.descriptionClass();
    String imagedClass = customizationDto.imageClass();

    Optional<Document> documentOpt = jsoupWebService.readHtml(url);
    Optional<String> titleOpt = findElementTextByClass(documentOpt, titleClass);
    Optional<String> descriptionOpt = findElementTextByClass(documentOpt, descriptionClass);
    if (titleOpt.isPresent() && descriptionOpt.isPresent()) {
      return Optional.of(titleOpt.get() + "\n" + descriptionOpt.get());
    }
    return Optional.empty();
  }
}
