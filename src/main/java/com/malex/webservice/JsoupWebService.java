package com.malex.webservice;

import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JsoupWebService {

  public Optional<Document> readHtml(String url) {
    Document document = null;
    try {
      var connection = Jsoup.connect(url);
      document = connection.get();
    } catch (IOException e) {
      log.error("Error parsing html by url - {}, error - {}", url, e.getMessage());
    }
    return Optional.ofNullable(document);
  }
}
