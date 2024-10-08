package com.malex.controller;

import com.malex.model.request.SpecialCharacterRequest;
import com.malex.model.response.SpecialCharacterResponse;
import com.malex.service.special.character.SpecialCharacterService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/special/characters")
@RequiredArgsConstructor
public class SpecialCharacterRestController {
  private final SpecialCharacterService service;

  @PostMapping
  public ResponseEntity<SpecialCharacterResponse> save(
      @RequestBody SpecialCharacterRequest request) {
    return ResponseEntity.ok(service.save(request));
  }

  @GetMapping
  public ResponseEntity<List<SpecialCharacterResponse>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }
}
