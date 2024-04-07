package com.malex.service.resolver;

import com.github.mustachejava.MustacheFactory;
import com.malex.exception.TemplateResolverException;
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

  private final MustacheFactory mustacheFactory;

  public Optional<String> applyTemplateToRssTopic(String template, RssTopicEntity topic) {
    try (var writer = new StringWriter();
        var reader = new StringReader(template)) {
      var mustache = mustacheFactory.compile(reader, null);
      var raw = mustache.execute(writer, topic);
      return applySpecialCharacterSubstitution(raw);
    } catch (IOException ex) {
      throw new TemplateResolverException(ex);
    }
  }

  /**
   * Apply special character substitution function: <br>
   * 1. Remove 'Zero Width Space'<br>
   * 2. quotes
   */
  private Optional<String> applySpecialCharacterSubstitution(Writer writer) throws IOException {
    try (writer) {
      var text = writer.toString();
      return Optional.ofNullable(text) //
          .map(desc -> desc.replaceAll("[\\p{Cf}]", ""))
          .map(desc -> desc.replace("&quot;", "\""))
          .map(desc -> desc.replace("&#39;", "'"))
          .map(desc -> desc.replace("&amp;#8722;", "-"))
          .map(desc -> desc.replace("&#8722;", "-"));
    }
  }
}
