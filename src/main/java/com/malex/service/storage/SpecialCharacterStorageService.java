package com.malex.service.storage;

import static com.malex.configuration.CacheConfiguration.SPECIAL_CHARACTER_CACHE_NAME;

import com.malex.model.entity.SpecialCharacterEntity;
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

  @Cacheable
  public List<SpecialCharacterEntity> findAll() {
    return repository.findAll();
  }

  @CacheEvict(allEntries = true)
  public SpecialCharacterEntity save(SpecialCharacterEntity entity) {
    return repository.save(entity);
  }
}
