package com.malex.service.storage;

import static com.malex.configuration.CacheConfiguration.*;

import com.malex.mapper.ObjectMapper;
import com.malex.model.entity.FilterEntity;
import com.malex.model.request.FilterRequest;
import com.malex.model.response.FilterResponse;
import com.malex.repository.FilterRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@CacheConfig(cacheNames = FILTERS_CACHE_NAME)
@RequiredArgsConstructor
public class FilterStorageService {

  private final FilterRepository repository;
  private final ObjectMapper mapper;

  @Cacheable
  public List<FilterResponse> findAllFilters() {
    return repository.findAll().stream().map(mapper::entityToDto).toList();
  }

  @Cacheable(key = FILTERS_CACHE_KEY_ID)
  public List<FilterEntity> findAllActiveFilters() {
    log.info("Cacheable: find all active filters");
    return repository.findAllByActive(true);
  }

  @CacheEvict(allEntries = true)
  public FilterResponse save(FilterRequest request) {
    var entity = mapper.dtoToEntity(request);
    var persistEntity = repository.save(entity);
    return mapper.entityToDto(persistEntity);
  }

  /** Inactive filter by id */
  @CacheEvict(allEntries = true)
  public Integer disableFilterById(String id) {
    return repository.setInactiveFilterStatusById(id).intValue();
  }
}
