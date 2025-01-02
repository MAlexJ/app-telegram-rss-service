package com.malex.service.customisation;

import static com.malex.configuration.CacheConfiguration.CUSTOMIZATION_CACHE_KEY_ID;
import static com.malex.configuration.CacheConfiguration.CUSTOMIZATION_CACHE_NAME;

import com.malex.model.entity.CustomizationEntity;
import com.malex.model.request.CustomizationRequest;
import com.malex.model.response.CustomizationResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = CUSTOMIZATION_CACHE_NAME)
public class CustomisationCacheService {

  private final CustomizationStorageService customizationStorageService;

  @Cacheable
  public Page<CustomizationResponse> findAllCacheable(PageRequest pageable) {
    log.info("Cacheable: find all customizations");
    return customizationStorageService.findAll(pageable);
  }

  @Cacheable(key = CUSTOMIZATION_CACHE_KEY_ID)
  public Optional<CustomizationEntity> findByIdCacheable(String customizationId) {
    log.info("Cacheable: customization by id - {}", customizationId);
    return customizationStorageService.findById(customizationId);
  }

  @CacheEvict(allEntries = true)
  public CustomizationResponse saveAndEvictCache(CustomizationRequest request) {
    log.info("CacheEvict: save customization");
    return customizationStorageService.save(request);
  }
}
