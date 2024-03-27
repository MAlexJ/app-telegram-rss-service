package com.malex.service.resolver;

import com.github.mustachejava.MustacheFactory;
import com.malex.model.entity.RssTopicEntity;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateResolverService {

  private static final String DEFAULT_FORMAT = "%s %n %s";
  private final MustacheFactory mustacheFactory;

  public String applyMessageTemplate(String template, RssTopicEntity topic) {
    try (var writer = new StringWriter();
        var reader = new StringReader(template)) {
      var mustache = mustacheFactory.compile(reader, null);
      return mustache.execute(writer, topic).toString();
    } catch (IOException ex) {
      log.error(ex.getMessage());
      return buildDefaultMessageFormat(topic);
    }
  }

  private String buildDefaultMessageFormat(RssTopicEntity topic) {
    return String.format(DEFAULT_FORMAT, topic.getTitle(), topic.getLink());
  }
}
