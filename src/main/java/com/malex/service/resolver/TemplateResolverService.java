package com.malex.service.resolver;

import com.github.mustachejava.MustacheFactory;
import com.malex.model.entity.RssTopicEntity;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateResolverService {

  private static final String DEFAULT_FORMAT = "%s %n %s";

  private final MustacheFactory mustacheFactory;

  public String applyTemplateToRssTopic(String template, RssTopicEntity topic) {
    try (var writer = new StringWriter();
        var reader = new StringReader(template)) {
      var mustache = mustacheFactory.compile(reader, null);
      var raw = mustache.execute(writer, topic);
      return applySpecialCharacterSubstitution(raw);
    } catch (IOException ex) {
      log.error(ex.getMessage());
      return buildDefaultMessageFormat(topic);
    }
  }

  private String buildDefaultMessageFormat(RssTopicEntity topic) {
    return String.format(DEFAULT_FORMAT, topic.getTitle(), topic.getLink());
  }

  /**
   * Apply special character substitution function: <br>
   * 1. Remove 'Zero Width Space'<br>
   * 2. quotes
   */
  private String applySpecialCharacterSubstitution(Writer writer) throws IOException {
    try (writer) {
      var text = writer.toString();
      return Optional.ofNullable(text) //
          .map(desc -> desc.replaceAll("[\\p{Cf}]", ""))
          .map(desc -> desc.replace("&quot;", "\""))
          .map(desc -> desc.replace("&#39;", "'"))
          .orElse(text);
    }
  }
}
