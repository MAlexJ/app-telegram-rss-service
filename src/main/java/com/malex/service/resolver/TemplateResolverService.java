package com.malex.service.resolver;

import com.github.mustachejava.MustacheFactory;
import com.malex.exception.template.TemplateResolverException;
import com.malex.model.entity.RssTopicEntity;
import com.malex.model.response.SpecialCharacterResponse;
import com.malex.service.special.character.SpecialCharacterService;
import com.malex.service.storage.TemplateStorageService;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateResolverService {

  private final MustacheFactory mustacheFactory;
  private final TemplateStorageService templateStorageService;
  private final SpecialCharacterService specialCharacterService;

  /** Find the template and apply to the element */
  public Optional<String> findTemplateAndApplyToRssTopic(String templateId, RssTopicEntity topic) {
    // 1. find template in database or default if not found
    var template = templateStorageService.findExistOrDefaultTemplateById(templateId);
    // 2. apply template to topic
    return mergeRssTopicWithTemplate(topic, template);
  }

  private Optional<String> mergeRssTopicWithTemplate(RssTopicEntity topic, String template) {
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
      var characterList = specialCharacterService.findAll();
      return Optional.ofNullable(text) //
          .map(desc -> replaceSpecialCharacters(desc, characterList));
    }
  }

  String replaceSpecialCharacters(String desc, List<SpecialCharacterResponse> characterList) {
    String result = desc;
    for (SpecialCharacterResponse symbol : characterList) {
      result = result.replaceAll(symbol.symbol(), symbol.replacement());
    }
    return result;
  }
}
