package com.malex.controller;

import com.malex.model.dto.RssItemDto;
import com.malex.model.request.RssRequest;
import com.malex.service.html.HtmlParserService;
import com.malex.webservice.RssReaderWebService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/rss")
@RequiredArgsConstructor
public class RssRestController {

  private final RssReaderWebService service;
  private final HtmlParserService htmlParserService;

  @PostMapping
  public ResponseEntity<List<RssItemDto>> findRssItemByCriteria(@RequestBody RssRequest request) {
    log.info("HTTP request, read rss nes by url - {}", request);
    return ResponseEntity.ok(service.readRssNews(request.url()));
  }

  @SneakyThrows
  @PostMapping("/xpath")
  public ResponseEntity<String> findByXpath(@RequestBody XpathRequest request) {
    String url = request.url();
    String htmlClass = request.htmlClass();

    return htmlParserService
        .applyCustomization(url, htmlClass)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  public record XpathRequest(String url, String htmlClass) {}
}
