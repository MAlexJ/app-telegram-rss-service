package com.malex.service.resolver;

import static org.junit.jupiter.api.Assertions.*;

import com.malex.model.response.SpecialCharacterResponse;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TemplateResolverServiceTest {

  @InjectMocks private TemplateResolverService service;

  @Test
  void replaceSymbol() {
    // given
    var text = "Dungeons &amp; Dragons";
    var response = build(" &amp; ", " & ");

    // when
    var resultText = service.replaceSpecialCharacters(text, Collections.singletonList(response));

    // then
    assertEquals("Dungeons & Dragons", resultText);
  }

  @Test
  void replaceSymbols() {
    // given
    var text = "&001;Dungeons &amp; Dragons&001;";
    var firstSymbol = build(" &amp; ", " & ");
    var secondSymbol = build("&001;", "");

    // when
    var resultText = service.replaceSpecialCharacters(text, List.of(secondSymbol, firstSymbol));

    // then
    assertEquals("Dungeons & Dragons", resultText);
  }

  private SpecialCharacterResponse build(String symbol, String replacement) {
    return new SpecialCharacterResponse("id", symbol, replacement, LocalDateTime.now());
  }
}
