package com.malex.controller;

import com.malex.model.response.RssTopicResponse;
import com.malex.service.storage.RssTopicStorageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/topics")
@RequiredArgsConstructor
public class TopicRestController {

  private final RssTopicStorageService topicService;

  @GetMapping
  public ResponseEntity<List<RssTopicResponse>> findAllTopics() {
    log.info("HTTP request, find all topics");
    return ResponseEntity.ok(topicService.findAllTopics());
  }
}
