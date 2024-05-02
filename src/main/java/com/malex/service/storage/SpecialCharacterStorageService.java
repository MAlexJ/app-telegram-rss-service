package com.malex.service.storage;

import static com.malex.configuration.CacheConfiguration.SPECIAL_CHARACTER_CACHE_NAME;

import com.malex.mapper.ObjectMapper;
import com.malex.model.request.SpecialCharacterRequest;
import com.malex.model.response.SpecialCharacterResponse;
import com.malex.repository.SpecialCharacterRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = SPECIAL_CHARACTER_CACHE_NAME)
public class SpecialCharacterStorageService {

  private final SpecialCharacterRepository repository;
  private final ObjectMapper mapper;

  @Cacheable
  public List<SpecialCharacterResponse> findAll() {
    return repository.findAll().stream().map(mapper::entityToDto).toList();
  }

  @CacheEvict(allEntries = true)
  public SpecialCharacterResponse save(SpecialCharacterRequest request) {
    log.info("CacheEvict: save special character - {}", request);
    var entity = mapper.dtoToEntity(request);
    var persistEntity = repository.save(entity);
    return mapper.entityToDto(persistEntity);
  }
}
