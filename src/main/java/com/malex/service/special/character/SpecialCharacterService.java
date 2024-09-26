package com.malex.service.special.character;

import com.malex.model.request.SpecialCharacterRequest;
import com.malex.model.response.SpecialCharacterResponse;
import com.malex.service.storage.SpecialCharacterStorageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecialCharacterService {

  private final SpecialCharacterStorageService storageService;

  public List<SpecialCharacterResponse> findAll() {
    return storageService.findAll();
  }

  public SpecialCharacterResponse save(SpecialCharacterRequest request) {
    return storageService.save(request);
  }
}
