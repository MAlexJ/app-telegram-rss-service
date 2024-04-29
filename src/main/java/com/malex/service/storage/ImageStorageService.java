package com.malex.service.storage;

import static com.malex.configuration.CacheConfiguration.*;

import com.malex.mapper.ObjectMapper;
import com.malex.model.dto.ImageDto;
import com.malex.model.request.ImageRequest;
import com.malex.repository.ImageRepository;
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
@CacheConfig(cacheNames = IMAGES_CACHE_NAME)
@RequiredArgsConstructor
public class ImageStorageService {

  private final ObjectMapper mapper;
  private final ImageRepository repository;

  @Cacheable
  public List<ImageDto> findAll() {
    log.info("Cacheable: find all active images");
    return repository.findAll().stream().map(mapper::entityToDto).toList();
  }

  @Cacheable(key = IMAGES_CACHE_KEY_ID)
  public Optional<ImageDto> findById(String id) {
    log.info("Cacheable: find image by id");
    return repository.findById(id).map(mapper::entityToDto);
  }

  @CacheEvict(allEntries = true)
  public ImageDto save(ImageRequest request) {
    var entity = mapper.dtoToEntity(request);
    var persistEntity = repository.save(entity);
    return mapper.entityToDto(persistEntity);
  }
}
