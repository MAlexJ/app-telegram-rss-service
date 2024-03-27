package com.malex.service.storage;

import com.malex.mapper.ObjectMapper;
import com.malex.model.entity.TemplateEntity;
import com.malex.model.request.MessageTemplateRequest;
import com.malex.model.request.UpdateMessageTemplateRequest;
import com.malex.model.response.TemplateResponse;
import com.malex.repository.TemplateRepository;
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
@CacheConfig(cacheNames = "templates")
@RequiredArgsConstructor
public class TemplateStorageService {

  private static final String DEFAULT_TEMPLATE = "{{title}} \n {{link}}";

  private final TemplateRepository repository;
  private final ObjectMapper mapper;

  @Cacheable
  public List<TemplateResponse> findAll() {
    return repository.findAll().stream().map(mapper::entityToDto).toList();
  }

  public Optional<TemplateResponse> findById(String id) {
    return repository.findById(id).map(mapper::entityToDto);
  }

  @Cacheable(key = "#id")
  public String findTemplateById(String id) {
    return repository.findById(id).map(TemplateEntity::getTemplate).orElse(DEFAULT_TEMPLATE);
  }

  @CacheEvict(allEntries = true)
  public Optional<TemplateResponse> save(MessageTemplateRequest request) {
    return Optional.of(mapper.dtoToEntity(request)).map(repository::save).map(mapper::entityToDto);
  }

  @CacheEvict(allEntries = true)
  public void update(UpdateMessageTemplateRequest request) {
    var numberOfRecords =
        repository.updateMessageTemplateEntityBy(request.id(), request.template());
    log.info("Updated records: {}", numberOfRecords);
  }

  @CacheEvict(allEntries = true)
  public void deleteBuId(String id) {
    repository.deleteById(id);
  }
}
