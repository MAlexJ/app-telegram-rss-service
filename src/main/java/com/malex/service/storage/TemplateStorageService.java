package com.malex.service.storage;

import static com.malex.configuration.CacheConfiguration.*;

import com.malex.mapper.ObjectMapper;
import com.malex.model.entity.TemplateEntity;
import com.malex.model.request.TemplateRequest;
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
@CacheConfig(cacheNames = TEMPLATES_CACHE_NAME)
@RequiredArgsConstructor
public class TemplateStorageService {

  private static final String DEFAULT_TEMPLATE = "{{title}} \n {{link}}";

  private final TemplateRepository repository;
  private final ObjectMapper mapper;

  @Cacheable
  public List<TemplateResponse> findAll() {
    log.info("Cacheable: find all template responses");
    return repository.findAll().stream().map(mapper::entityToDto).toList();
  }

  public Optional<TemplateResponse> findById(String id) {
    return repository.findById(id).map(mapper::entityToDto);
  }

  /**
   * Issue: Cached key should be the same as input parameter
   *
   * @param templateId template id
   * @return template placeholder
   */
  @Cacheable(key = TEMPLATES_CACHE_TEMPLATE_KEY_ID)
  public String findExistOrDefaultTemplateById(String templateId) {
    log.info("Cacheable: find exist or default template by id");
    return findTemplateById(templateId).orElse(DEFAULT_TEMPLATE);
  }

  @Cacheable(key = TEMPLATES_CACHE_KEY_ID)
  public Optional<String> findTemplateById(String id) {
    log.info("Cacheable: find template by id");
    return repository.findById(id).map(TemplateEntity::getTemplate);
  }

  @CacheEvict(allEntries = true)
  public Optional<TemplateResponse> save(TemplateRequest request) {
    log.info("CacheEvict: save template - {}", request);
    return Optional.of(mapper.dtoToEntity(request)).map(repository::save).map(mapper::entityToDto);
  }

  @CacheEvict(allEntries = true)
  public void update(UpdateMessageTemplateRequest request) {
    log.info("CacheEvict: update template - {}", request);
    var templateId = request.id();
    var template = request.template();
    Optional.ofNullable(repository.updateMessageTemplateEntityBy(templateId, template))
        .filter(numberOfRecordsUpdated -> numberOfRecordsUpdated > 1)
        .ifPresent(
            numberOfRecordsUpdated -> log.warn("Updated records: {}", numberOfRecordsUpdated));
  }

  @CacheEvict(allEntries = true)
  public void deleteById(String id) {
    log.info("CacheEvict: delete template by id - {}", id);
    repository.deleteById(id);
  }
}
