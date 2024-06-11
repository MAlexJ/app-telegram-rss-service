package com.malex.service.resolver;

import static org.junit.jupiter.api.Assertions.*;

import com.malex.model.response.SpecialCharacterResponse;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TemplateResolverServiceTest {

  @InjectMocks private TemplateResolverService service;

  @Test
  void replace() {
    // given
    var text = "Dungeons &amp; Dragons";
    var response = build(" &amp; ", " & ");

    // when
    var resultText = service.replaceSpecialCharacters(text, Collections.singletonList(response));

    // then
    assertEquals("Dungeons & Dragons", resultText);
  }

  private SpecialCharacterResponse build(String symbol, String replacement) {
    return new SpecialCharacterResponse("id", symbol, replacement, LocalDateTime.now());
  }
}
