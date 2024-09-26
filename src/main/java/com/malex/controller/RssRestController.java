package com.malex.controller;

import com.malex.model.dto.RssItemDto;
import com.malex.model.request.RssRequest;
import com.malex.webservice.rss.RssReaderWebService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/rss")
@RequiredArgsConstructor
public class RssRestController {

  private final RssReaderWebService service;

  @PostMapping
  public ResponseEntity<List<RssItemDto>> findRssItemByCriteria(@RequestBody RssRequest request) {
    log.info("HTTP request, read rss nes by url - {}", request);
    return ResponseEntity.ok(service.readRssNews(request.url()));
  }
}
