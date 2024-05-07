package com.malex.service.storage;

import static com.malex.configuration.CacheConfiguration.*;

import com.malex.mapper.ObjectMapper;
import com.malex.model.entity.CustomizationEntity;
import com.malex.model.request.CustomizationRequest;
import com.malex.model.response.CustomizationResponse;
import com.malex.repository.CustomizationRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = CUSTOMIZATION_CACHE_NAME)
public class CustomizationStorageService {

  private final CustomizationRepository repository;
  private final ObjectMapper mapper;

  @Cacheable
  public List<CustomizationResponse> findAll() {
    log.info("Cacheable: find all active customizations");
    return repository.findAll().stream().map(mapper::entityToDto).toList();
  }

  @Cacheable(key = CUSTOMIZATION_CACHE_KEY_ID)
  public Optional<CustomizationEntity> findById(String customizationId) {
    log.info("Cacheable: customization by id - {}", customizationId);
    return repository.findById(customizationId);
  }

  @CacheEvict(allEntries = true)
  public CustomizationResponse save(CustomizationRequest request) {
    log.info("CacheEvict: save customization");
    var entity = mapper.dtoToEntity(request);
    var persistEntity = repository.save(entity);
    return mapper.entityToDto(persistEntity);
  }
}
